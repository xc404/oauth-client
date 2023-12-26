package io.github.xc404.oauth.core;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import io.github.xc404.oauth.OAuthException;
import io.github.xc404.oauth.context.DefaultOAuthContextProvider;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.context.OAuthContextProvider;
import io.github.xc404.oauth.oauth2.protocol.AdditionalParamsAuthorizationGrant;
import io.github.xc404.oauth.oidc.protocol.IdToken;
import io.github.xc404.oauth.utils.OAuthParser;
import io.github.xc404.oauth.utils.UrlUtils;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nimbusds.oauth2.sdk.OAuth2Error.SERVER_ERROR_CODE;

/**
 * @Author chaox
 * @Date 12/22/2023 5:10 PM
 */
public class OAuthService
{
    public static final String AUTHORIZATION_REQUEST_CONTEXT_KEY = "authorizationRequest";
    public static final String AUTHENTICATION_RESPONSE_CONTEXT_KEY = "authenticationResponse";
    private final OAuthClientFactory oAuthClientFactory;
    private final OAuthContextProvider oAuthContextProvider;

    public OAuthService(OAuthClientFactory oAuthClientFactory) {
        this(oAuthClientFactory, DefaultOAuthContextProvider.getInstance());
    }

    public OAuthService(OAuthClientFactory oAuthClientFactory, OAuthContextProvider oAuthContextProvider) {
        this.oAuthClientFactory = oAuthClientFactory;
        this.oAuthContextProvider = oAuthContextProvider;
    }

    private static URI redirect(InnerAuthorizationRequest innerAuthorizationRequest,
                                ErrorObject errorObject) {
        HashMap<String, String> params = new HashMap<>();
        params.put("state", innerAuthorizationRequest.getState());
        params.put("provider", innerAuthorizationRequest.getProvider());
        if( errorObject != null ) {
            params.put("error", errorObject.getCode());
            params.put("error_description", errorObject.getDescription());
        }
        try {
            return new URI(UrlUtils.appendQuery(innerAuthorizationRequest.getRedirectUrl(), params));
        } catch( URISyntaxException e ) {
            throw new RuntimeException(e);
        }
    }

    public URI requestAuthorization(String provider, String redirectUrl, String state) {
        InnerAuthorizationRequest request = new InnerAuthorizationRequest(provider, redirectUrl, state);
        try {
            OAuthClient oAuthClient = getOAuthClient(provider);
            OAuthContext oAuthContext = oAuthContextProvider.createOAuthContext(Duration.ofMillis(oAuthClient.getOAuthConfig().getTimeout()));
            oAuthContext.setContext(AUTHORIZATION_REQUEST_CONTEXT_KEY, request);
            return oAuthClient.requestAuthorization(oAuthContext);
        } catch( Throwable e ) {
            ErrorObject errorObject;
            if( e instanceof OAuthException ) {
                errorObject = ((OAuthException) e).getErrorObject();
            } else {
                errorObject = new ErrorObject(SERVER_ERROR_CODE, e.getMessage());
            }
            return redirect(request, errorObject);
        }
    }

    public AuthorizationResult authorizationCallback(URI requestUrl) {

        OAuthParser.AuthorizationResponse parse = OAuthParser.parseAuthorizationResponse(requestUrl);
        AuthenticationResponse response = parse.getAuthenticationResponse();
        State state = response.getState();
        if( state == null ) {
            throw new OAuthException(OAuth2Error.INVALID_REQUEST);
        }
        OAuthContext oAuthContext = oAuthContextProvider.getOAuthContext(state);
        if( oAuthContext == null ) {
            throw new OAuthException(new ErrorObject(SERVER_ERROR_CODE, "session expired"));
        }
        InnerAuthorizationRequest request = getContext(oAuthContext, AUTHORIZATION_REQUEST_CONTEXT_KEY);
        try {
            if( !response.indicatesSuccess() ) {
                ErrorObject errorObject = response.toErrorResponse().getErrorObject();
                return new AuthorizationResult(redirect(request, errorObject), null);
            }

            AuthenticationSuccessResponse successResponse = response.toSuccessResponse();


            InnerAuthenticationResponse authenticationResponse = new InnerAuthenticationResponse(request.provider, successResponse, parse.getAdditionalParameters());
            oAuthContext.setContext(AUTHENTICATION_RESPONSE_CONTEXT_KEY, authenticationResponse);
            LoginToken loginToken = LoginToken.fromState(state);
            //return with loginToken;
            return new AuthorizationResult(redirect(request, null), loginToken);
        } catch( Throwable e ) {

            ErrorObject errorObject;
            if( e instanceof OAuthException ) {
                errorObject = ((OAuthException) e).getErrorObject();
            } else {
                errorObject = new ErrorObject(SERVER_ERROR_CODE, e.getMessage());
            }
            return new AuthorizationResult(redirect(request, errorObject), null);
        }
    }

    public UserInfo authenticate(String provider, AuthorizationGrant authorizationGrant) {
        AuthorizationGrant grant = authorizationGrant;
        if( authorizationGrant instanceof AdditionalParamsAuthorizationGrant ) {
            grant = ((AdditionalParamsAuthorizationGrant) authorizationGrant).getProxy();
        }
        if( grant instanceof LoginTokenGrant ) {
            return this.loginWithLoginToken(((LoginTokenGrant) grant).getLoginToken());
        }
        OAuthClient oAuthClient = getOAuthClient(provider);

        AccessTokenResponse accessTokenResponse = oAuthClient.authenticate(OAuthContext.empty(), authorizationGrant);
        return authenticate(provider, OAuthContext.empty(), UserInfoRequest.of(accessTokenResponse));
    }

    public UserInfo loginWithLoginToken(LoginToken loginToken) {
        OAuthContext oAuthContext = this.oAuthContextProvider.getOAuthContext(loginToken.toState());
        if( oAuthContext == null ) {
            throw new IllegalStateException("oauth context not found");
        }
        try {
            InnerAuthenticationResponse response = getContext(oAuthContext, AUTHENTICATION_RESPONSE_CONTEXT_KEY);
            return authenticate(response.getProvider(), oAuthContext, response);
        } finally {
            oAuthContext.clear();
        }
    }

    public UserInfo authenticate(String provider, OAuthContext oAuthContext, UserInfoRequest request) {
        OAuthClient oAuthClient = getOAuthClient(provider);
        if( request.getUserInfo() != null ) {
            return request.getUserInfo();
        }
        if( request.getIdToken() != null ) {
            oAuthClient.getUserInfo(oAuthContext, request.getIdToken());
        }
        if( request.getAccessToken() != null ) {
            return oAuthClient.getUserInfo(request.getAccessToken());
        }
        if( request.getAuthorizationGrant() != null ) {
            return authenticate(provider, request.getAuthorizationGrant());
        }
        return null;
    }

    protected <T> T getContext(OAuthContext oAuthContext, String key) {
        T cache = (T) oAuthContext.getContext(key);
        if( cache == null ) {
            throw new IllegalStateException(key + " not in oauth context");
        }
        return cache;
    }

    protected OAuthClient getOAuthClient(String provider) {
        OAuthClient oAuthClient = this.oAuthClientFactory.getOAuthClient(provider);
        if( oAuthClient == null ) {
            throw new IllegalStateException("oauth client not found for " + provider);
        }
        return oAuthClient;
    }

    private static class InnerAuthorizationRequest implements Serializable
    {
        private final String provider;
        private final String redirectUrl;
        private final String state;

        public InnerAuthorizationRequest(String provider, String redirectUrl, String state) {
            this.provider = provider;
            this.redirectUrl = redirectUrl;
            this.state = state;
        }

        public String getProvider() {
            return provider;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public String getState() {
            return state;
        }
    }


    private static class InnerAuthenticationResponse implements UserInfoRequest, Serializable
    {

        private final String provider;
        private final IdToken idToken;
        private final AccessToken accessToken;
        private final AuthorizationCode authorizationCode;
        private final Map<String, List<String>> additionalParameters;

        public InnerAuthenticationResponse(String provider, AuthenticationSuccessResponse successResponse, Map<String, List<String>> additionalParameters) {
            this.provider = provider;
            this.idToken = successResponse.getIDToken() == null ? null : new IdToken(successResponse.getIDToken());
            this.accessToken = successResponse.getAccessToken();
            this.authorizationCode = successResponse.getAuthorizationCode();
            this.additionalParameters = additionalParameters;
        }

        public String getProvider() {
            return provider;
        }

        public IdToken getIdToken() {
            return idToken;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }


        @Override
        public UserInfo getUserInfo() {
            return null;
        }

        @Override
        public AuthorizationGrant getAuthorizationGrant() {
            if( this.authorizationCode != null ) {
                AuthorizationGrant grant = new AuthorizationCodeGrant(authorizationCode, null, null);
                if( this.additionalParameters != null ) {
                    grant = new AdditionalParamsAuthorizationGrant(grant, (Map) additionalParameters);
                }
                return grant;
            }
            return null;
        }
    }


}

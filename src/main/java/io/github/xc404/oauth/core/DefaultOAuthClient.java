package io.github.xc404.oauth.core;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.OIDCScopeValue;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import io.github.xc404.oauth.OAuthException;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.oauth2.AdditionalParamsAuthorizationGrant;
import io.github.xc404.oauth.oidc.IdToken;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.oidc.OidcUserInfoClaim;
import io.github.xc404.oauth.oidc.StandardUserInfoConvertor;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.oidc.UserInfoTokenResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nimbusds.jose.jwk.source.JWKSourceBuilder.DEFAULT_CACHE_REFRESH_TIMEOUT;
import static com.nimbusds.jose.jwk.source.JWKSourceBuilder.DEFAULT_REFRESH_AHEAD_TIME;


/**
 * @Author chaox
 * @Date 12/20/2023 4:03 PM
 */
public class DefaultOAuthClient implements OAuthClient
{
    public static final String APPLICATION_JSON_ACCEPT = "application/json";
    protected OAuthConfig authConfig;
    protected ClientID clientID;
    protected Secret clientSecret;
    protected URI redirectUri;
    protected URI authorizationURI;
    protected Scope scope;
    protected URI issuerURI;
    protected URI jwkSetURI;
    protected boolean supportOIDC;
    protected URI tokenURI;
    protected URI userInfoURI;
    protected Issuer issuer;
    protected IDTokenValidator idTokenValidator;
    protected boolean requiredOIDC;

    protected UserInfoConvertor userInfoConvertor = new StandardUserInfoConvertor();


    public DefaultOAuthClient(OAuthConfig authConfig) {
        this.authConfig = authConfig;
        this.clientID = new ClientID(authConfig.getClientId());
        this.clientSecret = new Secret(authConfig.getClientSecret());
        this.redirectUri = URI.create(authConfig.getRedirectUri());
        this.authorizationURI = URI.create(authConfig.getAuthorizationUri());
        this.tokenURI = URI.create(authConfig.getTokenUri());
        this.userInfoURI = URI.create(authConfig.getUserInfoUri());
        this.scope = Scope.parse(authConfig.getScopes());
        this.requiredOIDC = scope != null && scope.contains(OIDCScopeValue.OPENID);
        this.issuerURI = StringUtils.isNotBlank(authConfig.getIssuerUri()) ? URI.create(authConfig.getIssuerUri()) : null;
        this.jwkSetURI = StringUtils.isNotBlank(authConfig.getJwkSetUri()) ? URI.create(authConfig.getJwkSetUri()) : null;
        this.supportOIDC = (this.issuerURI != null && this.jwkSetURI != null);
        if( supportOIDC ) {
            this.issuer = new Issuer(issuerURI);
            try {
                JWKSource<SecurityContext> jwkSource = JWKSourceBuilder.create(jwkSetURI.toURL())
                        .cache(authConfig.getJwkSetRefreshInterval(), DEFAULT_CACHE_REFRESH_TIMEOUT)
                        .refreshAheadCache(DEFAULT_REFRESH_AHEAD_TIME, true).build();
                this.idTokenValidator = new IDTokenValidator(issuer, clientID, new JWSVerificationKeySelector<>(JWSAlgorithm.HS256, jwkSource), null);
            } catch( MalformedURLException e ) {
                throw new IllegalArgumentException(e);
            }
            if( this.scope == null ) {
                this.scope = new Scope(OIDCScopeValue.OPENID);
            }
        }
    }

    /**
     * @param oAuthContext oauth context in oauth flow.
     * @return URI to send the end-user's browser to the server
     * @see <a href="https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/oauth/authorization-request">Example authorisation request (code flow)</a>
     */
    @Override
    public URI requestAuthorization(OAuthContext oAuthContext) {
        oAuthContext.setStep(OAuthContext.OAuthStep.AUTHORIZATION);
        State state = oAuthContext.getState();
        if( state == null ) {
            throw new IllegalStateException("State not exist in oauth context.");
        }
        if( oAuthContext.getStep() != OAuthContext.OAuthStep.AUTHORIZATION ) {
            throw new IllegalStateException("Illegal oauth state.");
        }

        // Generate random state string for pairing the response to the request
        CodeVerifier codeVerifier = authConfig.isEnablePkce() ? oAuthContext.getCodeVerifier() : null;
        Nonce nonce = requiredOIDC ? oAuthContext.getNonce() : null;
        AuthorizationRequest request;

        // Build the request
        if( requiredOIDC ) {
            request = new AuthenticationRequest.Builder(
                    this.authConfig.getResponseType(), scope, clientID, redirectUri)
                    .scope(scope)
                    .state(state)
                    .codeChallenge(codeVerifier, authConfig.getCodeChallengeMethod())
                    .endpointURI(authorizationURI)
                    .nonce(nonce)
                    .build();
        } else {
            request = new AuthorizationRequest.Builder(this.authConfig.getResponseType(), clientID)
                    .scope(scope)
                    .state(state)
                    .codeChallenge(codeVerifier, authConfig.getCodeChallengeMethod())
                    .endpointURI(authorizationURI)
                    .build();
        }

        // Use this URI to send the end-user's browser to the server
        return request.toURI();
    }


    /**
     * @param oAuthContext       oauth context in oauth flow.
     * @param authorizationGrant Authorization grant.
     * @return access token
     * @see <a href="https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/oauth/token-request#code">Example token request with a code grant</a>
     */
    @Override
    public AccessTokenResponse authenticate(OAuthContext oAuthContext, AuthorizationGrant authorizationGrant) {
        oAuthContext.setStep(OAuthContext.OAuthStep.AUTHENTICATE);
        // The token endpoint
        URI tokenEndpoint = this.tokenURI;
        Map<String, List<String>> parameters = null;
        if( authorizationGrant instanceof AdditionalParamsAuthorizationGrant ) {
            authorizationGrant = ((AdditionalParamsAuthorizationGrant) authorizationGrant).getProxy();
            parameters = authorizationGrant.toParameters();
        }
        if( authorizationGrant instanceof AuthorizationCodeGrant ) {
            URI redirectionURI = Optional.ofNullable(
                    ((AuthorizationCodeGrant) authorizationGrant).getRedirectionURI()).orElse(this.redirectUri);
            CodeVerifier codeVerifier = Optional.ofNullable
                            (((AuthorizationCodeGrant) authorizationGrant).getCodeVerifier())
                    .orElse(oAuthContext.getCodeVerifier());
            authorizationGrant = new AuthorizationCodeGrant(((AuthorizationCodeGrant) authorizationGrant).getAuthorizationCode(), redirectionURI, codeVerifier);
        }
        // Make the token request
        ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
        TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, authorizationGrant, scope, null, parameters);


        HTTPResponse httpResponse;
        try {
            HTTPRequest httpRequest = request.toHTTPRequest();
            httpRequest.setAccept(APPLICATION_JSON_ACCEPT);
            httpResponse = httpRequest.send();
            if( !httpResponse.indicatesSuccess() ) {
                throw new OAuthException(ErrorObject.parse(httpResponse));
            }
        } catch( IOException e ) {
            throw new OAuthException(e);
        }
        OIDCTokenResponse response;
        try {
            response = OIDCTokenResponse.parse(httpResponse);
        } catch( ParseException e ) {
            throw new OAuthException(ErrorObject.parse(httpResponse));
        }

        if( !response.indicatesSuccess() ) {
            // We got an error response...
            TokenErrorResponse errorResponse = response.toErrorResponse();
            throw new OAuthException(errorResponse.getErrorObject());
        }


        OIDCTokenResponse oidcTokenResponse = response.toSuccessResponse();
        JWT idToken = oidcTokenResponse.getOIDCTokens().getIDToken();
        if( idToken != null ) {
            validateIdToken(oAuthContext, new IdToken(idToken));
            oidcTokenResponse = UserInfoTokenResponse.from(oidcTokenResponse);
        }
        return oidcTokenResponse;
    }


    @Override
    public OidcUserInfo getUserInfo(AccessToken accessToken) {
        try {
            HTTPRequest httpRequest = new UserInfoRequest(this.userInfoURI,
                    this.authConfig.getUserInfoHttpMethod(),
                    accessToken)
                    .toHTTPRequest();
            httpRequest.setAccept(APPLICATION_JSON_ACCEPT);
            HTTPResponse httpResponse = httpRequest.send();
            if( !httpResponse.indicatesSuccess() ) {
                throw new OAuthException(ErrorObject.parse(httpResponse));
            }

            OidcUserInfo userInfo = convertToUserInfo(httpResponse);
            if( userInfo == null ) {
                throw new OAuthException(ErrorObject.parse(httpResponse));
            }
            return new ClientUserInfo(this.authConfig.getProvider(), userInfo);

        } catch( IOException e ) {
            throw new OAuthException(e);
        }
    }


    protected void validateIdToken(OAuthContext oAuthContext, IdToken idToken) {
        try {
            oAuthContext.setStep(OAuthContext.OAuthStep.AUTHENTICATE);
            idTokenValidator.validate(idToken.getJwt(), oAuthContext.getNonce());
        } catch( BadJOSEException | JOSEException e ) {
            throw new OAuthException(e);
        }
    }

    public OidcUserInfo getUserInfo(OAuthContext oAuthContext, IdToken idToken) {

        validateIdToken(oAuthContext, idToken);
        return new ClientUserInfo(getOAuthConfig().getProvider(), new OidcUserInfoClaim(idToken.getJWTClaimsSet().getClaims()));
    }

    @Override
    public OAuthConfig getOAuthConfig() {
        return this.authConfig;
    }


    protected OidcUserInfo convertToUserInfo(HTTPResponse httpResponse) {
        UserInfoConvertor convertor = this.getUserInfoConvertor();
        OidcUserInfo userInfo = convertor.toUserInfo(httpResponse);
        if( userInfo == null ) {
            try {
                return convertor.toUserUserInfo(httpResponse.getBodyAsJSONObject());
            } catch( ParseException e ) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public UserInfoConvertor getUserInfoConvertor() {
        return userInfoConvertor;
    }

    public void setUserInfoConvertor(UserInfoConvertor userInfoConvertor) {
        this.userInfoConvertor = userInfoConvertor;
    }
}

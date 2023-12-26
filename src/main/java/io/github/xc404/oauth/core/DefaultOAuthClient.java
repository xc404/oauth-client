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
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
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
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import io.github.xc404.oauth.OAuthException;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.oauth2.protocol.AdditionalParamsAuthorizationGrant;
import io.github.xc404.oauth.oidc.protocol.IdToken;
import io.github.xc404.oauth.oidc.protocol.UserInfoTokenResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nimbusds.jose.jwk.source.JWKSourceBuilder.DEFAULT_CACHE_REFRESH_TIMEOUT;
import static com.nimbusds.jose.jwk.source.JWKSourceBuilder.DEFAULT_REFRESH_AHEAD_TIME;
import static io.github.xc404.oauth.utils.UrlUtils.toURI;


/**
 * @Author chaox
 * @Date 12/20/2023 4:03 PM
 */
public class DefaultOAuthClient implements OAuthClient
{
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


    public DefaultOAuthClient(OAuthConfig authConfig) {
        this.authConfig = authConfig;
        this.clientID = new ClientID(authConfig.getClientId());
        this.clientSecret = new Secret(authConfig.getClientSecret());
        this.redirectUri = toURI(authConfig.getRedirectUri());
        this.authorizationURI = toURI(authConfig.getAuthorizationUri());
        this.tokenURI = toURI(authConfig.getTokenUri());
        this.userInfoURI = toURI(authConfig.getUserInfoUri());
        this.scope = Scope.parse(authConfig.getScopes());
        this.requiredOIDC = scope.contains(OIDCScopeValue.OPENID);
        this.issuerURI = StringUtils.isNotBlank(authConfig.getIssuerUri()) ? toURI(authConfig.getIssuerUri()) : null;
        this.jwkSetURI = StringUtils.isNotBlank(authConfig.getJwkSetUri()) ? toURI(authConfig.getJwkSetUri()) : null;
        this.supportOIDC = (this.issuerURI != null && this.jwkSetURI != null);
        if( supportOIDC ) {
            this.issuer = new Issuer(issuerURI);
            try {
                JWKSource<SecurityContext> jwkSource = JWKSourceBuilder.create(jwkSetURI.toURL())
                        .cache(authConfig.getJwkSetRefreshInterval(),DEFAULT_CACHE_REFRESH_TIMEOUT)
                        .refreshAheadCache(DEFAULT_REFRESH_AHEAD_TIME, true).build();
                this.idTokenValidator = new IDTokenValidator(issuer, clientID, new JWSVerificationKeySelector<>(JWSAlgorithm.HS256, jwkSource), null);
            } catch( MalformedURLException e ) {
                throw new IllegalArgumentException(e);
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

        // Build the request
        AuthenticationRequest request = new AuthenticationRequest.Builder(
                new ResponseType(ResponseType.Value.CODE), scope, clientID, redirectUri)
                .scope(scope)
                .state(state)
                .codeChallenge(codeVerifier, authConfig.getCodeChallengeMethod())
                .endpointURI(authorizationURI)
                .nonce(nonce)
                .build();

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
        oAuthContext.setStep(OAuthContext.OAuthStep.AUTHORIZATION);
        // The token endpoint
        URI tokenEndpoint = this.tokenURI;
        Map<String, List<String>> parameters = null;
        if( authorizationGrant instanceof AdditionalParamsAuthorizationGrant ) {
            authorizationGrant = ((AdditionalParamsAuthorizationGrant) authorizationGrant).getProxy();
            parameters = authorizationGrant.toParameters();
        }
        if( authorizationGrant instanceof AuthorizationCodeGrant ) {
            URI redirectionURI = Optional.ofNullable(
                    ((AuthorizationCodeGrant) authorizationGrant).getRedirectionURI()).orElse(this.authorizationURI);
            CodeVerifier codeVerifier = Optional.ofNullable
                            (((AuthorizationCodeGrant) authorizationGrant).getCodeVerifier())
                    .orElse(oAuthContext.getCodeVerifier());
            authorizationGrant = new AuthorizationCodeGrant(((AuthorizationCodeGrant) authorizationGrant).getAuthorizationCode(), redirectionURI, codeVerifier);
        }
        // Make the token request
        TokenRequest request;
        if( authorizationGrant.getType().requiresClientAuthentication() ) {
            ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
            request = new TokenRequest(tokenEndpoint, clientAuth, authorizationGrant, scope, null, parameters);
        } else {
            request = new TokenRequest(tokenEndpoint, clientID, authorizationGrant, scope, null, null, parameters);
        }

        OIDCTokenResponse response;
        try {
            HTTPResponse send = request.toHTTPRequest().send();
            response = OIDCTokenResponse.parse(send);
        } catch( ParseException | IOException e ) {
            throw new RuntimeException(e);
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
    public UserInfo getUserInfo(AccessToken accessToken) {
        try {
            HTTPResponse httpResponse = new UserInfoRequest(this.userInfoURI, accessToken)
                    .toHTTPRequest()
                    .send();

            // Parse the response
            UserInfoResponse userInfoResponse = UserInfoResponse.parse(httpResponse);

            if( !userInfoResponse.indicatesSuccess() ) {
                // The request failed, e.g. due to invalid or expired token
                throw new OAuthException(userInfoResponse.toErrorResponse().getErrorObject());
            }

            // Extract the claims
            return userInfoResponse.toSuccessResponse().getUserInfo();

        } catch( IOException | ParseException e ) {
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

    public UserInfo getUserInfo(OAuthContext oAuthContext, IdToken idToken) {

        validateIdToken(oAuthContext, idToken);
        return new UserInfo(idToken.getJWTClaimsSet());
    }

    @Override
    public OAuthConfig getOAuthConfig() {
        return this.authConfig;
    }


}

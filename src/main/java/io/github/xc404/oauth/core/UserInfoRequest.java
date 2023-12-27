package io.github.xc404.oauth.core;

import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import io.github.xc404.oauth.oauth2.AdditionalParamsAuthorizationGrant;
import io.github.xc404.oauth.oidc.IdToken;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.oidc.UserInfoTokenResponse;

import java.util.Map;

/**
 * @Author chaox
 * @Date 12/25/2023 6:04 PM
 */
public interface UserInfoRequest
{
    static UserInfoRequest of(AccessTokenResponse accessTokenResponse) {
        return new AuthenticateResponse(accessTokenResponse);
    }

    static UserInfoRequest of(AuthorizationSuccessResponse authorizationResponse) {
        return new AuthorizationResponse(authorizationResponse, null);
    }

    static UserInfoRequest of(AuthorizationSuccessResponse authorizationResponse, Map<String, Object> parameters) {
        return new AuthorizationResponse(authorizationResponse, parameters);
    }

    OidcUserInfo getUserInfo();

    IdToken getIdToken();

    AccessToken getAccessToken();

    AuthorizationGrant getAuthorizationGrant();

    class AuthenticateResponse implements UserInfoRequest
    {
        private final AccessTokenResponse accessTokenResponse;

        public AuthenticateResponse(AccessTokenResponse accessTokenResponse) {
            this.accessTokenResponse = accessTokenResponse;
        }

        @Override
        public OidcUserInfo getUserInfo() {
            if( accessTokenResponse instanceof UserInfoTokenResponse ) {
                return ((UserInfoTokenResponse) accessTokenResponse).getUserInfo();
            }
            return null;
        }

        @Override
        public IdToken getIdToken() {
            if( accessTokenResponse instanceof OIDCTokenResponse ) {
                OIDCTokens oidcTokens = ((OIDCTokenResponse) accessTokenResponse).getOIDCTokens();
                if( oidcTokens != null && oidcTokens.getIDToken() != null ) {
                    new IdToken(oidcTokens.getIDToken());
                }
            }
            return null;
        }

        @Override
        public AccessToken getAccessToken() {
            return this.accessTokenResponse.getTokens().getAccessToken();
        }

        @Override
        public AuthorizationGrant getAuthorizationGrant() {
            return null;
        }
    }


    class AuthorizationResponse implements UserInfoRequest
    {

        private final AuthorizationSuccessResponse authorizationResponse;
        private final Map<String, Object> additionalParameters;

        public AuthorizationResponse(AuthorizationSuccessResponse authorizationResponse, Map<String, Object> additionalParameters) {
            this.authorizationResponse = authorizationResponse;
            this.additionalParameters = additionalParameters;
        }

        @Override
        public OidcUserInfo getUserInfo() {
            return null;
        }

        @Override
        public IdToken getIdToken() {
            if( authorizationResponse instanceof AuthenticationSuccessResponse ) {
                JWT idToken = ((AuthenticationSuccessResponse) authorizationResponse).getIDToken();
                if( idToken != null ) {
                    return new IdToken(idToken);
                }
            }
            return null;
        }

        @Override
        public AccessToken getAccessToken() {

            if( authorizationResponse instanceof AuthenticationSuccessResponse ) {
                return authorizationResponse.getAccessToken();
            }
            return null;
        }

        @Override
        public AuthorizationGrant getAuthorizationGrant() {
            if( authorizationResponse.getAuthorizationCode() != null ) {
                AuthorizationGrant grant = new AuthorizationCodeGrant(authorizationResponse.getAuthorizationCode(), null, null);
                if( this.additionalParameters != null ) {
                    grant = new AdditionalParamsAuthorizationGrant(grant, additionalParameters);
                }
                return grant;
            }
            return null;
        }
    }

}

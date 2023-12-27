package io.github.xc404.oauth.config;


import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;

/**
 * @Author chaox
 * @Date 12/20/2023 6:39 PM
 */
public interface OAuthProviderConfig
{
    /**
     * URL used to obtain an authorization grant
     */
    default String getAuthorizationUri() {
        return null;
    }

    /**
     * URL used to obtain an access token
     */
    default String getTokenUri() {
        return null;
    }

    /**
     * URL used to obtain an userinfo
     */
    default String getUserInfoUri() {
        return null;
    }

    default HTTPRequest.Method getUserInfoHttpMethod() {
        return HTTPRequest.Method.GET;
    }

    default String getJwkSetUri() {
        return null;
    }

    default long getJwkSetRefreshInterval() {
        return 3600000;
    }

    default String getIssuerUri() {
        return null;
    }

    default OAuthClient oauthClient(OAuthConfig oAuthConfig) {
        return null;
    }

    default UserInfoConvertor userInfoConvertor() {
        return null;
    }

    /**
     * Name of OAuth provider
     */
    String getProvider();

}

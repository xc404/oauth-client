package com.x404.oauth.config;


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

    default String getJwkSetUri() {
        return null;
    }

    default String getIssuerUri() {
        return null;
    }

    /**
     * Name of OAuth provider
     */
    String getProvider();
}

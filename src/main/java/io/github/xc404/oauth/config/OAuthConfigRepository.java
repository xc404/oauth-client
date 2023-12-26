package io.github.xc404.oauth.config;

/**
 * @Author chaox
 * @Date 12/22/2023 5:06 PM
 */
public interface OAuthConfigRepository
{
    OAuthConfig getOAuthConfig(String provider);
}

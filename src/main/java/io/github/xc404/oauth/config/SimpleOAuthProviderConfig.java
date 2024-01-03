package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;

import java.util.Optional;
import java.util.function.Function;

/**
 * @Author chaox
 * @Date 12/22/2023 5:36 PM
 */
public class SimpleOAuthProviderConfig implements OAuthProviderConfig
{
    private String provider;
    private String authorizationUri;
    private String accessTokenUri;
    private String userInfoUri;
    private String jwkSetUri;
    private String issuerUri;

    private HTTPRequest.Method userInfoHttpMethod;
    private Function<OAuthConfig,OAuthClient> oauthClient;
    private Long jwkSetRefreshInterval;
    private UserInfoConvertor userInfoConvertor;


    @Override
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    @Override
    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    @Override
    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    @Override
    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    @Override
    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    @Override
    public HTTPRequest.Method getUserInfoHttpMethod() {
        return Optional.ofNullable(this.userInfoHttpMethod).orElse(OAuthProviderConfig.super.getUserInfoHttpMethod());
    }

    @Override
    public long getJwkSetRefreshInterval() {
        return Optional.ofNullable(this.jwkSetRefreshInterval).orElse(OAuthProviderConfig.super.getJwkSetRefreshInterval());
    }

    @Override
    public OAuthClient oauthClient(OAuthConfig oAuthConfig) {
        return Optional.ofNullable(this.oauthClient).map(c -> c.apply(oAuthConfig))
                .orElse(OAuthProviderConfig.super.oauthClient(oAuthConfig));
    }

    @Override
    public UserInfoConvertor userInfoConvertor() {
        return Optional.ofNullable(this.userInfoConvertor).orElse(OAuthProviderConfig.super.userInfoConvertor());
    }


    public void setUserInfoHttpMethod(HTTPRequest.Method userInfoHttpMethod) {
        this.userInfoHttpMethod = userInfoHttpMethod;
    }


    public void setOauthClient(Function<OAuthConfig, OAuthClient> oauthClient) {
        this.oauthClient = oauthClient;
    }

    public void setJwkSetRefreshInterval(Long jwkSetRefreshInterval) {
        this.jwkSetRefreshInterval = jwkSetRefreshInterval;
    }


    public void setUserInfoConvertor(UserInfoConvertor userInfoConvertor) {
        this.userInfoConvertor = userInfoConvertor;
    }
}


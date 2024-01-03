package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.github.xc404.oauth.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Function;

/**
 * @Author chaox
 * @Date 1/2/2024 9:39 AM
 */
public final class ProviderConfigBuilder
{
    private String provider;
    private String authorizationUri;
    private String accessTokenUri;
    private String userInfoUri;
    private String jwkSetUri;
    private String issuerUri;
    private HTTPRequest.Method userInfoHttpMethod;
    private Function<OAuthConfig, OAuthClient> oauthClient;
    private Long jwkSetRefreshInterval;
    private UserInfoConvertor userInfoConvertor;

    private ProviderConfigBuilder(String provider) {
        this.provider = provider;
    }

    public static ProviderConfigBuilder provider(String provider) {
        return new ProviderConfigBuilder(provider);
    }

    public ProviderConfigBuilder authorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
        return this;
    }

    public ProviderConfigBuilder accessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
        return this;
    }

    public ProviderConfigBuilder userInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
        return this;
    }

    public ProviderConfigBuilder jwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
        return this;
    }

    public ProviderConfigBuilder issuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
        return this;
    }

    public ProviderConfigBuilder userInfoHttpMethod(HTTPRequest.Method userInfoHttpMethod) {
        this.userInfoHttpMethod = userInfoHttpMethod;
        return this;
    }

    public ProviderConfigBuilder oauthClient(Function<OAuthConfig, OAuthClient> oauthClient) {
        this.oauthClient = oauthClient;
        return this;
    }

    public ProviderConfigBuilder jwkSetRefreshInterval(Long jwkSetRefreshInterval) {
        this.jwkSetRefreshInterval = jwkSetRefreshInterval;
        return this;
    }

    public ProviderConfigBuilder userInfoConvertor(UserInfoConvertor userInfoConvertor) {
        this.userInfoConvertor = userInfoConvertor;
        return this;
    }

    public OAuthProviderConfig build() {
        Optional<CommonOAuthProvider> exist = Optional.ofNullable(EnumUtils.findEnumInsensitiveCase(CommonOAuthProvider.class, provider));
        SimpleOAuthProviderConfig providerConfig = new SimpleOAuthProviderConfig();


        boolean hasAllOAuth2Uri = StringUtils.isNotBlank(authorizationUri)
                && StringUtils.isNotBlank(accessTokenUri)
                && StringUtils.isNotBlank(userInfoUri);
        boolean hasAllOidcUri = StringUtils.isNotBlank(issuerUri);
        if( hasAllOidcUri || hasAllOAuth2Uri ) {
            providerConfig.setIssuerUri(issuerUri);
            providerConfig.setProvider(provider.toLowerCase());
            if(StringUtils.isBlank(issuerUri)){
                providerConfig.setAuthorizationUri(Optional.ofNullable(authorizationUri)
                        .orElse(exist.map(OAuthProviderConfig::getAuthorizationUri).orElse(null)));
                providerConfig.setAccessTokenUri(Optional.ofNullable(accessTokenUri)
                        .orElse(exist.map(OAuthProviderConfig::getAccessTokenUri).orElse(null)));
                providerConfig.setUserInfoUri(Optional.ofNullable(userInfoUri)
                        .orElse(exist.map(OAuthProviderConfig::getUserInfoUri).orElse(null)));
            }
            providerConfig.setOauthClient(Optional.ofNullable(this.oauthClient)
                    .orElse(exist.map(e -> {
                       return (Function<OAuthConfig, OAuthClient>) e::oauthClient;
                    }).orElse(null)));

            providerConfig.setUserInfoConvertor(Optional.ofNullable(userInfoConvertor)
                    .orElse(exist.map(OAuthProviderConfig::userInfoConvertor).orElse(null)));

            providerConfig.setUserInfoHttpMethod(Optional.ofNullable(userInfoHttpMethod)
                    .orElse(exist.map(OAuthProviderConfig::getUserInfoHttpMethod).orElse(null)));

            providerConfig.setJwkSetRefreshInterval(Optional.ofNullable(jwkSetRefreshInterval)
                    .orElse(exist.map(OAuthProviderConfig::getJwkSetRefreshInterval).orElse(null)));

            providerConfig.setJwkSetUri(Optional.ofNullable(jwkSetUri)
                    .orElse(exist.map(OAuthProviderConfig::getJwkSetUri).orElse(null)));

            return providerConfig;
        }
        return exist.orElseThrow(() -> new IllegalArgumentException(provider + "'s config error"));
    }
}

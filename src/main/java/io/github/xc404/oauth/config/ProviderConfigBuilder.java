package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.github.xc404.oauth.utils.EnumUtils;

/**
 * @Author chaox
 * @Date 12/22/2023 7:49 PM
 */
public final class ProviderConfigBuilder
{
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String provider;
    private String jwkSetUri;
    private String issuerUri;

    private ProviderConfigBuilder() {
    }

    public static ProviderConfigBuilder provider(String provider) {
        ProviderConfigBuilder providerConfigBuilder = new ProviderConfigBuilder();
        providerConfigBuilder.provider = provider;
        return providerConfigBuilder;
    }

    public ProviderConfigBuilder authorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
        return this;
    }

    public ProviderConfigBuilder tokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
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

    public OAuthProviderConfig build() {
        boolean allEmpty = StringUtils.isBlank(authorizationUri)
                && StringUtils.isBlank(tokenUri)
                && StringUtils.isBlank(userInfoUri)
                && StringUtils.isBlank(issuerUri)
                && StringUtils.isBlank(jwkSetUri);
        if( allEmpty ) {
            CommonOAuthProvider authProvider = EnumUtils.findEnumInsensitiveCase(CommonOAuthProvider.class, provider);
            if( authProvider == null ) {
                throw new IllegalArgumentException(provider + "'s config is not configured");
            }
            return authProvider;
        }

        boolean hasAllOAuth2Uri = StringUtils.isNotBlank(authorizationUri)
                && StringUtils.isNotBlank(tokenUri)
                && StringUtils.isNotBlank(userInfoUri);
        boolean hasAllOidcUri = StringUtils.isNotBlank(issuerUri) && StringUtils.isNotBlank(jwkSetUri);
        if( hasAllOidcUri || hasAllOAuth2Uri ) {
            SimpleOAuthProviderConfig providerConfig = new SimpleOAuthProviderConfig();
            providerConfig.setAuthorizationUri(authorizationUri);
            providerConfig.setTokenUri(tokenUri);
            providerConfig.setUserInfoUri(userInfoUri);
            providerConfig.setProvider(provider);
            providerConfig.setIssuerUri(issuerUri);
            providerConfig.setJwkSetUri(jwkSetUri);
            return providerConfig;
        }
        throw new IllegalArgumentException(provider + "'s config error");
    }
}

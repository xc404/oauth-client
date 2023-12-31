package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;

import java.io.IOException;

/**
 * @Author chaox
 * @Date 12/22/2023 5:50 PM
 */
public class AutoloadProviderConfig implements OAuthProviderConfig
{
    private final OAuthProviderConfig oAuthProviderConfig;

    private OIDCProviderMetadata oidcMetadata;

    public AutoloadProviderConfig(OAuthProviderConfig oAuthProviderConfig) {
        this.oAuthProviderConfig = oAuthProviderConfig;
    }

    @Override
    public String getAuthorizationUri() {
        String uri = this.oAuthProviderConfig.getAuthorizationUri();
        if( StringUtils.isBlank(uri) ) {
            ensureOIDCMetadataLoaded();
            uri = oidcMetadata.getAuthorizationEndpointURI().toString();
        }
        return uri;
    }

    @Override
    public String getAccessTokenUri() {
        String uri = this.oAuthProviderConfig.getAccessTokenUri();
        if( StringUtils.isBlank(uri) ) {
            ensureOIDCMetadataLoaded();
            uri = oidcMetadata.getTokenEndpointURI().toString();
        }
        return uri;
    }

    @Override
    public String getUserInfoUri() {
        String uri = this.oAuthProviderConfig.getUserInfoUri();
        if( StringUtils.isBlank(uri) ) {
            ensureOIDCMetadataLoaded();
            uri = oidcMetadata.getUserInfoEndpointURI().toString();
        }
        return uri;
    }

    @Override
    public String getProvider() {
        return this.oAuthProviderConfig.getProvider();
    }

    @Override
    public String getJwkSetUri() {
        return this.oAuthProviderConfig.getJwkSetUri();
    }

    @Override
    public String getIssuerUri() {
        return this.oAuthProviderConfig.getIssuerUri();
    }

    public synchronized void ensureOIDCMetadataLoaded() {
        if( this.oidcMetadata == null ) {
            if( this.getIssuerUri() == null ) {
                throw new IllegalArgumentException("issuer is empty");
            }
            try {
                this.oidcMetadata = OIDCProviderMetadata.resolve(new Issuer(this.getIssuerUri()));
            } catch( GeneralException | IOException e ) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public HTTPRequest.Method getUserInfoHttpMethod() {
        return oAuthProviderConfig.getUserInfoHttpMethod();
    }

    @Override
    public long getJwkSetRefreshInterval() {
        return oAuthProviderConfig.getJwkSetRefreshInterval();
    }

    @Override
    public OAuthClient oauthClient(OAuthConfig oAuthConfig) {
        return oAuthProviderConfig.oauthClient(oAuthConfig);
    }

    @Override
    public UserInfoConvertor userInfoConvertor() {
        return oAuthProviderConfig.userInfoConvertor();
    }
}

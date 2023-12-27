package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;

import java.util.Set;

/**
 * @Author chaox
 * @Date 12/20/2023 6:37 PM
 */
public class OAuthClientConfig
{
    public static final int OAUTH_TIMEOUT = 180000;
    /**
     * Name of OAuth provider
     */
    private String provider;
    /**
     * identifies client to service provider
     */
    private String clientId;

    /**
     * secret used to establish ownership of the client identifer
     */
    private String clientSecret;

    /**
     * URL to which the service provider will redirect the user after obtaining authorization
     */
    private String redirectUri;

    /**
     * The value MUST be one of "code" for requesting an
     * authorization code as described by Section 4.1.1 (<a href="https://tools.ietf.org/html/rfc6749#section-4.1.1" target="_blank">https://tools.ietf.org/html/rfc6749#section-4.1.1</a>),
     * "token" for requesting an access token (implicit grant) as described by Section 4.2.1 (<a href="https://tools.ietf.org/html/rfc6749#section-4.2.1" target="_blank">https://tools.ietf.org/html/rfc6749#section-4.2.1</a>),
     * or a registered extension value as described by Section 8.4 (<a href="https://tools.ietf.org/html/rfc6749#section-8.4" target="_blank">https://tools.ietf.org/html/rfc6749#section-8.4</a>).
     */
    private ResponseType responseType = ResponseType.CODE;

    /**
     * The optional value is: {@code authorization_code}, {@code password}, {@code client_credentials}
     */
    private GrantType grantType = GrantType.AUTHORIZATION_CODE;

    /**
     * The scope supported by the OAuth platform
     */
    private Set<String> scopes;

    /**
     * An opaque value used by the client to maintain
     * state between the request and callback.  The authorization
     * server includes this value when redirecting the user-agent back
     * to the client.
     */

    /**
     * The scope supported by the OAuth platform
     */
    private boolean enablePkce = true;

    /**
     * After the pkce enhancement protocol is enabled, the generation method of challenge code derived from
     * the code verifier sent in the authorization request is `s256` by default
     *
     * @see <a href="https://tools.ietf.org/html/rfc7636#section-4.3" target="_blank"> Client Sends the Code Challenge with the Authorization Request</a>
     */
    private CodeChallengeMethod codeChallengeMethod = CodeChallengeMethod.S256;


    /**
     * In pkce mode, the expiration time of codeverifier, in milliseconds, default is 3 minutes
     */
    private long timeout = OAUTH_TIMEOUT;

    public static Builder builder(String provider) {
        return new Builder(provider);
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public boolean isEnablePkce() {
        return enablePkce;
    }

    public void setEnablePkce(boolean enablePkce) {
        this.enablePkce = enablePkce;
    }

    public CodeChallengeMethod getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    public void setCodeChallengeMethod(CodeChallengeMethod codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public static final class Builder
    {
        private final String provider;
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private ResponseType responseType;
        private GrantType grantType;
        private Set<String> scopes;
        private boolean enablePkce;
        private CodeChallengeMethod codeChallengeMethod;
        private Long timeout;

        private Builder(String provider) {
            this.provider = provider;
        }


        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder withRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder withResponseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder withGrantType(GrantType grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder withScopes(Set<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder withEnablePkce(boolean enablePkce) {
            this.enablePkce = enablePkce;
            return this;
        }

        public Builder withCodeChallengeMethod(CodeChallengeMethod codeChallengeMethod) {
            this.codeChallengeMethod = codeChallengeMethod;
            return this;
        }

        public Builder withTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public OAuthClientConfig build() {
            OAuthClientConfig oAuth2ClientConfig = new OAuthClientConfig();
            oAuth2ClientConfig.setProvider(provider.toLowerCase());
            oAuth2ClientConfig.setClientId(clientId);
            oAuth2ClientConfig.setClientSecret(clientSecret);
            oAuth2ClientConfig.setRedirectUri(redirectUri);
            oAuth2ClientConfig.setResponseType(responseType);
            oAuth2ClientConfig.setGrantType(grantType);
            oAuth2ClientConfig.setScopes(scopes);
            oAuth2ClientConfig.setEnablePkce(enablePkce);
            oAuth2ClientConfig.setCodeChallengeMethod(codeChallengeMethod);
            if( this.timeout != null ) {
                oAuth2ClientConfig.setTimeout(timeout);
            }
            return oAuth2ClientConfig;
        }
    }
}

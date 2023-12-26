//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.xc404.oauth.springboot;

import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationProperties(
        prefix = "xc404.oauth"
)
public class OAuthClientProperties implements InitializingBean
{
    private final Map<String, OAuthClientProperty> clients = new HashMap<>();

    public OAuthClientProperties() {
    }

    public Map<String, OAuthClientProperty> getClients() {
        return clients;
    }

    public void afterPropertiesSet() {
        this.validate();
    }

    public void validate() {
        this.getClients().values().forEach(this::validateRegistration);
    }

    private void validateRegistration(OAuthClientProperty property) {
        if( !StringUtils.hasText(property.getClientId()) ) {
            throw new IllegalStateException("Client id must not be empty.");
        }
    }


    public static class OAuthClientProperty
    {
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
        private String responseType = ResponseType.CODE.toString();

        /**
         * The optional value is: {@code authorization_code}, {@code password}, {@code client_credentials}
         */
        private String grantType = GrantType.AUTHORIZATION_CODE.getValue();

        /**
         * The scope supported by the OAuth platform
         */
        private Set<String> scope;

        /**
         * An opaque value used by the client to maintain
         * state between the request and callback.  The authorization
         * server includes this value when redirecting the user-agent back
         * to the client.
         */

        /**
         * The scope supported by the OAuth platform
         */
        private boolean enablePkce;

        /**
         * After the pkce enhancement protocol is enabled, the generation method of challenge code derived from
         * the code verifier sent in the authorization request is `s256` by default
         *
         * @see <a href="https://tools.ietf.org/html/rfc7636#section-4.3" target="_blank"> Client Sends the Code Challenge with the Authorization Request</a>
         */
        private String codeChallengeMethod = CodeChallengeMethod.S256.toJSONString();


        /**
         * In pkce mode, the expiration time of codeverifier, in milliseconds, default is 3 minutes
         */
        private long timeout = 180000;

        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String jwkSetUri;
        private String issuerUri;

        public OAuthClientProperty() {
        }


        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return this.clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }


        public String getResponseType() {
            return responseType;
        }

        public void setResponseType(String responseType) {
            this.responseType = responseType;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }

        public boolean isEnablePkce() {
            return enablePkce;
        }

        public void setEnablePkce(boolean enablePkce) {
            this.enablePkce = enablePkce;
        }

        public String getCodeChallengeMethod() {
            return codeChallengeMethod;
        }

        public void setCodeChallengeMethod(String codeChallengeMethod) {
            this.codeChallengeMethod = codeChallengeMethod;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public String getRedirectUri() {
            return this.redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public Set<String> getScope() {
            return this.scope;
        }

        public void setScope(Set<String> scope) {
            this.scope = scope;
        }

        public String getAuthorizationUri() {
            return authorizationUri;
        }

        public void setAuthorizationUri(String authorizationUri) {
            this.authorizationUri = authorizationUri;
        }

        public String getTokenUri() {
            return tokenUri;
        }

        public void setTokenUri(String tokenUri) {
            this.tokenUri = tokenUri;
        }

        public String getUserInfoUri() {
            return userInfoUri;
        }

        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }

        public String getJwkSetUri() {
            return jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        public String getIssuerUri() {
            return issuerUri;
        }

        public void setIssuerUri(String issuerUri) {
            this.issuerUri = issuerUri;
        }
    }
}

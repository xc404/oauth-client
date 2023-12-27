/*
 * Copyright (c) 2020-2040, 北京符节科技有限公司 (support@fujieid.com & https://www.fujieid.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.xc404.oauth.config;

import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.provider.CommonOAuthProvider;

import java.util.Set;

/**
 * Configuration file of oauth2 module
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 */
public class OAuthConfig
{

    private final OAuthClientConfig clientConfig;

    private final OAuthProviderConfig providerConfig;


    public OAuthConfig(OAuthClientConfig clientConfig) {
        this(clientConfig, CommonOAuthProvider.valueOf(clientConfig.getProvider().toUpperCase()));
    }

    public OAuthConfig(OAuthClientConfig clientConfig, OAuthProviderConfig providerConfig) {
        this.clientConfig = clientConfig;
        this.providerConfig = new AutoloadProviderConfig(providerConfig);
    }

    public String getProvider() {
        return clientConfig.getProvider();
    }

    public String getClientId() {
        return clientConfig.getClientId();
    }

    public String getClientSecret() {
        return clientConfig.getClientSecret();
    }

    public String getRedirectUri() {
        return clientConfig.getRedirectUri();
    }


    public Set<String> getScopes() {
        return clientConfig.getScopes();
    }

    public ResponseType getResponseType() {
        return clientConfig.getResponseType();
    }

    public GrantType getGrantType() {
        return clientConfig.getGrantType();
    }

    public long getTimeout() {
        return clientConfig.getTimeout();
    }

    public boolean isEnablePkce() {
        return clientConfig.isEnablePkce();
    }

    public CodeChallengeMethod getCodeChallengeMethod() {
        return clientConfig.getCodeChallengeMethod();
    }


    public String getAuthorizationUri() {
        return providerConfig.getAuthorizationUri();
    }

    public String getTokenUri() {
        return providerConfig.getTokenUri();
    }

    public String getUserInfoUri() {
        return providerConfig.getUserInfoUri();
    }

    public String getJwkSetUri() {
        return providerConfig.getJwkSetUri();
    }

    public String getIssuerUri() {
        return providerConfig.getIssuerUri();
    }

    public long getJwkSetRefreshInterval() {
        return providerConfig.getJwkSetRefreshInterval();
    }


    public HTTPRequest.Method getUserInfoHttpMethod() {
        return providerConfig.getUserInfoHttpMethod();
    }

    public OAuthClient oauthClient(OAuthConfig oAuthConfig) {
        return providerConfig.oauthClient(oAuthConfig);
    }

    public UserInfoConvertor userInfoConvertor() {
        return providerConfig.userInfoConvertor();
    }
}

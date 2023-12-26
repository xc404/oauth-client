/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xc404.oauth.springboot;

import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import io.github.xc404.oauth.config.ConfigurationException;
import io.github.xc404.oauth.config.OAuthClientConfig;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthProviderConfig;
import io.github.xc404.oauth.config.ProviderConfigBuilder;

import java.util.HashMap;
import java.util.Map;


public final class OAuthClientPropertiesConfigAdapter
{

    private OAuthClientPropertiesConfigAdapter() {
    }

    public static Map<String, OAuthConfig> getClientConfigs(OAuthClientProperties properties) {
        Map<String, OAuthConfig> configMap = new HashMap<>();
        properties.getClients().forEach((key, value) -> configMap.put(key,
                getClientAuthConfig(key, value)));
        return configMap;
    }

    private static OAuthConfig getClientAuthConfig(String provider,
                                                   OAuthClientProperties.OAuthClientProperty property) {
        OAuthClientConfig clientConfig = buildClientConfig(provider, property);
        OAuthProviderConfig providerConfig = buildProviderConfig(provider, property);
        return new OAuthConfig(clientConfig, providerConfig);
    }

    private static OAuthProviderConfig buildProviderConfig(String provider, OAuthClientProperties.OAuthClientProperty property) {
        ProviderConfigBuilder builder = ProviderConfigBuilder.provider(provider);
        return builder.issuerUri(property.getIssuerUri())
                .jwkSetUri(property.getJwkSetUri())
                .authorizationUri(property.getAuthorizationUri())
                .userInfoUri(property.getUserInfoUri())
                .tokenUri(property.getTokenUri())
                .build();
    }


    private static OAuthClientConfig buildClientConfig(String provider, OAuthClientProperties.OAuthClientProperty property) {
        OAuthClientConfig.Builder builder = OAuthClientConfig.builder();
        builder.withProvider(provider);
        builder.withClientId(property.getClientId());
        builder.withClientSecret(property.getClientSecret());
        builder.withEnablePkce(property.isEnablePkce());
        try {
            builder.withGrantType(GrantType.parse(property.getGrantType()));
        } catch( ParseException e ) {
            throw new ConfigurationException(e, "grantType");
        }
        builder.withTimeout(property.getTimeout());
        builder.withScopes(property.getScope());
        builder.withRedirectUri(property.getRedirectUri());
        builder.withCodeChallengeMethod(CodeChallengeMethod.parse(property.getCodeChallengeMethod()));
        try {
            builder.withResponseType(ResponseType.parse(property.getResponseType()));
        } catch( ParseException e ) {
            throw new ConfigurationException(e, "responseType");
        }
        return builder.build();
    }


}

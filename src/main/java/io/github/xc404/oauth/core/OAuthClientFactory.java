package io.github.xc404.oauth.core;

import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthConfigRepository;
import io.github.xc404.oauth.oidc.UserInfoConvertor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chaox
 * @Date 12/22/2023 5:06 PM
 */
public class OAuthClientFactory
{
    private final OAuthConfigRepository oAuthConfigRepository;
    private final Map<String, OAuthClient> clients = new ConcurrentHashMap<>();

    public OAuthClientFactory(OAuthConfigRepository oAuthConfigRepository) {
        this.oAuthConfigRepository = oAuthConfigRepository;
    }

    public OAuthClient getOAuthClient(String provider) {
        return clients.computeIfAbsent(provider.toLowerCase(), key -> {
            OAuthConfig oAuthConfig = getoAuthConfig(key);
            OAuthClient oAuthClient = oAuthConfig.oauthClient(oAuthConfig);
            if( oAuthClient == null ) {
                oAuthClient = new DefaultOAuthClient(oAuthConfig);
            }

            UserInfoConvertor userInfoConvertor = oAuthConfig.userInfoConvertor();
            if( userInfoConvertor != null && oAuthClient instanceof DefaultOAuthClient ) {
                ((DefaultOAuthClient) oAuthClient).setUserInfoConvertor(userInfoConvertor);
            }
            return oAuthClient;
        });

    }

    protected OAuthConfig getoAuthConfig(String provider) {
        return this.oAuthConfigRepository.getOAuthConfig(provider.toLowerCase());
    }


}

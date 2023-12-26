package io.github.xc404.oauth.core;

import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthConfigRepository;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chaox
 * @Date 12/22/2023 5:06 PM
 */
public class OAuthClientFactory
{
    private final OAuthConfigRepository oAuthConfigRepository;
    private final ConcurrentHashMap<String, OAuthClient> clients = new ConcurrentHashMap<>();

    public OAuthClientFactory(OAuthConfigRepository oAuthConfigRepository) {
        this.oAuthConfigRepository = oAuthConfigRepository;
    }

    public OAuthClient getOAuthClient(String provider) {
        return clients.computeIfAbsent(provider, key -> {
            OAuthConfig oAuthConfig = getoAuthConfig(key);
            return new DefaultOAuthClient(oAuthConfig);
        });

    }

    protected OAuthConfig getoAuthConfig(String provider) {
        return this.oAuthConfigRepository.getOAuthConfig(provider);
    }


}

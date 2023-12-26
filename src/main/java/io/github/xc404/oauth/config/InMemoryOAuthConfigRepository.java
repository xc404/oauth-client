package io.github.xc404.oauth.config;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chaox
 * @Date 12/22/2023 7:07 PM
 */
public class InMemoryOAuthConfigRepository implements OAuthConfigRepository
{
    private final Map<String, OAuthConfig> oAuthConfigs;

    /**
     * Constructs an {@code InMemoryOAuthConfigRepository} using the provided
     * parameters.
     *
     * @param oAuthConfigs the client registration(s)
     */
    public InMemoryOAuthConfigRepository(OAuthConfig... oAuthConfigs) {
        this(Arrays.asList(oAuthConfigs));
    }

    /**
     * Constructs an {@code InMemoryOAuthConfigRepository} using the provided
     * parameters.
     *
     * @param oAuthConfigs the client registration(s)
     */
    public InMemoryOAuthConfigRepository(List<OAuthConfig> oAuthConfigs) {
        this(createRegistrationsMap(oAuthConfigs));
    }

    /**
     * Constructs an {@code InMemoryOAuthConfigRepository} using the provided
     * {@code Map} of {@link OAuthConfig#getProvider() provider } to
     * {@link OAuthConfig}.
     *
     * @param configs the {@code Map} of client registration(s)
     * @since 5.2
     */
    public InMemoryOAuthConfigRepository(Map<String, OAuthConfig> configs) {
        this.oAuthConfigs = configs;
    }

    private static Map<String, OAuthConfig> createRegistrationsMap(List<OAuthConfig> oAuthConfigs) {
        return toUnmodifiableConcurrentMap(oAuthConfigs);
    }

    private static Map<String, OAuthConfig> toUnmodifiableConcurrentMap(List<OAuthConfig> oAuthConfigs) {
        ConcurrentHashMap<String, OAuthConfig> result = new ConcurrentHashMap<>();
        for( OAuthConfig registration : oAuthConfigs ) {
            result.put(registration.getProvider().toLowerCase(), registration);
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public OAuthConfig getOAuthConfig(String provider) {
        return this.oAuthConfigs.get(provider.toLowerCase());
    }
}

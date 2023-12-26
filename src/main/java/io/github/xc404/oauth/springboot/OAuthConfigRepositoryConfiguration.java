package io.github.xc404.oauth.springboot;

import io.github.xc404.oauth.config.InMemoryOAuthConfigRepository;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthConfigRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties({OAuthClientProperties.class})
@Conditional({OAuthClientsConfiguredCondition.class})
public class OAuthConfigRepositoryConfiguration
{
    OAuthConfigRepositoryConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({OAuthConfigRepository.class})
    InMemoryOAuthConfigRepository oauthConfigRepository(OAuthClientProperties properties) {
        List<OAuthConfig> oAuthConfigs = new ArrayList(OAuthClientPropertiesConfigAdapter.getClientConfigs(properties).values());
        return new InMemoryOAuthConfigRepository(oAuthConfigs);
    }
}
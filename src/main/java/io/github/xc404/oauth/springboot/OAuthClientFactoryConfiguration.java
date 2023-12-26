package io.github.xc404.oauth.springboot;

import io.github.xc404.oauth.config.OAuthConfigRepository;
import io.github.xc404.oauth.core.OAuthClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(
        proxyBeanMethods = false
)
@Import(OAuthConfigRepositoryConfiguration.class)
public class OAuthClientFactoryConfiguration
{
    OAuthClientFactoryConfiguration() {
    }

    @Bean
    @ConditionalOnClass(OAuthConfigRepository.class)
    @ConditionalOnMissingBean({OAuthClientFactory.class})
    OAuthClientFactory oauthClientFactory(OAuthConfigRepository authConfigRepository) {

        return new OAuthClientFactory(authConfigRepository);
    }
}
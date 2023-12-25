package com.x404.oauth.springboot;

import com.x404.oauth.config.OAuthConfigRepository;
import com.x404.oauth.core.OAuthClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
class OAuthClientFactoryConfiguration
{
    OAuthClientFactoryConfiguration() {
    }

    @Bean
    @ConditionalOnClass(OAuthConfigRepository.class)
    @ConditionalOnMissingBean({OAuthClientFactory.class})
    OAuthClientFactory OAuthConfigRepository(OAuthConfigRepository authConfigRepository) {

        return new OAuthClientFactory(authConfigRepository);
    }
}
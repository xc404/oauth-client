package io.github.xc404.oauth.springboot;

import io.github.xc404.oauth.config.OAuthConfigRepository;
import io.github.xc404.oauth.core.OAuthClientFactory;
import io.github.xc404.oauth.core.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(
        proxyBeanMethods = false
)
@Import({OAuthConfigRepositoryConfiguration.class, OAuthController.class})
public class OAuthClientConfiguration
{

    @Value("${xc404.oauth.allowUnknownStateAuthorization:false}")
    private boolean allowUnknownStateAuthorization;

    OAuthClientConfiguration() {
    }

    @Bean
    @ConditionalOnClass(OAuthConfigRepository.class)
    @ConditionalOnMissingBean({OAuthClientFactory.class})
    OAuthClientFactory oauthClientFactory(OAuthConfigRepository authConfigRepository) {

        return new OAuthClientFactory(authConfigRepository);
    }

    @ConditionalOnClass(OAuthClientFactory.class)
    @Bean
    OAuthService oauthService(OAuthClientFactory oAuthClientFactory) {
        OAuthService oAuthService = new OAuthService(oAuthClientFactory);
        oAuthService.setAllowUnknownStateAuthorization(this.allowUnknownStateAuthorization);
        return oAuthService;
    }


}
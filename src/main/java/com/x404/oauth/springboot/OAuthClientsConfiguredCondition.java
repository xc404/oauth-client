package com.x404.oauth.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuthClientsConfiguredCondition extends SpringBootCondition
{

    private static final Bindable<Map<String, OAuthClientProperties.OAuthClientProperty>> STRING_REGISTRATION_MAP = Bindable
            .mapOf(String.class, OAuthClientProperties.OAuthClientProperty.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth2 Clients Configured Condition");
        Map<String, OAuthClientProperties.OAuthClientProperty> registrations = getRegistrations(context.getEnvironment());
        if( !registrations.isEmpty() ) {
            return ConditionOutcome.match(message.foundExactly("registered clients " + registrations.values().stream()
                    .map(OAuthClientProperties.OAuthClientProperty::getClientId).collect(Collectors.joining(", "))));
        }
        return ConditionOutcome.noMatch(message.notAvailable("registered clients"));
    }

    private Map<String, OAuthClientProperties.OAuthClientProperty> getRegistrations(Environment environment) {
        return Binder.get(environment).bind("x404.oauth.clients", STRING_REGISTRATION_MAP)
                .orElse(Collections.emptyMap());
    }

}
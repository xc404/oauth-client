package com.x404.oauth.context;

import com.nimbusds.oauth2.sdk.id.State;

import java.time.Duration;

/**
 * @Author chaox
 * @Date 12/24/2023 4:22 PM
 */
public interface OAuthContextProvider
{

    OAuthContext getOAuthContext(State state);

    OAuthContext createOAuthContext(Duration ttl);
}

package com.x404.oauth.core;

import java.net.URI;

/**
 * @Author chaox
 * @Date 12/25/2023 4:34 PM
 */
public class AuthorizationResult
{
    private final LoginToken loginToken;
    private final URI redirectURI;

    public AuthorizationResult(URI redirectURI, LoginToken loginToken) {
        this.loginToken = loginToken;
        this.redirectURI = redirectURI;
    }

    public URI getRedirectURI() {
        return redirectURI;
    }

    public LoginToken getLoginToken() {
        return loginToken;
    }
}

package io.github.xc404.oauth.auth;

import com.nimbusds.oauth2.sdk.id.Identifier;

/**
 * @Author chaox
 * @Date 12/25/2023 6:48 PM
 */
public class SessionToken extends Identifier
{
    public SessionToken(String value) {
        super(value);
    }

    public SessionToken(int byteLength) {
        super(byteLength);
    }

    public SessionToken() {
    }
}


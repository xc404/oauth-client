package io.github.xc404.oauth.core;

import com.nimbusds.oauth2.sdk.id.Identifier;
import com.nimbusds.oauth2.sdk.id.State;

/**
 * @Author chaox
 * @Date 12/25/2023 3:19 PM
 */
public class LoginToken extends Identifier
{
    public LoginToken(String value) {
        super(value);
    }

    public LoginToken(int byteLength) {
        super(byteLength);
    }

    public LoginToken() {
        super();
    }

    public static LoginToken fromState(State state) {
        return new LoginToken(state.getValue());
    }

    public State toState() {
        return new State(this.getValue());
    }

}

package io.github.xc404.oauth.context;

import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.openid.connect.sdk.Nonce;

import java.time.Duration;

/**
 * @Author chaox
 * @Date 12/24/2023 9:03 AM
 */
public interface OAuthContext
{
    Duration DEFAULT_SESSION_TTL = Duration.ofMinutes(15);

    static OAuthContext empty() {
        return new AbstractOAuthContext(new State())
        {
            @Override
            public void clear() {

            }
        };
    }

    static OAuthContext create() {
        return DefaultOAuthContextProvider.getInstance().createOAuthContext(null);
    }

    static OAuthContext get(State state) {
        return DefaultOAuthContextProvider.getInstance().getOAuthContext(state);
    }

    OAuthStep getStep();

    void setStep(OAuthStep oAuthStep);

    void setContext(String key, Object value);

    Object getContext(String key);

    State getState();


    CodeVerifier getCodeVerifier();

    Nonce getNonce();

    void clear();

    void setCodeVerifier(CodeVerifier codeVerifier);

    void setNonce(Nonce nonce);

    enum OAuthStep
    {
        AUTHORIZATION,
        AUTHENTICATE
    }
}

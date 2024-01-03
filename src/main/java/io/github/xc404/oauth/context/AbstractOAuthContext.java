package io.github.xc404.oauth.context;

import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.openid.connect.sdk.Nonce;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chaox
 * @Date 12/24/2023 8:02 PM
 */
public abstract class AbstractOAuthContext implements OAuthContext
{
    private final Map<String, Object> contextCache = new ConcurrentHashMap<>();
    private final State state;
    private OAuthStep oAuthStep;
    private CodeVerifier codeVerifier;

    private Nonce nonce;

    protected AbstractOAuthContext(State state) {
        this.state = state;
    }

    @Override
    public OAuthStep getStep() {
        return this.oAuthStep;
    }

    @Override
    public void setStep(OAuthStep oAuthStep) {
        if( oAuthStep == OAuthStep.AUTHORIZATION && this.oAuthStep == OAuthStep.AUTHENTICATE ) {
            throw new IllegalStateException("state can not rollback");
        }
        this.oAuthStep = oAuthStep;
    }

    @Override
    public void setContext(String key, Object value) {
        this.contextCache.put(key, value);
    }

    @Override
    public Object getContext(String key) {
        return this.contextCache.get(key);
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public CodeVerifier getCodeVerifier() {
        return this.codeVerifier;
    }

    @Override
    public Nonce getNonce() {
        return this.nonce;
    }

    @Override
    public void setCodeVerifier(CodeVerifier codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    @Override
    public void setNonce(Nonce nonce) {
        this.nonce = nonce;
    }
}

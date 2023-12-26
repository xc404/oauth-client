package io.github.xc404.oauth.context;

import com.nimbusds.oauth2.sdk.id.State;
import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.io.Serializable;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import static io.github.xc404.oauth.context.OAuthContext.DEFAULT_SESSION_TTL;

/**
 * @Author chaox
 * @Date 12/24/2023 8:14 PM
 */
public class DefaultOAuthContextProvider implements OAuthContextProvider
{

    private static DefaultOAuthContextProvider instance;
    private final Timer timer;
    private final PassiveExpiringMap<State, CachedObject<OAuthContext>> contextMap = new PassiveExpiringMap<>();

    private DefaultOAuthContextProvider() {
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run() {
                contextMap.isEmpty();
            }
        }, DEFAULT_SESSION_TTL.toMillis(), DEFAULT_SESSION_TTL.toMillis());
    }

    public synchronized static OAuthContextProvider getInstance() {
        if( instance == null ) {
            instance = new DefaultOAuthContextProvider();
        }
        return instance;
    }


    @Override
    public OAuthContext getOAuthContext(State state) {
        CachedObject<OAuthContext> cachedObject = this.contextMap.get(state);
        if( cachedObject == null ) {
            return null;
        }
        cachedObject.extendExpiration();
        return cachedObject.getData();
    }

    @Override
    public OAuthContext createOAuthContext(Duration ttl) {
        if( ttl == null ) {
            ttl = DEFAULT_SESSION_TTL;
        }
        State state = new State();
        OAuthContext oAuthContext = new SimpleOAuthContext(state);
        CachedObject<OAuthContext> cachedObject = new CachedObject<>(oAuthContext, ttl.toMillis());
        this.contextMap.put(state, cachedObject);
        return oAuthContext;
    }

    private static class CachedObject<T> implements Serializable
    {
        private final T data;
        private final long ttl;
        private long lastAccessTime;

        CachedObject(T data, long ttl) {
            this.data = data;
            this.lastAccessTime = System.currentTimeMillis();
            // The actual expiration time is equal to the current time plus the validity period
            this.ttl = ttl;
        }

        boolean isExpired() {
            if( ttl > 0 ) {
                return System.currentTimeMillis() > lastAccessTime + ttl;
            }
            return false;
        }

        public T getData() {
            if( isExpired() ) {
                return null;
            }
            return data;
        }


        public void extendExpiration() {
            this.lastAccessTime = System.currentTimeMillis();
        }
    }

    public static class ExpirationPolicy implements PassiveExpiringMap.ExpirationPolicy<String, com.nimbusds.jose.util.cache.CachedObject<OAuthContext>>
    {


        @Override
        public long expirationTime(String key, com.nimbusds.jose.util.cache.CachedObject<OAuthContext> value) {
            return value.getExpirationTime();
        }
    }

    class SimpleOAuthContext extends AbstractOAuthContext
    {

        protected SimpleOAuthContext(State state) {
            super(state);
        }

        @Override
        public void clear() {
            contextMap.remove(this.getState());
        }
    }

}

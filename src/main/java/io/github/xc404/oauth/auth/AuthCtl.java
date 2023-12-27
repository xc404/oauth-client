package io.github.xc404.oauth.auth;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import io.github.xc404.oauth.core.AuthorizationResult;
import io.github.xc404.oauth.core.ClientUserInfo;
import io.github.xc404.oauth.core.LoginToken;
import io.github.xc404.oauth.core.OAuthService;
import io.github.xc404.oauth.utils.UrlUtils;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/25/2023 6:31 PM
 */
public class AuthCtl
{
    private final OAuthService oAuthService;
    private final UserService userService;
    private final LoginService loginService;
    private boolean loginAfterAuthorization;

    public AuthCtl(OAuthService oAuthService, UserService userService, LoginService loginService) {
        this(oAuthService, userService, loginService, false);
    }

    public AuthCtl(OAuthService oAuthService, UserService userService, LoginService loginService, boolean loginAfterAuthorization) {
        this.oAuthService = oAuthService;
        this.userService = userService;
        this.loginService = loginService;
        this.loginAfterAuthorization = loginAfterAuthorization;
    }

    public URI requestAuthorization(String provider, String redirectUrl, String state) {
        return oAuthService.requestAuthorization(provider, redirectUrl, state);
    }

    public URI authorizationCallback(URI requestUrl) {
        return this.authorizationCallback(null, requestUrl);
    }

    public URI authorizationCallback(String provider, URI requestUrl) {

        AuthorizationResult authorizationResult = oAuthService.authorizationCallback(provider, requestUrl);
        URI uri = authorizationResult.getRedirectURI();
        LoginToken loginToken = authorizationResult.getLoginToken();
        if( loginToken != null ) {
            if( this.loginAfterAuthorization ) {
                ClientUserInfo userInfo = this.oAuthService.loginWithLoginToken(loginToken);
                SessionToken sessionToken = login(userInfo);
                Map<String, String> params = Collections.singletonMap("session_token", sessionToken.getValue());
                uri = URI.create(UrlUtils.appendQuery(uri.toString(), params));
            } else {
                Map<String, String> params = Collections.singletonMap("login_token", loginToken.getValue());
                uri = URI.create(UrlUtils.appendQuery(uri.toString(), params));
            }
        }
        return uri;
    }

    public SessionToken login(String provider, AuthorizationGrant authorizationGrant) {
        ClientUserInfo userInfo = oAuthService.authenticate(provider, authorizationGrant);
        return login(userInfo);
    }


    protected SessionToken login(ClientUserInfo userInfo) {
        IUser user = this.userService.getOrCreateUser(userInfo);
        return this.loginService.login(user);
    }

    public boolean isLoginAfterAuthorization() {
        return loginAfterAuthorization;
    }

    public void setLoginAfterAuthorization(boolean loginAfterAuthorization) {
        this.loginAfterAuthorization = loginAfterAuthorization;
    }
}

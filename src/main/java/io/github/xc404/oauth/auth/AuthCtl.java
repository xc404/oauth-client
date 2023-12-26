package io.github.xc404.oauth.auth;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import io.github.xc404.oauth.core.AuthorizationResult;
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
    private final boolean loginWhenAuthorization;


    public AuthCtl(OAuthService oAuthService, UserService userService, LoginService loginService, boolean loginWhenAuthorization) {
        this.oAuthService = oAuthService;
        this.userService = userService;
        this.loginService = loginService;
        this.loginWhenAuthorization = loginWhenAuthorization;
    }

    public URI requestAuthorization(String provider, String redirectUrl, String state) {
        return oAuthService.requestAuthorization(provider, redirectUrl, state);
    }

    public URI authorizationCallback(URI requestUrl) {

        AuthorizationResult authorizationResult = oAuthService.authorizationCallback(requestUrl);
        URI uri = authorizationResult.getRedirectURI();
        LoginToken loginToken = authorizationResult.getLoginToken();
        if( loginToken != null ) {
            if( this.loginWhenAuthorization ) {
                UserInfo userInfo = this.oAuthService.loginWithLoginToken(loginToken);
                SessionToken sessionToken = login(userInfo);
                Map<String, String> params = Collections.singletonMap("session_token", sessionToken.getValue());
                uri = UrlUtils.toURI(UrlUtils.appendQuery(uri.toString(), params));
            } else {
                Map<String, String> params = Collections.singletonMap("login_token", loginToken.getValue());
                uri = UrlUtils.toURI(UrlUtils.appendQuery(uri.toString(), params));
            }
        }
        return uri;
    }

    public SessionToken login(String provider, AuthorizationGrant authorizationGrant) {
        UserInfo authenticate = oAuthService.authenticate(provider, authorizationGrant);
        return login(authenticate);
    }


    protected SessionToken login(UserInfo userInfo) {
        IUser user = this.userService.getOrCreateUser(userInfo);
        return this.loginService.login(user);
    }

}

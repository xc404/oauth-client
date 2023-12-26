package io.github.xc404.oauth.springboot;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import io.github.xc404.oauth.auth.AuthCtl;
import io.github.xc404.oauth.auth.LoginService;
import io.github.xc404.oauth.auth.SessionToken;
import io.github.xc404.oauth.auth.UserService;
import io.github.xc404.oauth.core.OAuthService;
import io.github.xc404.oauth.utils.OAuthParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/22/2023 9:46 PM
 */
@Controller
public class OAuthController extends AuthCtl
{
    public OAuthController(OAuthService oAuthService, UserService userService, LoginService loginService, boolean loginWhenAuthorization) {
        super(oAuthService, userService, loginService, loginWhenAuthorization);
    }

    @GetMapping("{provider}/request/oauth")
    public RedirectView requestAuthorizationRedirect(@PathVariable("provider") String provider, @RequestParam("redirect_uri") String redirectUri, @RequestParam("state") String state) {
        return new RedirectView(super.requestAuthorization(provider, redirectUri, state).toString());
    }

    @GetMapping("{provider}/complete/oauth")
    public RedirectView authorizationCallbackRedirect(@PathVariable("provider") String provider, UriComponentsBuilder uriComponentsBuilder) {
        return new RedirectView(super.authorizationCallback(uriComponentsBuilder.build().toUri()).toString());
    }

    @PostMapping("{provider}/login")
    @ResponseBody
    public SessionTokenResponse login(@PathVariable("provider") String provider, @RequestParam Map<String, List<String>> params) {
        AuthorizationGrant grant = OAuthParser.parseAuthorizationGrant(params);
        SessionToken sessionToken = super.login(provider, grant);
        return new SessionTokenResponse(sessionToken);
    }

    public static class SessionTokenResponse
    {
        private final String sessionToken;

        public SessionTokenResponse(SessionToken sessionToken) {
            this.sessionToken = sessionToken.getValue();
        }

        public String getSessionToken() {
            return sessionToken;
        }
    }

}

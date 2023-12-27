package io.github.xc404.oauth.springboot;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import io.github.xc404.oauth.auth.AuthCtl;
import io.github.xc404.oauth.auth.LoginService;
import io.github.xc404.oauth.auth.SessionToken;
import io.github.xc404.oauth.auth.UserService;
import io.github.xc404.oauth.core.OAuthService;
import io.github.xc404.oauth.utils.OAuthParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/22/2023 9:46 PM
 */
@Controller
@ConditionalOnProperty(
        prefix = "xc404.oauth.server", name = "enabled", havingValue = "true"
)
public class OAuthController extends AuthCtl
{

    public OAuthController(OAuthService oAuthService, UserService userService, LoginService loginService) {
        super(oAuthService, userService, loginService);
    }

    @GetMapping("oauth/{provider}/request")
    public RedirectView requestAuthorizationRedirect(@PathVariable("provider") String provider, @RequestParam(value = "redirect_uri", required = false) String redirectUri, @RequestParam(value = "state", required = false) String state) {
        return new RedirectView(super.requestAuthorization(provider, redirectUri, state).toString());
    }

    @GetMapping("oauth/{provider}/complete")
    public RedirectView authorizationCallbackRedirect(@PathVariable("provider") String provider) {
        return new RedirectView(super.authorizationCallback(provider, ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).toString());
    }

    @PostMapping("oauth/{provider}/login")
    @ResponseBody
    public SessionTokenResponse login(@PathVariable("provider") String provider, @RequestParam Map<String, List<String>> params) {
        AuthorizationGrant grant = OAuthParser.parseAuthorizationGrant(params);
        SessionToken sessionToken = super.login(provider, grant);
        return new SessionTokenResponse(sessionToken);
    }

    @Value("${xc404.oauth.server.loginAfterAuthorization:false}")
    @Override
    public void setLoginAfterAuthorization(boolean loginAfterAuthorization) {
        super.setLoginAfterAuthorization(loginAfterAuthorization);
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

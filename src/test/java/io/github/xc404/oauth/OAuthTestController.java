package io.github.xc404.oauth;

import io.github.xc404.oauth.auth.AuthCtl;
import io.github.xc404.oauth.auth.LoginService;
import io.github.xc404.oauth.auth.UserService;
import io.github.xc404.oauth.core.OAuthService;
import io.github.xc404.oauth.springboot.OAuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

/**
 * @Author chaox
 * @Date 12/27/2023 4:32 PM
 */
@Controller
public class OAuthTestController extends AuthCtl
{

    public OAuthTestController(@Autowired OAuthService oAuthService, UserService userService, LoginService loginService) {
        super(oAuthService, userService, loginService);
        this.setLoginAfterAuthorization(true);
    }


    @GetMapping("sys/authentication/external/redirect")
    public RedirectView authorizationCallback() {
        return new RedirectView(super.authorizationCallback("wechat",ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).toString());

    }
}

package io.github.xc404.oauth.satoken;

import cn.dev33.satoken.stp.StpUtil;
import io.github.xc404.oauth.auth.IUser;
import io.github.xc404.oauth.auth.LoginService;
import io.github.xc404.oauth.auth.SessionToken;

/**
 * @Author chaox
 * @Date 12/25/2023 7:39 PM
 */
public class SaTokenLoginService implements LoginService
{

    @Override
    public SessionToken login(IUser user) {
        StpUtil.login(user.getId());
        return new SessionToken(StpUtil.getTokenInfo().getTokenValue());
    }
}

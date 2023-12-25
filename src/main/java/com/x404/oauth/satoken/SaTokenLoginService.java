package com.x404.oauth.satoken;

import cn.dev33.satoken.stp.StpUtil;
import com.x404.oauth.auth.IUser;
import com.x404.oauth.auth.LoginService;
import com.x404.oauth.auth.SessionToken;

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

package io.github.xc404.oauth.core;

import io.github.xc404.oauth.auth.IUser;
import io.github.xc404.oauth.auth.SessionToken;
import org.springframework.stereotype.Service;

/**
 * @Author chaox
 * @Date 12/26/2023 6:04 PM
 */
@Service
public class LoginService implements io.github.xc404.oauth.auth.LoginService
{
    @Override
    public SessionToken login(IUser user) {
        return new SessionToken(user.getId().toString());
    }
}

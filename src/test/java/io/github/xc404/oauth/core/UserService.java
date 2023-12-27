package io.github.xc404.oauth.core;

import io.github.xc404.oauth.auth.IUser;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Author chaox
 * @Date 12/26/2023 6:03 PM
 */
@Service
public class UserService implements io.github.xc404.oauth.auth.UserService
{
    @Override
    public IUser getOrCreateUser(ClientUserInfo userInfo) {
        return new IUser()
        {
            @Override
            public Serializable getId() {
                return 0000;
            }
        };
    }
}

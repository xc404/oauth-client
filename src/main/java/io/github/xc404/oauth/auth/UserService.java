package io.github.xc404.oauth.auth;

import io.github.xc404.oauth.core.ClientUserInfo;

/**
 * @Author chaox
 * @Date 12/24/2023 9:31 PM
 */
public interface UserService
{
    IUser getOrCreateUser(ClientUserInfo userInfo);
}

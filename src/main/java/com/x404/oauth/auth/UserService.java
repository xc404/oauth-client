package com.x404.oauth.auth;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

/**
 * @Author chaox
 * @Date 12/24/2023 9:31 PM
 */
public interface UserService
{
    IUser getOrCreateUser(UserInfo userInfo);
}

package com.x404.oauth.auth;

/**
 * @Author chaox
 * @Date 12/25/2023 8:05 PM
 */
public interface LoginService
{
    SessionToken login(IUser user);
}

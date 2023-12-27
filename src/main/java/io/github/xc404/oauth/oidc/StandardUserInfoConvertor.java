package io.github.xc404.oauth.oidc;

import net.minidev.json.JSONObject;

/**
 * @Author chaox
 * @Date 12/27/2023 8:33 AM
 */
public class StandardUserInfoConvertor implements UserInfoConvertor
{
    @Override
    public OidcUserInfo toUserUserInfo(JSONObject jsonObject) {
        return new OidcUserInfoClaim(jsonObject);
    }
}

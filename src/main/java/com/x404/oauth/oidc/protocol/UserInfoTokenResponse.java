package com.x404.oauth.oidc.protocol;

import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;

/**
 * @Author chaox
 * @Date 12/22/2023 8:56 AM
 */
public class UserInfoTokenResponse extends OIDCTokenResponse
{
    private final UserInfo userInfo;

    private UserInfoTokenResponse(OIDCTokenResponse oidcTokenResponse) {
        super(oidcTokenResponse.getOIDCTokens(), oidcTokenResponse.getCustomParameters());
        this.userInfo = new UserInfo(oidcTokenResponse.getOIDCTokens().toJSONObject());
    }

    public static UserInfoTokenResponse from(OIDCTokenResponse oidcTokenResponse) {
        return new UserInfoTokenResponse(oidcTokenResponse);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}

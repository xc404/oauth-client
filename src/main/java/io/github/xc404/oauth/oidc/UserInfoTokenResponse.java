package io.github.xc404.oauth.oidc;

import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;

/**
 * @Author chaox
 * @Date 12/22/2023 8:56 AM
 */
public class UserInfoTokenResponse extends OIDCTokenResponse
{
    private final OidcUserInfo userInfo;

    private UserInfoTokenResponse(OIDCTokenResponse oidcTokenResponse) {
        super(oidcTokenResponse.getOIDCTokens(), oidcTokenResponse.getCustomParameters());
        this.userInfo = new OidcUserInfoClaim(oidcTokenResponse.getOIDCTokens().toJSONObject());
    }

    public static UserInfoTokenResponse from(OIDCTokenResponse oidcTokenResponse) {
        return new UserInfoTokenResponse(oidcTokenResponse);
    }

    public OidcUserInfo getUserInfo() {
        return userInfo;
    }
}

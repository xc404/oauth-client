package io.github.xc404.oauth.core;

/**
 * @Author chaox
 * @Date 12/26/2023 11:10 AM
 */
public class ClientUserInfo extends com.nimbusds.openid.connect.sdk.claims.UserInfo
{
    private final String provider;

    public ClientUserInfo(String provider, com.nimbusds.openid.connect.sdk.claims.UserInfo userInfo) {
        super(userInfo.toJSONObject());
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}

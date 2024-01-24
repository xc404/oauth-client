package io.github.xc404.oauth.oidc;

import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import io.github.xc404.oauth.OAuthException;

import java.text.ParseException;

/**
 * @Author chaox
 * @Date 12/22/2023 8:56 AM
 */
public class UserInfoTokenResponse extends OIDCTokenResponse
{
    private final OidcUserInfo userInfo;

    private UserInfoTokenResponse(OIDCTokenResponse oidcTokenResponse) {
        super(oidcTokenResponse.getOIDCTokens(), oidcTokenResponse.getCustomParameters());
        try {
            this.userInfo = new OidcUserInfoClaim(oidcTokenResponse.getOIDCTokens().getIDToken().getJWTClaimsSet().getClaims());
        } catch( ParseException e ) {
            throw new OAuthException(e);
        }
    }

    public static UserInfoTokenResponse from(OIDCTokenResponse oidcTokenResponse) {
        return new UserInfoTokenResponse(oidcTokenResponse);
    }

    public OidcUserInfo getUserInfo() {
        return userInfo;
    }
}

package io.github.xc404.oauth.provider.github;

import io.github.xc404.oauth.oidc.OidcUserInfoClaim;
import io.github.xc404.oauth.oidc.UserInfoConvertor;

import java.util.Map;
import java.util.Optional;

/**
 * @Author chaox
 * @Date 12/27/2023 9:04 AM
 */
public class GithubUserInfo extends OidcUserInfoClaim
{
    public static final UserInfoConvertor GithubUserInfoConvertor = GithubUserInfo::new;

    public static final String SUB = "id";
    public static final String NAME = "login";
    public static final String PICTURE = "avatar_url";
    public static final String PROFILE = "url";

    public GithubUserInfo(Map<String, Object> claims) {
        super(claims);
    }


    @Override
    public String getSubject() {
        return getClaimAsString(SUB);
    }

    @Override
    public String getFullName() {
        return Optional.ofNullable(super.getFullName()).orElse(this.getNickName());
    }


    @Override
    public String getNickName() {
        return getClaimAsString(NAME);
    }

    @Override
    public String getProfile() {
        return getClaimAsString(PROFILE);
    }

    @Override
    public String getPicture() {
        return getClaimAsString(PICTURE);
    }


}

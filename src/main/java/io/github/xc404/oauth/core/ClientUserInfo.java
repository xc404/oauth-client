package io.github.xc404.oauth.core;

import io.github.xc404.oauth.claim.ClaimAccessor;
import io.github.xc404.oauth.oidc.AddressStandard;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.oidc.OidcUserInfoClaim;

import java.time.Instant;
import java.util.Collections;

/**
 * @Author chaox
 * @Date 12/26/2023 11:10 AM
 */
public class ClientUserInfo extends OidcUserInfoClaim
{
    private final String provider;

    private final OidcUserInfo oidcUserInfo;

    public ClientUserInfo(String provider, OidcUserInfo userInfo) {
        super(userInfo instanceof ClaimAccessor ? ((ClaimAccessor) userInfo).getClaims() : Collections.EMPTY_MAP);
        this.oidcUserInfo = userInfo;
        this.provider = provider;
    }


    public String getProvider() {
        return provider;
    }


    @Override
    public String getSubject() {
        return oidcUserInfo.getSubject();
    }

    @Override
    public String getFullName() {
        return oidcUserInfo.getFullName();
    }

    @Override
    public String getGivenName() {
        return oidcUserInfo.getGivenName();
    }

    @Override
    public String getFamilyName() {
        return oidcUserInfo.getFamilyName();
    }

    @Override
    public String getMiddleName() {
        return oidcUserInfo.getMiddleName();
    }

    @Override
    public String getNickName() {
        return oidcUserInfo.getNickName();
    }

    @Override
    public String getPreferredUsername() {
        return oidcUserInfo.getPreferredUsername();
    }

    @Override
    public String getProfile() {
        return oidcUserInfo.getProfile();
    }

    @Override
    public String getPicture() {
        return oidcUserInfo.getPicture();
    }

    @Override
    public String getWebsite() {
        return oidcUserInfo.getWebsite();
    }

    @Override
    public String getEmail() {
        return oidcUserInfo.getEmail();
    }

    @Override
    public Boolean getEmailVerified() {
        return oidcUserInfo.getEmailVerified();
    }

    @Override
    public String getGender() {
        return oidcUserInfo.getGender();
    }

    @Override
    public String getBirthdate() {
        return oidcUserInfo.getBirthdate();
    }

    @Override
    public String getZoneInfo() {
        return oidcUserInfo.getZoneInfo();
    }

    @Override
    public String getLocale() {
        return oidcUserInfo.getLocale();
    }

    @Override
    public String getPhoneNumber() {
        return oidcUserInfo.getPhoneNumber();
    }

    @Override
    public Boolean getPhoneNumberVerified() {
        return oidcUserInfo.getPhoneNumberVerified();
    }

    @Override
    public AddressStandard getAddress() {
        return oidcUserInfo.getAddress();
    }

    @Override
    public Instant getUpdatedAt() {
        return oidcUserInfo.getUpdatedAt();
    }
}

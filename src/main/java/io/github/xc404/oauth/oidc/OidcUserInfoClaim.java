/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xc404.oauth.oidc;

import io.github.xc404.oauth.claim.ClaimAccessor;

import java.time.Instant;
import java.util.Map;


/**
 * A {@link ClaimAccessor} for the &quot;Standard Claims&quot; that can be returned either
 * in the UserInfo Response or the ID Token.
 *
 * @author Joe Grandja
 * @see ClaimAccessor
 * @see StandardClaimNames
 * @see OidcUserInfo
 * @see <a target="_blank" href=
 * "https://openid.net/specs/openid-connect-core-1_0.html#UserInfoResponse">UserInfo
 * Response</a>
 * @see <a target="_blank" href=
 * "https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims">Standard
 * Claims</a>
 * @since 5.0
 */
public class OidcUserInfoClaim implements ClaimAccessor, OidcUserInfo
{
    private final Map<String, Object> claims;

    public OidcUserInfoClaim(Map<String, Object> claims) {
        this.claims = claims;
    }

    /**
     * Returns the Subject identifier {@code (sub)}.
     *
     * @return the Subject identifier
     */
    public String getSubject() {
        return this.getClaimAsString(StandardClaimNames.SUB);
    }

    /**
     * Returns the user's full name {@code (name)} in displayable form.
     *
     * @return the user's full name
     */
    public String getFullName() {
        return this.getClaimAsString(StandardClaimNames.NAME);
    }

    /**
     * Returns the user's given name(s) or first name(s) {@code (given_name)}.
     *
     * @return the user's given name(s)
     */
    public String getGivenName() {
        return this.getClaimAsString(StandardClaimNames.GIVEN_NAME);
    }

    /**
     * Returns the user's surname(s) or last name(s) {@code (family_name)}.
     *
     * @return the user's family names(s)
     */
    public String getFamilyName() {
        return this.getClaimAsString(StandardClaimNames.FAMILY_NAME);
    }

    /**
     * Returns the user's middle name(s) {@code (middle_name)}.
     *
     * @return the user's middle name(s)
     */
    public String getMiddleName() {
        return this.getClaimAsString(StandardClaimNames.MIDDLE_NAME);
    }

    /**
     * Returns the user's nick name {@code (nickname)} that may or may not be the same as
     * the {@code (given_name)}.
     *
     * @return the user's nick name
     */
    public String getNickName() {
        return this.getClaimAsString(StandardClaimNames.NICKNAME);
    }

    /**
     * Returns the preferred username {@code (preferred_username)} that the user wishes to
     * be referred to.
     *
     * @return the user's preferred user name
     */
    public String getPreferredUsername() {
        return this.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
    }

    /**
     * Returns the URL of the user's profile page {@code (profile)}.
     *
     * @return the URL of the user's profile page
     */
    public String getProfile() {
        return this.getClaimAsString(StandardClaimNames.PROFILE);
    }

    /**
     * Returns the URL of the user's profile picture {@code (picture)}.
     *
     * @return the URL of the user's profile picture
     */
    public String getPicture() {
        return this.getClaimAsString(StandardClaimNames.PICTURE);
    }

    /**
     * Returns the URL of the user's web page or blog {@code (website)}.
     *
     * @return the URL of the user's web page or blog
     */
    public String getWebsite() {
        return this.getClaimAsString(StandardClaimNames.WEBSITE);
    }

    /**
     * Returns the user's preferred e-mail address {@code (email)}.
     *
     * @return the user's preferred e-mail address
     */
    public String getEmail() {
        return this.getClaimAsString(StandardClaimNames.EMAIL);
    }

    /**
     * Returns {@code true} if the user's e-mail address has been verified
     * {@code (email_verified)}, otherwise {@code false}.
     *
     * @return {@code true} if the user's e-mail address has been verified, otherwise
     * {@code false}
     */
    public Boolean getEmailVerified() {
        return this.getClaimAsBoolean(StandardClaimNames.EMAIL_VERIFIED);
    }

    /**
     * Returns the user's gender {@code (gender)}.
     *
     * @return the user's gender
     */
    public String getGender() {
        return this.getClaimAsString(StandardClaimNames.GENDER);
    }

    /**
     * Returns the user's birth date {@code (birthdate)}.
     *
     * @return the user's birth date
     */
    public String getBirthdate() {
        return this.getClaimAsString(StandardClaimNames.BIRTHDATE);
    }

    /**
     * Returns the user's time zone {@code (zoneinfo)}.
     *
     * @return the user's time zone
     */
    public String getZoneInfo() {
        return this.getClaimAsString(StandardClaimNames.ZONEINFO);
    }

    /**
     * Returns the user's locale {@code (locale)}.
     *
     * @return the user's locale
     */
    public String getLocale() {
        return this.getClaimAsString(StandardClaimNames.LOCALE);
    }

    /**
     * Returns the user's preferred phone number {@code (phone_number)}.
     *
     * @return the user's preferred phone number
     */
    public String getPhoneNumber() {
        return this.getClaimAsString(StandardClaimNames.PHONE_NUMBER);
    }

    /**
     * Returns {@code true} if the user's phone number has been verified
     * {@code (phone_number_verified)}, otherwise {@code false}.
     *
     * @return {@code true} if the user's phone number has been verified, otherwise
     * {@code false}
     */
    public Boolean getPhoneNumberVerified() {
        return this.getClaimAsBoolean(StandardClaimNames.PHONE_NUMBER_VERIFIED);
    }

    /**
     * Returns the user's preferred postal address {@code (address)}.
     *
     * @return the user's preferred postal address
     */
    public AddressStandard getAddress() {
        Map<String, Object> addressFields = this.getClaimAsMap(StandardClaimNames.ADDRESS);
        return new AddressStandardClaim(addressFields);
    }

    /**
     * Returns the time the user's information was last updated {@code (updated_at)}.
     *
     * @return the time the user's information was last updated
     */
    public Instant getUpdatedAt() {
        return this.getClaimAsInstant(StandardClaimNames.UPDATED_AT);
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }
}

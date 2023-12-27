package io.github.xc404.oauth.oidc;

import java.time.Instant;

/**
 * @Author chaox
 * @Date 12/26/2023 8:01 PM
 */
public interface OidcUserInfo
{
    /**
     * Returns the Subject identifier {@code (sub)}.
     *
     * @return the Subject identifier
     */
    String getSubject();

    /**
     * Returns the user's full name {@code (name)} in displayable form.
     *
     * @return the user's full name
     */
    default String getFullName() {
        return null;
    }

    /**
     * Returns the user's given name(s) or first name(s) {@code (given_name)}.
     *
     * @return the user's given name(s)
     */
    default String getGivenName() {
        return null;
    }

    /**
     * Returns the user's surname(s) or last name(s) {@code (family_name)}.
     *
     * @return the user's family names(s)
     */
    default String getFamilyName() {
        return null;
    }

    /**
     * Returns the user's middle name(s) {@code (middle_name)}.
     *
     * @return the user's middle name(s)
     */
    default String getMiddleName() {
        return null;
    }

    /**
     * Returns the user's nick name {@code (nickname)} that may or may not be the same as
     * the {@code (given_name)}.
     *
     * @return the user's nick name
     */
    default String getNickName() {
        return null;
    }

    /**
     * Returns the preferred username {@code (preferred_username)} that the user wishes to
     * be referred to.
     *
     * @return the user's preferred user name
     */
    default String getPreferredUsername() {
        return null;
    }

    /**
     * Returns the URL of the user's profile page {@code (profile)}.
     *
     * @return the URL of the user's profile page
     */
    default String getProfile() {
        return null;
    }

    /**
     * Returns the URL of the user's profile picture {@code (picture)}.
     *
     * @return the URL of the user's profile picture
     */
    default String getPicture() {
        return null;
    }

    /**
     * Returns the URL of the user's web page or blog {@code (website)}.
     *
     * @return the URL of the user's web page or blog
     */
    default String getWebsite() {
        return null;
    }

    /**
     * Returns the user's preferred e-mail address {@code (email)}.
     *
     * @return the user's preferred e-mail address
     */
    default String getEmail() {
        return null;
    }

    /**
     * Returns {@code true} if the user's e-mail address has been verified
     * {@code (email_verified)}, otherwise {@code false}.
     *
     * @return {@code true} if the user's e-mail address has been verified, otherwise
     * {@code false}
     */
    default Boolean getEmailVerified() {
        return null;
    }

    /**
     * Returns the user's gender {@code (gender)}.
     *
     * @return the user's gender
     */
    default String getGender() {
        return null;
    }

    /**
     * Returns the user's birth date {@code (birthdate)}.
     *
     * @return the user's birth date
     */
    default String getBirthdate() {
        return null;
    }

    /**
     * Returns the user's time zone {@code (zoneinfo)}.
     *
     * @return the user's time zone
     */
    default String getZoneInfo() {
        return null;
    }

    /**
     * Returns the user's locale {@code (locale)}.
     *
     * @return the user's locale
     */
    default String getLocale() {
        return null;
    }

    /**
     * Returns the user's preferred phone number {@code (phone_number)}.
     *
     * @return the user's preferred phone number
     */
    default String getPhoneNumber() {
        return null;
    }

    /**
     * Returns {@code true} if the user's phone number has been verified
     * {@code (phone_number_verified)}, otherwise {@code false}.
     *
     * @return {@code true} if the user's phone number has been verified, otherwise
     * {@code false}
     */
    default Boolean getPhoneNumberVerified() {
        return false;
    }

    /**
     * Returns the user's preferred postal address {@code (address)}.
     *
     * @return the user's preferred postal address
     */
    default AddressStandard getAddress() {
        return null;
    }

    /**
     * Returns the time the user's information was last updated {@code (updated_at)}.
     *
     * @return the time the user's information was last updated
     */
    default Instant getUpdatedAt() {
        return null;
    }
}

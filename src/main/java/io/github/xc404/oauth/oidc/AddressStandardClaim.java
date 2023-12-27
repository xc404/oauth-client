package io.github.xc404.oauth.oidc;

import io.github.xc404.oauth.claim.ClaimAccessor;

import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 7:56 AM
 */
public class AddressStandardClaim implements ClaimAccessor, AddressStandard
{
    private final Map<String, Object> claims;

    public AddressStandardClaim(Map<String, Object> claims) {
        this.claims = claims;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }

    @Override
    public String getFormatted() {
        return getClaimAsString(StandardClaimNames.FORMATTED);
    }

    @Override
    public String getStreetAddress() {
        return getClaimAsString(StandardClaimNames.STREET_ADDRESS);
    }

    @Override
    public String getLocality() {
        return getClaimAsString(StandardClaimNames.LOCALITY);
    }

    @Override
    public String getRegion() {
        return getClaimAsString(StandardClaimNames.REGION);
    }

    @Override
    public String getPostalCode() {
        return getClaimAsString(StandardClaimNames.POSTAL_CODE);
    }

    @Override
    public String getCountry() {
        return getClaimAsString(StandardClaimNames.COUNTRY);
    }
}

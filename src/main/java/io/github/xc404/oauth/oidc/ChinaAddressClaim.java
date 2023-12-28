package io.github.xc404.oauth.oidc;

import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 2:34 PM
 */
public class ChinaAddressClaim extends AddressStandardClaim implements ChinaAddress
{

    public static final String PROVINCE_CLAIM = "province";
    public static final String CITY_CLAIM = "city";

    public ChinaAddressClaim(Map<String, Object> claims) {
        super(claims);
    }

    @Override
    public String getLocality() {
        return super.getClaimAsString(CITY_CLAIM);
    }

    @Override
    public String getRegion() {
        return super.getClaimAsString(PROVINCE_CLAIM);
    }
}

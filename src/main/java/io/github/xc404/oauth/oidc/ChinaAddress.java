package io.github.xc404.oauth.oidc;

/**
 * @Author chaox
 * @Date 12/27/2023 2:27 PM
 */
public interface ChinaAddress extends AddressStandard
{

    String CHINA_ADDRESS_FORMAT = "%s%s%s%s";

    @Override
    public default String getFormatted() {
        return String.format(CHINA_ADDRESS_FORMAT,getCountry(),getRegion(),getLocality(),getStreetAddress());
    }
}

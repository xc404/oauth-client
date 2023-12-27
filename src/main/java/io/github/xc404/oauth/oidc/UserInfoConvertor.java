package io.github.xc404.oauth.oidc;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import net.minidev.json.JSONObject;

/**
 * @Author chaox
 * @Date 12/27/2023 8:31 AM
 */
public interface UserInfoConvertor
{
    OidcUserInfo toUserUserInfo(JSONObject jsonObject);

    default OidcUserInfo toUserInfo(HTTPResponse response) {
        return null;
    }
}

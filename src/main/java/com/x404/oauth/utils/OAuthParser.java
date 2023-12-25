package com.x404.oauth.utils;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.oauth2.sdk.util.MultivaluedMapUtils;
import com.nimbusds.oauth2.sdk.util.URIUtils;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.x404.oauth.core.LoginTokenGrant;
import net.minidev.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author chaox
 * @Date 12/25/2023 6:52 PM
 */
public class OAuthParser
{
    public static AuthorizationResponse parseAuthorizationResponse(URI uri) {
        AuthenticationResponse response;
        Map<String, List<String>> additional = null;
        try {
            Map<String, List<String>> parameters = AuthenticationSuccessResponse.parseResponseParameters(uri);
            response = AuthenticationResponseParser.parse(URIUtils.getBaseURI(uri), parameters);
            if( response.indicatesSuccess() ) {
                additional = new HashMap<>(parameters);
                Set<String> tokenKeys = response.toSuccessResponse().toParameters().keySet();
                tokenKeys.forEach(additional::remove);
            }
        } catch( ParseException e ) {
            throw new RuntimeException(e);
        }
        return new AuthorizationResponse(response, additional);
    }

    public static AuthorizationGrant parseAuthorizationGrant(String requestJson) {
        try {
            JSONObject jsonObject = JSONObjectUtils.parse(requestJson);
            Map<String, List<String>> params = ParameterMapUtils.toParameterMap(jsonObject);
            return parseAuthorizationGrant(params);

        } catch( ParseException e ) {
            throw new RuntimeException(e);
        }
    }

    public static AuthorizationGrant parseAuthorizationGrant(Map<String, List<String>> params) {
        try {
            String grantTypeString = MultivaluedMapUtils.getFirstValue(params, "grant_type");
            GrantType grantType = GrantType.parse(grantTypeString);
            if( LoginTokenGrant.GRANT_TYPE.equals(grantType) ) {
                LoginTokenGrant.parse(params);
            }
            return AuthorizationGrant.parse(params);
        } catch( ParseException e ) {
            throw new RuntimeException(e);
        }
    }

    public static class AuthorizationResponse
    {
        private final AuthenticationResponse authenticationResponse;
        private final Map<String, List<String>> additionalParameters;

        public AuthorizationResponse(AuthenticationResponse authenticationResponse, Map<String, List<String>> additionalParameters) {
            this.authenticationResponse = authenticationResponse;
            this.additionalParameters = additionalParameters;
        }

        public AuthenticationResponse getAuthenticationResponse() {
            return authenticationResponse;
        }

        public Map<String, List<String>> getAdditionalParameters() {
            return additionalParameters;
        }
    }
}

package io.github.xc404.oauth.core;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.MultivaluedMapUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/25/2023 3:22 PM
 */
public class LoginTokenGrant extends AuthorizationGrant
{
    public static final GrantType GRANT_TYPE = new GrantType("login_token");
    private static final String MISSING_LOGIN_TOKEN_PARAM_MESSAGE = "Missing or empty login_token parameter";
    protected static final ParseException MISSING_LOGIN_TOKEN_PARAM_EXCEPTION
            = new ParseException(MISSING_LOGIN_TOKEN_PARAM_MESSAGE,
            OAuth2Error.INVALID_REQUEST.appendDescription(": " + MISSING_LOGIN_TOKEN_PARAM_MESSAGE));
    private final LoginToken loginToken;

    public LoginTokenGrant(LoginToken loginToken) {
        super(GRANT_TYPE);
        this.loginToken = loginToken;
    }

    public static AuthorizationGrant parse(final Map<String, List<String>> params) throws ParseException {
        GrantType.ensure(GRANT_TYPE, params);

        // Parse JWT assertion
        String loginToken = MultivaluedMapUtils.getFirstValue(params, "login_token");
        if( StringUtils.isBlank(loginToken) ) {
            throw MISSING_LOGIN_TOKEN_PARAM_EXCEPTION;
        }
        return new LoginTokenGrant(new LoginToken(loginToken));
    }

    @Override
    public Map<String, List<String>> toParameters() {
        return Collections.EMPTY_MAP;
    }

    public LoginToken getLoginToken() {
        return loginToken;
    }
}

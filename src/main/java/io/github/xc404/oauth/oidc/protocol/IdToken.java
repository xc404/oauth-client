package io.github.xc404.oauth.oidc.protocol;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

/**
 * @Author chaox
 * @Date 12/24/2023 4:51 PM
 */
public class IdToken
{
    private final JWT jwt;

    public IdToken(String tokenValue) {
        try {
            this.jwt = JWTParser.parse(tokenValue);
        } catch( ParseException e ) {
            throw new IllegalArgumentException(e);
        }
    }

    public IdToken(JWT jwt) {
        this.jwt = jwt;
    }

    public JWT getJwt() {
        return jwt;
    }

    public JWTClaimsSet getJWTClaimsSet() {
        try {
            return jwt.getJWTClaimsSet();
        } catch( ParseException e ) {
            throw new RuntimeException(e);
        }
    }


}

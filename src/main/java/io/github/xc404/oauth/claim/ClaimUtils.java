package io.github.xc404.oauth.claim;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 7:28 AM
 */
public class ClaimUtils
{

    public static String toString(Object claim) {
        return (claim != null) ? claim.toString() : null;
    }

    public static Boolean toBoolean(Object claim) {
        if( claim == null ) {
            return null;
        }
        if( claim instanceof Boolean ) {
            return (Boolean) claim;
        }
        if( claim instanceof String ) {
            return Boolean.valueOf((String) claim);
        }
        return null;
    }

    public static URL toURL(Object claim) {
        if( claim == null ) {
            return null;
        }
        if( claim instanceof URL ) {
            return (URL) claim;
        }
        try {
            return new URI(claim.toString()).toURL();
        } catch( Exception ex ) {
            // Ignore
        }
        return null;
    }

    public static List<String> toStringList(Object claim) {
        if( claim == null ) {
            return null;
        }
        if( claim instanceof Collection ) {
            List<String> results = new ArrayList<>();
            for( Object object : ((Collection<?>) claim) ) {
                if( object != null ) {
                    results.add(object.toString());
                }
            }
            return results;
        }
        return Collections.singletonList(claim.toString());
    }

    public static Map<String, Object> toMap(Object claim) {
        if( claim == null ) {
            return null;
        }
        if( !(claim instanceof Map) ) {
            return null;
        }
        Map<?, ?> sourceMap = (Map<?, ?>) claim;
        Map<String, Object> result = new HashMap<>();
        sourceMap.forEach((k, v) -> result.put(k.toString(), v));
        return result;
    }

    public static Instant toInstant(Object claim) {
        if( claim == null ) {
            return null;
        }
        if( claim instanceof Instant ) {
            return (Instant) claim;
        }
        if( claim instanceof Date ) {
            return ((Date) claim).toInstant();
        }
        if( claim instanceof Number ) {
            return Instant.ofEpochSecond(((Number) claim).longValue());
        }
        try {
            return Instant.ofEpochSecond(Long.parseLong(claim.toString()));
        } catch( Exception ex ) {
            // Ignore
        }
        try {
            return Instant.parse(claim.toString());
        } catch( Exception ex ) {
            // Ignore
        }
        return null;
    }
}

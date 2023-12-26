package io.github.xc404.oauth.utils;


import com.nimbusds.oauth2.sdk.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

public class UrlUtils
{

    public static String join(String base, String path) {
        if( !StringUtils.isBlank(path) ) {
            return base;
        }
        if( base.endsWith("/") ) {
            base = base.substring(0, base.length() - 1);
        }
        if( path.startsWith("/") ) {
            path = path.substring(1);
        }
        return base + "/" + path;
    }

    public static String getBase(String url) {
        URI uri = URI.create(url);
        return uri.getScheme() + "//" + uri.getHost() + ":" + uri.getPort();
    }

    public static String appendQuery(String url, Map<String, String> params) {
        return UrlUtils.appendQueryString(url, UrlUtils.queryString(params));
    }

    public static String queryString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> sb.append("&").append(key)
                .append("=").append(encodeValue(value)));
        if( sb.length() > 0 ) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }


    public static String appendQueryString(String url, String queryString) {
        if( queryString.length() > 0 ) {
            if( url.contains("?") ) {
                return url + "&" + queryString;
            } else {
                return url + "?" + queryString;
            }
        }
        return url;
    }

    public static URI toURI(String url) {
        try {
            return new URI(url);
        } catch( URISyntaxException e ) {
            throw new IllegalArgumentException(e);
        }
    }


    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch( UnsupportedEncodingException e ) {
            throw new IllegalArgumentException(e);
        }
    }

}

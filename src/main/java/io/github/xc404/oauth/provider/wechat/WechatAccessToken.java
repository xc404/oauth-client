package io.github.xc404.oauth.provider.wechat;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.rar.AuthorizationDetail;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.AccessTokenType;
import com.nimbusds.oauth2.sdk.token.TokenTypeURI;
import net.minidev.json.JSONObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author chaox
 * @Date 12/27/2023 2:45 PM
 */
public class WechatAccessToken extends AccessToken
{
    private final String openid;
    private final AccessToken proxy;

    public WechatAccessToken(String openid, AccessToken proxy) {
        super(proxy.getType(), proxy.getValue());
        this.openid = openid;
        this.proxy = proxy;
    }

    public String getOpenid() {
        return openid;
    }

    @Override
    public AccessTokenType getType() {
        return proxy.getType();
    }

    @Override
    public long getLifetime() {
        return proxy.getLifetime();
    }

    @Override
    public Scope getScope() {
        return proxy.getScope();
    }

    @Override
    public List<AuthorizationDetail> getAuthorizationDetails() {
        return proxy.getAuthorizationDetails();
    }

    @Override
    public TokenTypeURI getIssuedTokenType() {
        return proxy.getIssuedTokenType();
    }

    @Override
    public Set<String> getParameterNames() {
        Set<String> parameters = new HashSet<>();
        parameters.addAll(proxy.getParameterNames());
        parameters.add(WechatUserInfo.SUB);
        return Collections.unmodifiableSet(parameters);
    }

    @Override
    public JSONObject toJSONObject() {
        return proxy.toJSONObject();
    }

    @Override
    public String toJSONString() {
        return proxy.toJSONString();
    }

    @Override
    public String toAuthorizationHeader() {
        return proxy.toAuthorizationHeader();
    }

}

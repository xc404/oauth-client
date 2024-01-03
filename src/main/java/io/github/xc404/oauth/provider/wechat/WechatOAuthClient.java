package io.github.xc404.oauth.provider.wechat;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.core.DefaultOAuthClient;
import io.github.xc404.oauth.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 2:42 PM
 */
public class WechatOAuthClient extends DefaultOAuthClient
{
    public WechatOAuthClient(OAuthConfig authConfig) {
        super(authConfig);
        if( this.scope == null ) {
            this.scope = Scope.parse("snsapi_login");
        }
        this.setUserInfoConvertor(WechatUserInfo.WechatUserInfoConvertor);
    }

    @Override
    public URI requestAuthorization(OAuthContext oAuthContext) {
        oAuthContext.setStep(OAuthContext.OAuthStep.AUTHORIZATION);
        State state = oAuthContext.getState();
        if( state == null ) {
            throw new IllegalStateException("State not exist in oauth context.");
        }

        Map<String, String> params = new HashMap<>();
        params.put("appid", this.clientID.getValue());
        params.put("redirect_uri", this.redirectUri.toString());
        params.put("response_type", this.getOAuthConfig().getResponseType().toString());
        params.put("scope", getScopeParams());
        params.put("state", state.getValue());
        return URI.create(UrlUtils.appendQuery(this.authorizationURI.toString(), params));
    }


    @Override
    protected HTTPRequest buildAccessTokenRequest(AuthorizationGrant authorizationGrant, Map<String, List<String>> parameters) {
        Map<String, List<String>> params = new HashMap<>();
        if(parameters != null){
            params.putAll(parameters);
        }
        params.put("appid", Collections.singletonList(this.clientID.getValue()));
        params.put("secret", Collections.singletonList(this.clientSecret.getValue()));
        params.putAll(authorizationGrant.toParameters());
        HTTPRequest httpRequest = new HTTPRequest(HTTPRequest.Method.GET, this.accessTokenURI);
        httpRequest.appendQueryParameters(params);
        httpRequest.setAccept(APPLICATION_JSON_ACCEPT);
        return httpRequest;
    }

    @Override
    protected AccessTokenResponse parseAccessTokenResponse(OAuthContext oAuthContext, HTTPResponse httpResponse) {
        AccessTokenResponse accessTokenResponse = super.parseAccessTokenResponse(oAuthContext, httpResponse);
        String openid = (String) accessTokenResponse.getCustomParameters().get("openid");

        AccessToken accessToken = accessTokenResponse.getTokens().getAccessToken();
        Tokens tokens = new Tokens(new WechatAccessToken(openid, accessToken), accessTokenResponse.getTokens().getRefreshToken());
        return new AccessTokenResponse(tokens, accessTokenResponse.getCustomParameters());
    }

    @Override
    protected HTTPRequest buildUserInfoRequest(AccessToken accessToken) {
        WechatAccessToken wechatAccessToken = (WechatAccessToken) accessToken;
        HTTPRequest httpRequest = super.buildUserInfoRequest(accessToken);
        httpRequest.appendQueryParameters(Collections.singletonMap(WechatUserInfo.SUB,Collections.singletonList(wechatAccessToken.getOpenid())));
        return httpRequest;
    }

    private String getScopeParams() {
        return StringUtils.join(scope.toStringList(), ",");
    }

}

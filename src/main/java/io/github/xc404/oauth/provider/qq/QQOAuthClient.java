package io.github.xc404.oauth.provider.qq;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.Tokens;
import io.github.xc404.oauth.OAuthException;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.core.ClientUserInfo;
import io.github.xc404.oauth.core.DefaultOAuthClient;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.provider.wechat.WechatAccessToken;
import io.github.xc404.oauth.provider.wechat.WechatUserInfo;
import io.github.xc404.oauth.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 2:42 PM
 */
public class QQOAuthClient extends DefaultOAuthClient
{
    public QQOAuthClient(OAuthConfig authConfig) {
        super(authConfig);
        this.setUserInfoConvertor(QQUserInfo.QQUserInfoConvertor);
    }

    @Override
    public URI requestAuthorization(OAuthContext oAuthContext) {
        return super.requestAuthorization(oAuthContext);
    }


    @Override
    protected HTTPRequest buildAccessTokenRequest(AuthorizationGrant authorizationGrant, Map<String, List<String>> parameters) {
        parameters.put("fmt", Collections.singletonList("json"));
        parameters.put("need_openid", Collections.singletonList("1"));
        return super.buildAccessTokenRequest(authorizationGrant, parameters);
    }

    @Override
    protected AccessTokenResponse parseAccessTokenResponse(OAuthContext oAuthContext, HTTPResponse httpResponse) {
        AccessTokenResponse accessTokenResponse = super.parseAccessTokenResponse(oAuthContext, httpResponse);
        String openid = (String) accessTokenResponse.getCustomParameters().get("openid");

        AccessToken accessToken = accessTokenResponse.getTokens().getAccessToken();
        Tokens tokens = new Tokens(new QQAccessToken(openid, accessToken), accessTokenResponse.getTokens().getRefreshToken());
        return new AccessTokenResponse(tokens, accessTokenResponse.getCustomParameters());
    }

    @Override
    protected HTTPRequest buildUserInfoRequest(AccessToken accessToken) {
        QQAccessToken wechatAccessToken = (QQAccessToken) accessToken;
        HTTPRequest httpRequest = super.buildUserInfoRequest(accessToken);
        Map<String,List<String>> params = new HashMap<>();
        params.put("openid",Collections.singletonList(wechatAccessToken.getOpenid()));
        params.put("oauth_consumer_key",Collections.singletonList(clientID.getValue()));
        httpRequest.appendQueryParameters(params);
        return httpRequest;
    }

    @Override
    public OidcUserInfo getUserInfo(AccessToken accessToken) {
        try {
            QQAccessToken qqAccessToken = (QQAccessToken) accessToken;
            HTTPRequest httpRequest = buildUserInfoRequest(accessToken);
            HTTPResponse httpResponse = httpRequest.send();
            if( !httpResponse.indicatesSuccess() ) {
                throw new OAuthException(ErrorObject.parse(httpResponse));
            }

            QQUserInfo userInfo = (QQUserInfo) super.convertToUserInfo(httpResponse);
            if( userInfo == null ) {
                throw new OAuthException(ErrorObject.parse(httpResponse));
            }
            userInfo.setOpenId(qqAccessToken.getOpenid());
            return new ClientUserInfo(this.authConfig.getProvider(), userInfo);

        } catch( IOException e ) {
            throw new OAuthException(e);
        }
    }

}

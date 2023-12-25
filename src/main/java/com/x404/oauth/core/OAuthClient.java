package com.x404.oauth.core;


import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.x404.oauth.config.OAuthConfig;
import com.x404.oauth.context.OAuthContext;
import com.x404.oauth.oidc.protocol.IdToken;

import java.net.URI;

public interface OAuthClient
{

    URI requestAuthorization(OAuthContext oAuthContext);


    AccessTokenResponse authenticate(OAuthContext oAuthContext, AuthorizationGrant authorizationGrant);


    UserInfo getUserInfo(AccessToken accessToken);

    UserInfo getUserInfo(OAuthContext oAuthContext, IdToken idToken);

    OAuthConfig getOAuthConfig();

}

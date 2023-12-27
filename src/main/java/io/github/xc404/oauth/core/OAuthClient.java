package io.github.xc404.oauth.core;


import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.oidc.IdToken;
import io.github.xc404.oauth.oidc.OidcUserInfo;

import java.net.URI;

public interface OAuthClient
{

    URI requestAuthorization(OAuthContext oAuthContext);


    AccessTokenResponse authenticate(OAuthContext oAuthContext, AuthorizationGrant authorizationGrant);


    OidcUserInfo getUserInfo(AccessToken accessToken);

    OidcUserInfo getUserInfo(OAuthContext oAuthContext, IdToken oidcIdToken);

    OAuthConfig getOAuthConfig();

}

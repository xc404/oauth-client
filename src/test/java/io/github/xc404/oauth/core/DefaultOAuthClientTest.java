package io.github.xc404.oauth.core;


import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.github.xc404.oauth.config.OAuthClientConfig;
import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthConfigRepository;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.github.xc404.oauth.springboot.OAuthClientProperties;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * @Author chaox
 * @Date 12/26/2023 2:48 PM
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DefaultOAuthClientTest
{



    @Autowired
    private OAuthClientFactory oAuthClientFactory;


    @Test
    public void githubAuthorizationRequest(){
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.GITHUB.getProvider());
        String redirectUri = oAuthClient.getOAuthConfig().getRedirectUri();
        Assert.assertNotNull(oAuthClient);
        URI uri = oAuthClient.requestAuthorization(OAuthContext.create());
        System.out.println(uri);

    }
    @Test
    public void githubAuthorizationComplete(){
//        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.GITHUB.getProvider());
//        String redirectUri = oAuthClient.getOAuthConfig().getRedirectUri();
//        Assert.assertNotNull(oAuthClient);
//        URI uri = oAuthClient.requestAuthorization(OAuthContext.create());
//        System.out.println(uri);
        String url = "http://localhost:8080/oauth/github/complete?code=735c52d7ffeec34946d2&state=okHkIUfNoWT6DDpMgGLZzFl2jSpl59tKajFiEwWBWDs";
        ExtractableResponse<Response> response = given()
                .redirects().follow(false)
                .get(url)
                .then().assertThat().statusCode(302).extract();
        String location = response.header("Location");
        System.out.println(location);
        URI uri = URI.create(location);


    }


    @Test
    public void getUserInfo(){
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.GITHUB.getProvider());
        OidcUserInfo userInfo = oAuthClient.getUserInfo(new BearerAccessToken("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"));
        System.out.println(userInfo.getSubject());
        System.out.println(userInfo.getFullName());

    }
}
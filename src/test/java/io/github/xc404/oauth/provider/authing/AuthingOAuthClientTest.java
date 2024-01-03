package io.github.xc404.oauth.provider.authing;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.core.DefaultOAuthClient;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.core.OAuthClientFactory;
import io.github.xc404.oauth.oidc.IdToken;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.github.xc404.oauth.provider.qq.QQAccessToken;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static io.restassured.RestAssured.given;

/**
 * @Author chaox
 * @Date 12/30/2023 8:45 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AuthingOAuthClientTest
{
    @Autowired
    private OAuthClientFactory oAuthClientFactory;
    @Test
    public void githubAuthorizationRequest(){
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient("authing");
        String redirectUri = oAuthClient.getOAuthConfig().getRedirectUri();
        Assert.assertNotNull(oAuthClient);
        URI uri = oAuthClient.requestAuthorization(OAuthContext.create());
        System.out.println(uri);

    }
    @Test
    public void githubAuthorizationComplete(){

        String url = "http://localhost:8080/oauth/authing/complete?code=cWC6FUnF_txVifsAXwe_PnW4gLyJr5eUhR5XqdNqiuB&state=9tWTMedGh\n";
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
        DefaultOAuthClient oAuthClient = (DefaultOAuthClient) oAuthClientFactory.getOAuthClient("authing");
        String idToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2NThmNzMyZDI0NjZiZmVlNmMyNWI4MWUiLCJhdWQiOiI2NTdkNWFlZWVjYjNiOWU0ZjBkNTQ1MWMiLCJpYXQiOjE3MDQwNzIyOTcsImV4cCI6MTcwNTI4MTg5NywiaXNzIjoiaHR0cHM6Ly9ibGNsOHFtOXp2cmUtZGVtby5hdXRoaW5nLmNuL29pZGMifQ.0lqew0MfAMpvFNngi2rD7sywCE9JEkKmnZCYhMHpPL4";
        IdToken token = new IdToken(idToken);
        OidcUserInfo userInfo = oAuthClient.getUserInfo(OAuthContext.create(), token);
    }
}
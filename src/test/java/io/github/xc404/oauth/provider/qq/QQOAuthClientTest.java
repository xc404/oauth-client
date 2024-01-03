package io.github.xc404.oauth.provider.qq;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.core.OAuthClientFactory;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.github.xc404.oauth.provider.wechat.WechatAccessToken;
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
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author chaox
 * @Date 12/30/2023 8:45 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class QQOAuthClientTest
{
    @Autowired
    private OAuthClientFactory oAuthClientFactory;
    @org.junit.Test
    public void githubAuthorizationRequest(){
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.QQ.getProvider());
        String redirectUri = oAuthClient.getOAuthConfig().getRedirectUri();
        Assert.assertNotNull(oAuthClient);
        URI uri = oAuthClient.requestAuthorization(OAuthContext.create());
        System.out.println(uri);

    }
    @org.junit.Test
    public void githubAuthorizationComplete(){

        String url = "http://localhost:9000/sys/authentication/external/redirect?code=EA0F21ED1002AB3EFB94C2F8B35E917C&state=aGthEfXr3YP6bJ2nZhkGUODG_2n7qJPlsL1ajAfred4";
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
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.QQ.getProvider());
        OidcUserInfo userInfo = oAuthClient.getUserInfo(new QQAccessToken("81B89A92647C4E324BC76FE9BA3BBC62", new BearerAccessToken("24B0A53B5791AC741EB632D183398409")));
        System.out.println(userInfo.getSubject());
        System.out.println(userInfo.getNickName());
        RestClient.create();
    }
}
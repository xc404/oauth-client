package io.github.xc404.oauth.provider.wechat;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.github.xc404.oauth.context.OAuthContext;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.core.OAuthClientFactory;
import io.github.xc404.oauth.oidc.OidcUserInfo;
import io.github.xc404.oauth.provider.CommonOAuthProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author chaox
 * @Date 12/27/2023 4:14 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WechatOAuthClientTest
{

    @Autowired
    private OAuthClientFactory oAuthClientFactory;
    @org.junit.Test
    public void githubAuthorizationRequest(){
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.WECHAT.getProvider());
        String redirectUri = oAuthClient.getOAuthConfig().getRedirectUri();
        Assert.assertNotNull(oAuthClient);
        URI uri = oAuthClient.requestAuthorization(OAuthContext.create());
        System.out.println(uri);

    }
    @org.junit.Test
    public void githubAuthorizationComplete(){

        String url = "http://localhost:9000/sys/authentication/external/redirect?code=091SVfGa1kZ4EG0refIa1Ho5k71SVfGQ&state=D6lpdEsayvdMw5-639uWGe5QS1vuo7YMm13kF7V9GD8";
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
        OAuthClient oAuthClient = oAuthClientFactory.getOAuthClient(CommonOAuthProvider.WECHAT.getProvider());
        OidcUserInfo userInfo = oAuthClient.getUserInfo(new WechatAccessToken("o45IpwiB_uIRlYHzdhxe1on6T2Fg", new BearerAccessToken("75_ZU899cws1mnWUq5jy6Kzk2lxk4L7qwbGfcL_dKdl3A4yh8BOlBCCU_a0hgDwXiuoWlvYDzxdPzPacjBzPWVrTVyZ-j19L631crFBpmDyYRs")));
        System.out.println(userInfo.getSubject());
        System.out.println(userInfo.getFullName());

    }
}
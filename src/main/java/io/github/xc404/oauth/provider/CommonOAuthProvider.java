package io.github.xc404.oauth.provider;

import io.github.xc404.oauth.config.OAuthConfig;
import io.github.xc404.oauth.config.OAuthProviderConfig;
import io.github.xc404.oauth.core.OAuthClient;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.provider.github.GithubUserInfo;
import io.github.xc404.oauth.provider.qq.QQOAuthClient;
import io.github.xc404.oauth.provider.wechat.WechatOAuthClient;

/**
 * @Author chaox
 * @Date 12/22/2023 7:54 PM
 */
public enum CommonOAuthProvider implements OAuthProviderConfig
{

    GOOGLE {
        @Override
        public String getAuthorizationUri() {
            return "https://accounts.google.com/o/oauth2/v2/auth";
        }

        @Override
        public String getAccessTokenUri() {
            return "https://www.googleapis.com/oauth2/v4/token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://www.googleapis.com/oauth2/v3/userinfo";
        }

        @Override
        public String getJwkSetUri() {
            return "https://www.googleapis.com/oauth2/v3/certs";
        }

        @Override
        public String getIssuerUri() {
            return "https://accounts.google.com";
        }
    },
    FACEBOOK {
        @Override
        public String getAuthorizationUri() {
            return "https://www.facebook.com/v10.0/dialog/oauth";
        }

        @Override
        public String getAccessTokenUri() {
            return "https://graph.facebook.com/v10.0/oauth/access_token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://graph.facebook.com/v10.0/me";
        }
    },
    GITHUB {
        @Override
        public String getAuthorizationUri() {
            return "https://github.com/login/oauth/authorize";
        }

        @Override
        public String getAccessTokenUri() {
            return "https://github.com/login/oauth/access_token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://api.github.com/user";
        }

        @Override
        public UserInfoConvertor userInfoConvertor() {
            return GithubUserInfo.GithubUserInfoConvertor;
        }
    },
    GITEE{
        public String getAuthorizationUri() {
            return "https://gitee.com/oauth/authorize";
        }

        @Override
        public String getAccessTokenUri() {
            return "https://gitee.com/oauth/token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://gitee.com/api/v5/user";
        }
    },
    WECHAT {
        @Override
        public String getAuthorizationUri() {
            return "https://open.weixin.qq.com/connect/qrconnect";
        }

        @Override
        public String getAccessTokenUri() {
            return "https://api.weixin.qq.com/sns/oauth2/access_token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://api.weixin.qq.com/sns/userinfo";
        }

        @Override
        public OAuthClient oauthClient(OAuthConfig oAuthConfig) {
            return new WechatOAuthClient(oAuthConfig);
        }
    },
    QQ {
        public String getAuthorizationUri() {
            return "https://graph.qq.com/oauth2.0/authorize";
        }

        public String getAccessTokenUri() {
            return "https://graph.qq.com/oauth2.0/token";
        }

        public String getUserInfoUri() {
            return "https://graph.qq.com/user/get_user_info";
        }
        @Override
        public OAuthClient oauthClient(OAuthConfig oAuthConfig) {
            return new QQOAuthClient(oAuthConfig);
        }
    }
    ;

    @Override
    public String getProvider() {
        return this.name().toLowerCase();
    }
}

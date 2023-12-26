package io.github.xc404.oauth.provider;

import io.github.xc404.oauth.config.OAuthProviderConfig;

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
        public String getTokenUri() {
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
    GITHUB {
        @Override
        public String getAuthorizationUri() {
            return "https://github.com/login/oauth/authorize";
        }

        @Override
        public String getTokenUri() {
            return "https://github.com/login/oauth/access_token";
        }

        @Override
        public String getUserInfoUri() {
            return "https://api.github.com/user";
        }
    };

    @Override
    public String getProvider() {
        return this.name().toLowerCase();
    }
}

package io.github.xc404.oauth.provider.qq;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.github.xc404.oauth.oidc.AddressStandard;
import io.github.xc404.oauth.oidc.ChinaAddressClaim;
import io.github.xc404.oauth.oidc.OidcUserInfoClaim;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.provider.wechat.WechatUserInfo;

import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 2:12 PM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html#UinonId">获取用户基本信息(UnionID机制)</a>
 */
public class QQUserInfo extends OidcUserInfoClaim
{
    public static final UserInfoConvertor QQUserInfoConvertor = QQUserInfo::new;

    private String openId;
    public static final String NAME = "nickname";

    public static final String PICTURE = "figureurl";


    public QQUserInfo(Map<String, Object> claims) {
        super(claims);
    }

    @Override
    public String getSubject() {
      return this.openId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String getFullName() {
        return this.getClaimAsString(NAME);
    }


    @Override
    public String getPicture() {
        return this.getClaimAsString(PICTURE);
    }
}

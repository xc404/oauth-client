package io.github.xc404.oauth.provider.wechat;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.github.xc404.oauth.oidc.AddressStandard;
import io.github.xc404.oauth.oidc.ChinaAddressClaim;
import io.github.xc404.oauth.oidc.OidcUserInfoClaim;
import io.github.xc404.oauth.oidc.UserInfoConvertor;
import io.github.xc404.oauth.provider.github.GithubUserInfo;

import java.util.Map;

/**
 * @Author chaox
 * @Date 12/27/2023 2:12 PM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html#UinonId">获取用户基本信息(UnionID机制)</a>
 */
public class WechatUserInfo extends OidcUserInfoClaim
{

    public static final String SUB = "openid";
    public static final String UNIONID = "unionid";
    public static final String NAME = "nickname";

    public static final String GENDER = "sex";

    public static final String PICTURE = "headimgurl";


    public static final UserInfoConvertor WechatUserInfoConvertor = WechatUserInfo::new;
    public WechatUserInfo(Map<String, Object> claims) {
        super(claims);
    }

    @Override
    public String getSubject() {
        String uniqueId = getClaimAsString(UNIONID);
        if( StringUtils.isNotBlank(uniqueId) ){
            return uniqueId;
        }
        return getClaimAsString(SUB);
    }

    @Override
    public String getFullName() {
        return this.getClaimAsString(NAME);
    }

    @Override
    public String getGender() {
        return this.getClaimAsString(GENDER);
    }

    @Override
    public String getPicture() {
        return this.getClaimAsString(PICTURE);
    }

    @Override
    public AddressStandard getAddress() {
        Object claim = getClaim(ChinaAddressClaim.PROVINCE_CLAIM);
        if(claim != null){
            return new ChinaAddressClaim(this.getClaims());
        }
        return null;
    }
}

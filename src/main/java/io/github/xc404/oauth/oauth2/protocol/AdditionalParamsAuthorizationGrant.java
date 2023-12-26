package io.github.xc404.oauth.oauth2.protocol;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import io.github.xc404.oauth.utils.ParameterMapUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author chaox
 * @Date 12/22/2023 9:56 AM
 */
public class AdditionalParamsAuthorizationGrant extends AuthorizationGrant
{
    private final Map<String, List<String>> additionalParams;

    private final AuthorizationGrant proxy;

    public AdditionalParamsAuthorizationGrant(AuthorizationGrant proxy, Map<String, Object> additionalParams) {
        super(proxy.getType());
        this.additionalParams = ParameterMapUtils.toParameterMap(additionalParams);
        this.proxy = proxy;
    }


    @Override
    public Map<String, List<String>> toParameters() {
        Map<String, List<String>> parameters = proxy.toParameters();
        parameters.putAll(proxy.toParameters());
        parameters.putAll(this.additionalParams);
        return parameters;
    }

    public Map<String, List<String>> getAdditionalParams() {
        return additionalParams;
    }

    public AuthorizationGrant getProxy() {
        return proxy;
    }
}

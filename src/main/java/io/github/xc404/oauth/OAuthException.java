package io.github.xc404.oauth;

import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.GeneralException;

import static com.nimbusds.oauth2.sdk.OAuth2Error.SERVER_ERROR_CODE;

/**
 * @Author chaox
 * @Date 12/22/2023 11:20 AM
 */
public class OAuthException extends RuntimeException
{
    private final ErrorObject errorObject;

    public OAuthException(Throwable cause) {
        super(cause);
        if( cause instanceof GeneralException ) {
            this.errorObject = ((GeneralException) cause).getErrorObject();
        } else {
            this.errorObject = new ErrorObject(SERVER_ERROR_CODE, cause.getMessage());
        }
    }

    public OAuthException(ErrorObject errorObject) {
        super(errorObject.getCode() + ":" + errorObject.getDescription());
        this.errorObject = errorObject;
    }

    public ErrorObject getErrorObject() {
        return errorObject;
    }
}

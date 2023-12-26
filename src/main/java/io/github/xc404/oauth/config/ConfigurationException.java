package io.github.xc404.oauth.config;

/**
 * @Author chaox
 * @Date 12/22/2023 7:24 PM
 */
public class ConfigurationException extends RuntimeException
{
    private final String field;

    public ConfigurationException(String field) {
        this.field = field;
    }

    public ConfigurationException(String message, String field) {
        super(message);
        this.field = field;
    }

    public ConfigurationException(String message, Throwable cause, String field) {
        super(message, cause);
        this.field = field;
    }

    public ConfigurationException(Throwable cause, String field) {
        super(cause);
        this.field = field;
    }

    public ConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String field) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

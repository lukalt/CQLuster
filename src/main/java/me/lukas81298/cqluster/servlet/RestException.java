package me.lukas81298.cqluster.servlet;

import lombok.Getter;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class RestException extends Exception {

    private final String message;
    @Getter
    private final int responseCode;

    public RestException( String message, int responseCode ) {
        super( message );
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return this.message;
    }
}

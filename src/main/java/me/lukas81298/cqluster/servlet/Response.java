package me.lukas81298.cqluster.servlet;

import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class Response {

    private final boolean success;
    private final Object data;

    public Response( boolean success ) {
        this.success = success;
        this.data = null;
    }
}

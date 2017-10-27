package me.lukas81298.cqluster.servlet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
@Getter
public class Session {

    private final String username;

    public boolean isAnonymous() {
        return this.username == null;
    }
}

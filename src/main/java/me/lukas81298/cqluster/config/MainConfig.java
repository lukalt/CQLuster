package me.lukas81298.cqluster.config;

import lombok.Getter;

/**
 * @author lukas
 * @since 27.10.2017
 */
@Getter
public class MainConfig {

    private int port = 8167;

    private boolean anonymousAccess = true;

    private String[] contactPoints = new String[] {
            "127.0.0.1"
    };

}

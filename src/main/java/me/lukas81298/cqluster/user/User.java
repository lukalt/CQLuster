package me.lukas81298.cqluster.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author lukas
 * @since 28.10.2017
 */
@AllArgsConstructor
@Getter
public class User {

    private String username;
    private UUID uuid;
    private String passwordHash;
}

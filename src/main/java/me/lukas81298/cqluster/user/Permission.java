package me.lukas81298.cqluster.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author lukas
 * @since 28.10.2017
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode( callSuper = false )
public class Permission {

    private String name;
    private String description;
    private String category;

}

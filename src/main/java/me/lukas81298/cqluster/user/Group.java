package me.lukas81298.cqluster.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * @author lukas
 * @since 28.10.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {

    private UUID uuid;
    private String name;
    private Set<String> permissions = new HashSet<>();

    public Group( UUID uuid ) {
        this.uuid = uuid;
    }

    public boolean isPermissionSet( String permission ) {
        return this.permissions.contains( permission );
    }

    public void setPermission( String permission, boolean set ) {
        if( set ) {
            this.permissions.add( permission );
        } else {
            this.permissions.remove( permission );
        }
    }

}

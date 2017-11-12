package me.lukas81298.cqluster.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
@Getter
public class Session {

    private final static long SESSION_DURATION = TimeUnit.DAYS.toMillis( 1 );

    private volatile UUID userId;
    private final long start;
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    private final transient GroupManager groupManager;
    private final transient UserManager userManager;

    public boolean isAnonymous() {
        return this.userId == null;
    }

    public boolean isValid() {
        return !this.isExpired();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > ( this.start + SESSION_DURATION );
    }

    public void logout() {
        this.userId = null;
    }

    public void setUser( UUID uuid ) {
        this.userId = uuid;
    }

    public synchronized boolean isPermissionSet( String permission ) {
        if( this.isAnonymous() ) {
            Group group = this.groupManager.getAnonymousGroup();
            if( group == null ) {
                return false;
            }
            return group.isPermissionSet( permission );
        }
        User user = this.userManager.getUser( this.userId );
        if( user != null && user.getGroup() != null ) {
            Group group = this.groupManager.getGroup( user.getGroup() );
            if( group != null ) {
                return group.isPermissionSet( permission );
            }
        }
        return false;
    }


}

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

    public boolean isAnonymous() {
        return this.userId == null;
    }

    public boolean isValid() {
        return !this.isAnonymous() && !this.isExpired();
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


}

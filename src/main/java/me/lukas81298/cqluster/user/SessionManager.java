package me.lukas81298.cqluster.user;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
public class SessionManager {

    private final Map<UUID,Session> activeSessions = new ConcurrentHashMap<>();
    private final UserManager userManager;
    private final GroupManager groupManager;

    public Session getSession( UUID sessionId ) {
        return this.activeSessions.computeIfAbsent( sessionId, new Function<UUID, Session>() {
            @Override
            public Session apply( UUID uuid ) {
                return new Session( System.currentTimeMillis(), groupManager, userManager );
            }
        } );
    }


}

package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.*;

import java.util.Map;
import java.util.UUID;

/**
 * @author lukas
 * @since 31.10.2017
 */
@RequiredArgsConstructor
public class UserGroupEndpoint extends BaseRestEndpoint {

    private final GroupManager groupManager;
    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String groupStr = params.get( "group" ), userStr = params.get( "user" );
        if( groupStr == null || userStr == null ) {
            throw new RestException( "group or user not specified", 400 );
        }
        try {
            UUID uuid = UUID.fromString( userStr );
            UUID groupId = UUID.fromString( groupStr );
            User user = this.userManager.getUser( uuid );
            if( user == null ) {
                throw new RestException( "User not found", 404 );
            }
            Group group = this.groupManager.getGroup( groupId );
            if( group == null ) {
                throw new RestException( "Group not found", 404 );
            }
            user.setGroup( groupId );
        } catch ( Exception e ) {
            throw new RestException( e.getMessage(), 400 );
        }

        return null;
    }
}

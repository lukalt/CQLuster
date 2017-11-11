package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Group;
import me.lukas81298.cqluster.user.GroupManager;
import me.lukas81298.cqluster.user.Session;

import java.util.Map;

/**
 * @author lukas
 * @since 11.11.2017
 */
@RequiredArgsConstructor
public class GroupPermissionEndpoint extends BaseRestEndpoint {

    private final GroupManager groupManager;


    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String groupName = params.get( "group" ), permission = params.get( "permission" );
        boolean value = "true".equals( params.get( "value" ) );
        if( groupName == null || groupName.isEmpty() || permission == null || permission.isEmpty() ) {
            throw new RestException( "Invalid parameters", 400 );
        }
        Group group = this.groupManager.getGroup( groupName );
        if( group == null ) {
            throw new RestException( "Group not found", 404 );
        }
        group.setPermission( permission, value );
        this.groupManager.save(); // save the changed group
        return true;
    }
}

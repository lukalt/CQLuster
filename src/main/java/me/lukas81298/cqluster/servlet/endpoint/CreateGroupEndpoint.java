package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Group;
import me.lukas81298.cqluster.user.GroupManager;
import me.lukas81298.cqluster.user.Session;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author lukas
 * @since 31.10.2017
 */
@RequiredArgsConstructor
public class CreateGroupEndpoint extends BaseRestEndpoint {

    private final GroupManager groupManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String name = params.get( "name" );
        if( name == null ) {
            throw new RestException( "No name specified", 400 );
        }
        if( name.isEmpty() ) {
            throw new RestException( "Please enter a name", 200 );
        }
        if( !StringUtils.isAlphanumeric( name ) ) {
            throw new RestException( "The name of the group must be alphanumeric", 200 );
        }
        synchronized ( this.groupManager ) {
            for ( Group group : this.groupManager.getGroups() ) {
                if( group.getName().equalsIgnoreCase( name ) ) {
                    throw new RestException( "A group with this name already exists", 200 );
                }
            }
            return this.groupManager.addGroup( name );
        }
    }

}

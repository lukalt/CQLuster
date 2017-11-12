package me.lukas81298.cqluster.servlet.endpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Group;
import me.lukas81298.cqluster.user.GroupManager;
import me.lukas81298.cqluster.user.Permission;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.util.Map;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
public class GroupsEndpoint extends BaseRestEndpoint {

    private final GroupManager groupManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        JsonObject object = new JsonObject();
        JsonArray permissions = new JsonArray();
        for ( Permission permission : this.groupManager.getPermissions() ) {
            permissions.add( JsonObjectBuilder.create( "name", permission.getName() )
                .add( "description", permission.getDescription() )
                .add( "category", permission.getCategory() ).build() );
        }
        object.add( "permissions", permissions );
        JsonArray groups = new JsonArray();
        for ( Group group : this.groupManager.getGroups() ) {
            JsonArray p = new JsonArray();
            for ( String s : group.getPermissions() ) {
                p.add( s );
            }
            groups.add( JsonObjectBuilder.create( "uuid", group.getUuid().toString() )
                .add( "name", group.getName() )
                .add( "permissions", p )
                .add( "anonymous", group.isAnonymous() ).build() );
        }
        object.add( "groups", groups );
        return object;
    }

}

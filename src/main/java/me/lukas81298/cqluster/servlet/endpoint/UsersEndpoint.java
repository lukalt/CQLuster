package me.lukas81298.cqluster.servlet.endpoint;

import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.User;
import me.lukas81298.cqluster.user.UserManager;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.util.Map;
import java.util.Objects;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
public class UsersEndpoint extends BaseRestEndpoint {

    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        JsonArray array = new JsonArray();
        for ( User user : this.userManager.dump() ) {
            array.add( JsonObjectBuilder.create( "name", user.getUsername() )
                .add( "uuid", user.getUuid().toString() ).add( "group", Objects.toString( user.getGroup() ) ).build() );
        }
        return array;
    }
}

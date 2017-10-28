package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.servlet.annotation.DisableAuth;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.User;
import me.lukas81298.cqluster.user.UserManager;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
@DisableAuth
public class UserInfoEndpoint extends BaseRestEndpoint {

    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        UUID u = session.getUserId();
        if( u != null ) {
            User user = this.userManager.getUser( u );
            if( user != null ) {
                return JsonObjectBuilder.create( "loggedIn", "true" )
                        .add( "username", user.getUsername() )
                        .add( "uid", user.getUuid().toString() )
                        .build();
            }
        }
        return JsonObjectBuilder.create( "loggedIn", "false" ).build();
    }
}

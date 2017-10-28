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

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
@DisableAuth
public class LoginEndpoint extends BaseRestEndpoint {

    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String username = params.get( "username" ), password = params.get( "password" );
        if( username == null || password == null ) {
            throw new RestException( "Missing username or password", 400 );
        }
        User user = this.userManager.matchUser( username, password );
        if( user != null ) {
            session.setUser( user.getUuid() );
        }
        return user == null ? ( JsonObjectBuilder.create( "success", "false" ).build() ) : JsonObjectBuilder.create( "success", "true" )
                .add( "username", username ).add( "uid", user.getUuid().toString() ).build();
    }
}

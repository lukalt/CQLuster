package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.UserManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
public class CreateUserEndpoint extends BaseRestEndpoint {

    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String username = params.get( "username" );
        String password = params.get( "password" );
        String role = params.get( "role" ); // ignored for now
        if( username == null || password == null ) {
            throw new RestException( "Missing username or password", 400 );
        }
        if( password.isEmpty() ) {
            throw new RestException( "The password cannot be empty", 200 );
        }
        if( !StringUtils.isAlphanumeric( username ) ) {
            throw new RestException( "The username has to be alphanumeric", 200 );
        }
        if( username.length() < 4 ) {
            throw new RestException( "The username has to be at least 4 characters long", 200 );
        }
        synchronized ( this.userManager ) {
            if( this.userManager.getUser( username ) != null ) {
                throw new RestException( "An user with the given name already exists", 200 );
            }
            return this.userManager.addUser( username, password );
        }
    }
}

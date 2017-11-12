package me.lukas81298.cqluster.servlet.endpoint;

import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;

import java.util.Map;

/**
 * @author lukas
 * @since 28.10.2017
 */
public class LogoutEndpoint extends BaseRestEndpoint {

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        session.logout(); // logout
        return true;
    }
}

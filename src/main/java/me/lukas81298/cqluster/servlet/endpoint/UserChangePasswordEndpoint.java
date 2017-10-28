package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.UserManager;

import java.util.Map;

/**
 * @author lukas
 * @since 28.10.2017
 */
@RequiredArgsConstructor
public class UserChangePasswordEndpoint extends BaseRestEndpoint {

    private final UserManager userManager;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        return null;
    }
}

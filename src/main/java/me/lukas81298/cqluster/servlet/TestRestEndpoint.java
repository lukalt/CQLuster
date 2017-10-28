package me.lukas81298.cqluster.servlet;

import me.lukas81298.cqluster.user.Session;

import java.util.Map;

/**
 * @author lukas
 * @since 28.10.2017
 */
public class TestRestEndpoint extends BaseRestEndpoint {

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        Long l = (Long) session.getData().getOrDefault( "i", 0L );
        session.getData().put( "i", l + 1 );
        return l;
    }
}

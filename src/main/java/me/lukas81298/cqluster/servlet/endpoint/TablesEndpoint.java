package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.ClusterConnection;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.servlet.Session;

import java.util.Map;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class TablesEndpoint extends BaseRestEndpoint {

    private final ClusterConnection clusterConnection;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        if ( !params.containsKey( "keyspace" ) ) {
            throw new RestException( "keyspace is not defined", 400 );
        }
        return this.clusterConnection.getTables( params.get( "keyspace" ) );
    }
}

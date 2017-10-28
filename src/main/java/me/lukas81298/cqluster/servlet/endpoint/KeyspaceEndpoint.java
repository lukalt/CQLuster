package me.lukas81298.cqluster.servlet.endpoint;

import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.ClusterConnection;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;

import java.util.Map;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class KeyspaceEndpoint extends BaseRestEndpoint {

    private final ClusterConnection clusterConnection;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        if( "1".equals( params.get( "advanced" ) ) ) {
            return this.clusterConnection.getKeyspaceMap();
        }
        return this.clusterConnection.getKeyspaceList();
    }

}

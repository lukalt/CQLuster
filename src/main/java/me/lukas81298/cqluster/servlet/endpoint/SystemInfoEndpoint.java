package me.lukas81298.cqluster.servlet.endpoint;

import com.datastax.driver.core.Host;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.ClusterConnection;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.servlet.Session;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.util.Map;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class SystemInfoEndpoint extends BaseRestEndpoint {

    private final ClusterConnection clusterConnection;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        JsonArray array = new JsonArray();
        for ( Host host : clusterConnection.getCluster().getMetadata().getAllHosts() ) {
            array.add( JsonObjectBuilder.create( "datacenter", host.getDatacenter() )
                .add( "rack", host.getRack() )
                .add( "state", host.getState() )
                .add( "listenAddress", host.getListenAddress().getHostAddress() )
                .add( "broadcastAddress", host.getBroadcastAddress().getHostAddress() )
                .add( "version", host.getCassandraVersion().toString() )
                .build() );
        }
        return JsonObjectBuilder.create( "version", "1.0.0" )
                .add( "name", clusterConnection.getCluster().getClusterName() )
                .add( "hosts", array ).build();
    }
}

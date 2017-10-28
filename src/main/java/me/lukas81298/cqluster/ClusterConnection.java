package me.lukas81298.cqluster;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class ClusterConnection {

    @Getter
    private final Cluster cluster;

    public ClusterConnection( String[] contactPoints ) {
        this.cluster = Cluster.builder().addContactPoints( contactPoints ).build();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
            @Override
            public void run() {
                if( !cluster.isClosed() ) {
                    cluster.close();
                }
            }
        } ) );
    }

    public Session getSession( String keyspace ) {
        return this.cluster.connect( keyspace );
    }

    public List<String> getTables( String keyspace ) {
        KeyspaceMetadata keyspaceMetadata = this.cluster.getMetadata().getKeyspace( keyspace );
        if ( keyspaceMetadata == null ) {
            return null;
        }
        return keyspaceMetadata.getTables().stream().map( t -> t.getName() ).collect( Collectors.toList() );
    }

    public List<String> getKeyspaceList() {
        return cluster.getMetadata().getKeyspaces().stream().map( KeyspaceMetadata::getName ).collect( Collectors.toList() );
    }

    public TableMetadata getTableMeta( String keyspace, String table ) {
        KeyspaceMetadata keyspaceMetadata = this.cluster.getMetadata().getKeyspace( keyspace );
        if( keyspaceMetadata == null ) {
            return null;
        }
        return keyspaceMetadata.getTable( table );
    }

    public Map<String, List<String>> getKeyspaceMap() {
        Map<String, List<String>> map = new HashMap<>();
        for ( KeyspaceMetadata keyspaceMetadata : this.cluster.getMetadata().getKeyspaces() ) {
            map.put( keyspaceMetadata.getName(), keyspaceMetadata.getTables().stream().map( t -> t.getName() ).collect( Collectors.toList() ) );
        }
        return map;
    }

}

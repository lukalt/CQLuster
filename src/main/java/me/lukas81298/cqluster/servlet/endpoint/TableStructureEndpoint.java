package me.lukas81298.cqluster.servlet.endpoint;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.IndexMetadata;
import com.datastax.driver.core.TableMetadata;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.ClusterConnection;
import me.lukas81298.cqluster.servlet.BaseRestEndpoint;
import me.lukas81298.cqluster.servlet.RestException;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.util.Map;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class TableStructureEndpoint extends BaseRestEndpoint {

    private final ClusterConnection clusterConnection;

    @Override
    public Object execute( Session session, Map<String, String> params ) throws RestException {
        String keyspace = params.get( "keyspace" ), tableName = params.get( "table" );
        if( keyspace == null || tableName == null ) {
            throw new RestException( "Missing parameteter", 400 );
        }
        TableMetadata table = this.clusterConnection.getTableMeta( keyspace, tableName );
        if( table == null ) {
            throw new RestException( "Keyspace or table was not found", 404 );
        }

        JsonArray columns = new JsonArray();

        for ( ColumnMetadata columnMetadata : table.getColumns() ) {
            columns.add( JsonObjectBuilder.create( "name", columnMetadata.getName() )
                .add( "type", columnMetadata.getType().getName().name() )
                .build() );
        }

        JsonArray indices = new JsonArray();

        for( IndexMetadata indexMetadata : table.getIndexes() ) {
            indices.add( JsonObjectBuilder.create( "name", indexMetadata.getName() )
            .add( "type", indexMetadata.getIndexClassName() )
            .add( "target", indexMetadata.getTarget() )
            .build() );
        }

        return JsonObjectBuilder.create( "table", table.getName() )
                .add( "keyspace", keyspace )
                .add( "columns", columns )
                .add( "indices", indices )
                .build();
    }
}

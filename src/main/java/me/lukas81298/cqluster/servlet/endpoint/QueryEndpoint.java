package me.lukas81298.cqluster.servlet.endpoint;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
public class QueryEndpoint extends BaseRestEndpoint {

    private final ClusterConnection clusterConnection;

    @Override
    public Object execute( Session sessionName, Map<String, String> params ) throws RestException {
        String query = params.get( "query" ), keyspace = params.get( "keyspace" );
        if( query == null || keyspace == null ) {
            throw new RestException( "query or keyspace not specified", 400 );
        }
        com.datastax.driver.core.Session session = this.clusterConnection.getSession( keyspace );
        if( session == null ) {
            throw new RestException( "keyspace not found", 404 );
        }
        try {
            com.datastax.driver.core.ResultSet resultSet = session.execute( query );
            JsonArray response = new JsonArray();
            for ( Row row : resultSet ) {
                JsonObject object = new JsonObject();
                ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
                int i = 0;
                for ( ColumnDefinitions.Definition columnDefinition : columnDefinitions ) {
                    object.addProperty( columnDefinition.getName(), row.getObject( i ).toString() );
                    i++;
                }
                response.add( object );
            }
            return response;
        } catch ( Exception e ) {
            throw new RestException( e.getMessage(), 200 );
        }
    }

}

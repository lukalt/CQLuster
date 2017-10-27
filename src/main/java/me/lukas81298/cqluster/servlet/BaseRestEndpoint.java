package me.lukas81298.cqluster.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.lukas81298.cqluster.util.JsonObjectBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lukas
 * @since 27.10.2017
 */
public abstract class BaseRestEndpoint implements HttpHandler {

    private final static Gson gson = new Gson();
    protected final static Session ANONYMOUS_SESSION = new Session( null );

    public abstract Object execute( Session session, Map<String, String> params ) throws RestException ;

    @Override
    public void handle( HttpExchange httpExchange ) throws IOException {
        Map<String, String> params = queryToMap( httpExchange.getRequestURI().getQuery() );
        try {
            Object result = this.execute( ANONYMOUS_SESSION, params );
            writeJson( httpExchange, new Response( true, result ), 200 );
        } catch ( RestException e ) {
            writeJson( httpExchange, new Response( false, e.getErrorMessage() ), e.getResponseCode() );
        }
    }

    protected static Map<String, String> queryToMap( String query ) {
        if( query == null ) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        for ( String param : query.split( "&" ) ) {
            String pair[] = param.split( "=" );
            if ( pair.length > 1 ) {
                result.put( pair[0], pair[1] );
            } else {
                result.put( pair[0], "" );
            }
        }
        return result;
    }

    private void writeJson( HttpExchange httpExchange, Object object, int responseCode ) throws IOException {
        String response = gson.toJson( object );
        writeRawResponse( httpExchange, response, responseCode );
    }

    private void writeRawResponse( HttpExchange httpExchange, String string, int responseCode ) throws IOException {
        httpExchange.sendResponseHeaders( responseCode, string.length() );
        OutputStream os = httpExchange.getResponseBody();
        os.write( string.getBytes() );
        os.close();
    }

}

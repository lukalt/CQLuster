package me.lukas81298.cqluster.servlet;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.lukas81298.cqluster.servlet.annotation.DisableAuth;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.SessionManager;
import me.lukas81298.cqluster.util.SneakyThrow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lukas
 * @since 27.10.2017
 */
public abstract class BaseRestEndpoint implements HttpHandler {

    private final static Gson gson = new Gson();
    private final static SessionManager sessionManager = new SessionManager();

    public abstract Object execute( Session session, Map<String, String> params ) throws RestException;

    @Override
    public void handle( HttpExchange httpExchange ) throws IOException {
        try {
            Map<String, String> params = queryToMap( httpExchange );
            try {
                Session session = getSession( httpExchange );
                if( !session.isValid() && !this.getClass().isAnnotationPresent( DisableAuth.class ) ) {
                    throw new RestException( "Access denied", 403 );
                }
                Object result = this.execute( session, params );
                writeJson( httpExchange, new Response( true, result ), 200 );
            } catch ( RestException e ) {
                writeJson( httpExchange, new Response( false, e.getErrorMessage() ), e.getResponseCode() );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            writeRawResponse( httpExchange, "<h1>500 Internal Server Error</h1><p>Please contact the webmaster</p>", 500 );
        }
    }

    protected Session getSession( HttpExchange exchange ) {
        List<HttpCookie> cookies;
        String cookieString = exchange.getRequestHeaders().getFirst( "Cookie" );
        if ( cookieString != null ) {
            cookies = HttpCookie.parse( cookieString );
        } else {
            cookies = Collections.emptyList();
        }
        cookies = new ArrayList<>( cookies );
        for ( HttpCookie cookie : cookies ) {
            if ( cookie.getName().equals( "SESSION_ID" ) ) {
                return sessionManager.getSession( UUID.fromString( cookie.getValue() ) );
            }
        }
        UUID sessionId = UUID.randomUUID();
        cookies.add( new HttpCookie( "SESSION_ID", sessionId.toString() ) );
        exchange.getResponseHeaders().set( "Set-Cookie", String.join( ";", cookies.stream().map( new Function<HttpCookie, String>() {
            @Override
            public String apply( HttpCookie httpCookie ) {
                return httpCookie.getName() + "=" + httpCookie.getValue() + "";
            }
        } ).collect( Collectors.toList() ) ) );
        return sessionManager.getSession( sessionId );
    }

    protected static Map<String, String> queryToMap( HttpExchange httpExchange ) {

        String query = null;
        if ( httpExchange.getRequestMethod().equalsIgnoreCase( "get" ) ) {
            query = httpExchange.getRequestURI().getQuery();
        } else if ( httpExchange.getRequestMethod().equalsIgnoreCase( "post" ) ) {
            try( BufferedReader reader = new BufferedReader( new InputStreamReader( httpExchange.getRequestBody(), Charsets.UTF_8 ) ) ) {
                query = reader.readLine();
            } catch ( IOException e ) {
                return SneakyThrow.throwSilently( e );
            }
        }

        if ( query == null ) {
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

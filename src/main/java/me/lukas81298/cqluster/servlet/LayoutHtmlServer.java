package me.lukas81298.cqluster.servlet;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.lukas81298.cqluster.CqlCluster;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.util.SneakyThrow;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class LayoutHtmlServer implements HttpHandler {

    private final static java.util.function.Supplier<String> layoutSupplier = Suppliers.memoizeWithExpiration( new Supplier<String>() {
        @Override
        public String get() {
            try {
                File file = new File( "html/layout.html" );
                if( !file.exists() ) {
                    return "404 Not Found";
                }
                return new String( Files.readAllBytes( file.toPath() ), Charsets.UTF_8 );
            } catch ( IOException e ) {
                return SneakyThrow.throwSilently( e );
            }
        }
    }, 1, TimeUnit.MINUTES );

    private final File file;
    private final BiFunction<Session,Map<String,String>,Map<String,String>> parseFunction;
    private final java.util.function.Supplier<String> fileSupplier = Suppliers.memoizeWithExpiration( new Supplier<String>() {
        @Override
        public String get() {
            try {
                if( !file.exists() ) {
                    return "404 Not Found";
                }
                return new String( Files.readAllBytes( file.toPath() ), Charsets.UTF_8 );
            } catch ( IOException e ) {
                return SneakyThrow.throwSilently( e );
            }
        }
    }, 1, TimeUnit.MINUTES );

    public LayoutHtmlServer( File file ) {
        this( file, null );
    }

    public LayoutHtmlServer( File file, BiFunction<Session, Map<String, String>, Map<String, String>> parseFunction) {
        this.file = file;
        this.parseFunction = parseFunction;
    }


    @Override
    public void handle( HttpExchange httpExchange ) throws IOException {
        try {
            String layout = layoutSupplier.get();
            if( layout.contains( "{{BODY}}" ) ) {
                layout = layout.replace( "{{BODY}}", fileSupplier.get() );
            }
            Map<String, String> params = BaseRestEndpoint.queryToMap( httpExchange );
            if( this.parseFunction != null ) {
                Map<String, String> apply = this.parseFunction.apply( new Session( -1L, CqlCluster.getInstance().getGroupManager(), CqlCluster.getInstance().getUserManager() ), params );
                if( apply == null ) {
                    layout = "400 Bad Request";
                } else {
                    for ( Map.Entry<String, String> entry : apply.entrySet() ) {
                        layout = layout.replace( "{{#" + entry.getKey() + "}}", entry.getValue() );
                    }
                }
            }
            byte[] b = layout.getBytes( Charsets.UTF_8 );
            httpExchange.sendResponseHeaders( 200, b.length );
            httpExchange.getResponseHeaders().add( "Content-Type", "text/html" );
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write( b );
            outputStream.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


}

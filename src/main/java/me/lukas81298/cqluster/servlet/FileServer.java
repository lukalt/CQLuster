package me.lukas81298.cqluster.servlet;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.RequiredArgsConstructor;
import me.lukas81298.cqluster.util.SneakyThrow;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * @author lukas
 * @since 27.10.2017
 */
@RequiredArgsConstructor
public class FileServer implements HttpHandler {

    private final File file;
    private final java.util.function.Supplier<byte[]> contents = Suppliers.memoizeWithExpiration( new Supplier<byte[]>() {
        @Override
        public byte[] get() {
            try {
                return Files.readAllBytes( file.toPath() );
            } catch ( IOException e ) {
                return SneakyThrow.throwSilently( e );
            }
        }
    }, 1, TimeUnit.MINUTES );

    @Override
    public void handle( HttpExchange httpExchange ) throws IOException {
        byte[] b = this.contents.get();
        httpExchange.sendResponseHeaders( 200, b.length );
        httpExchange.getResponseHeaders().add( "Content-Type", "text/html" );
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write( b );
        outputStream.close();
    }

}

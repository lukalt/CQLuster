package me.lukas81298.cqluster.config;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lukas81298.cqluster.util.SneakyThrow;

import java.io.*;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class JsonConfig {

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static <K> K load( File file, Class<K> clazz ) throws IOException {
        if( !file.exists() ) {
            try {
                K k = clazz.newInstance();
                gson.toJson( k, clazz, new PrintWriter( file ) );
                return k;
            } catch ( InstantiationException | IllegalAccessException e ) {
                SneakyThrow.throwSilently( e );
            }
        }
        return gson.fromJson( new InputStreamReader( new FileInputStream( file ), Charsets.UTF_8 ), clazz );
    }

}

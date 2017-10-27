package me.lukas81298.cqluster;

import me.lukas81298.cqluster.config.JsonConfig;
import me.lukas81298.cqluster.config.MainConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class Bootstrap {

    public static void main( String[] args ) {

        File configFolder = new File( "config" );

        if( !configFolder.exists() || !configFolder.isDirectory() ) {
            if( !configFolder.mkdir() ) {
                System.out.println( "Could not create config folder " + configFolder.getAbsolutePath() );
            }
        }
        try {
            MainConfig config = JsonConfig.load( new File( configFolder, "main.json" ), MainConfig.class );
            new CqlCluster( config );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}

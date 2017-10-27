package me.lukas81298.cqluster;

import me.lukas81298.cqluster.config.MainConfig;
import me.lukas81298.cqluster.servlet.Servlet;

import java.io.IOException;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class CqlCluster {

    private final MainConfig mainConfig;
    private final ClusterConnection clusterConnection;
    private final Servlet servlet;

    public CqlCluster( MainConfig mainConfig ) throws IOException {
        this.mainConfig = mainConfig;
        this.clusterConnection = new ClusterConnection( mainConfig.getContactPoints() );
        this.servlet = new Servlet( mainConfig.getPort(), clusterConnection );
    }

}

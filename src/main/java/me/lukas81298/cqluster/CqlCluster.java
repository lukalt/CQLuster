package me.lukas81298.cqluster;

import me.lukas81298.cqluster.config.MainConfig;
import me.lukas81298.cqluster.servlet.Servlet;
import me.lukas81298.cqluster.user.UserManager;

import java.io.IOException;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class CqlCluster {

    private final MainConfig mainConfig;
    private final ClusterConnection clusterConnection;
    private final Servlet servlet;
    private final UserManager userManager;

    public CqlCluster( MainConfig mainConfig ) throws IOException {
        this.mainConfig = mainConfig;
        this.clusterConnection = new ClusterConnection( mainConfig.getContactPoints() );
        this.userManager = new UserManager();
        this.servlet = new Servlet( mainConfig.getPort(), clusterConnection, userManager );
    }

}

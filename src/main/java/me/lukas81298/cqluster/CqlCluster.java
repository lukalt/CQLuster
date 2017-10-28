package me.lukas81298.cqluster;

import lombok.Getter;
import me.lukas81298.cqluster.config.MainConfig;
import me.lukas81298.cqluster.servlet.Servlet;
import me.lukas81298.cqluster.user.GroupManager;
import me.lukas81298.cqluster.user.UserManager;

import java.io.IOException;

/**
 * @author lukas
 * @since 27.10.2017
 */
@Getter
public class CqlCluster {

    private final MainConfig mainConfig;
    private final ClusterConnection clusterConnection;
    private final Servlet servlet;
    private final UserManager userManager;
    private final GroupManager groupManager;

    public CqlCluster( MainConfig mainConfig ) throws IOException {
        this.mainConfig = mainConfig;
        this.clusterConnection = new ClusterConnection( mainConfig.getContactPoints() );
        this.userManager = new UserManager();
        this.groupManager = new GroupManager();
        this.servlet = new Servlet( mainConfig.getPort(), clusterConnection, userManager, groupManager );

    }

}

package me.lukas81298.cqluster.servlet;

import com.sun.net.httpserver.HttpServer;
import me.lukas81298.cqluster.ClusterConnection;
import me.lukas81298.cqluster.servlet.endpoint.*;
import me.lukas81298.cqluster.user.Session;
import me.lukas81298.cqluster.user.UserManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class Servlet {

    public Servlet( int port, ClusterConnection clusterConnection, UserManager userManager ) throws IOException {
        HttpServer httpServer = HttpServer.create( new InetSocketAddress( port ), 0);
        httpServer.createContext( "/", new LayoutHtmlServer( new File( "html/index.html" ) ) );
        httpServer.createContext( "/keyspace", new LayoutHtmlServer( new File( "html/keyspace.html" ), new BiFunction<Session, Map<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply( Session session, Map<String, String> source ) {
                Map<String,String> result = new HashMap<>();
                if( source.containsKey( "k" ) ) {
                    result.put( "KEYSPACE_NAME", source.get( "k" ) );
                    result.put( "BC_PAGE_TITLE", source.get( "k" ) );
                } else {
                    result.put( "BC_PAGE_TITLE", "Keyspace" );
                }
                return result;
            }
        } ) );
        httpServer.createContext( "/table", new LayoutHtmlServer( new File( "html/table.html" ), new BiFunction<Session, Map<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply( Session session, Map<String, String> source ) {
                Map<String,String> result = new HashMap<>();
                if( source.containsKey( "keyspace" ) ) {
                    result.put( "KEYSPACE_NAME", source.get( "keyspace" ) );
                } else {
                    return null;
                }
                if( source.containsKey( "table" ) ) {
                    result.put( "TABLE_NAME", source.get( "table" ) );
                } else {
                    return null;
                }
                return result;
            }
        } ) );
        httpServer.createContext( "/query", new LayoutHtmlServer( new File( "html/query.html" ), new BiFunction<Session, Map<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply( Session session, Map<String, String> stringStringMap ) {
                return Collections.singletonMap( "SELECTED_KEYSPACE", stringStringMap.getOrDefault( "keyspace", "" ) );
            }
        } ) );
        httpServer.createContext( "/create-table", new LayoutHtmlServer( new File( "html/create-table.html" ), new BiFunction<Session, Map<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> apply( Session session, Map<String, String> stringStringMap ) {
                return Collections.singletonMap( "SELECTED_KEYSPACE", stringStringMap.getOrDefault( "keyspace", "" ) );
            }
        } ) );
        httpServer.createContext( "/create-keyspace", new LayoutHtmlServer( new File( "html/create-keyspace.html" ) ) );
        httpServer.createContext( "/system", new LayoutHtmlServer( new File( "html/system.html" ) ) );
        httpServer.createContext( "/system/info", new LayoutHtmlServer( new File( "html/system-info.html" ) ) );
        httpServer.createContext( "/system/users", new LayoutHtmlServer( new File( "html/users.html" ) ) );
        httpServer.createContext( "/login", new FileServer( new File( "html/login.html" ) ) );

        // api endpoints
        httpServer.createContext( "/api/user-info", new UserInfoEndpoint( userManager ) );
        httpServer.createContext( "/api/login", new LoginEndpoint( userManager ) );
        httpServer.createContext( "/api/logout", new LogoutEndpoint() );
        httpServer.createContext( "/api/test", new TestRestEndpoint() );
        httpServer.createContext( "/api/system/info", new SystemInfoEndpoint( clusterConnection ) );
        httpServer.createContext( "/api/keyspaces", new KeyspaceEndpoint( clusterConnection ) );
        httpServer.createContext( "/api/tables", new TablesEndpoint( clusterConnection ) );
        httpServer.createContext( "/api/query", new QueryEndpoint( clusterConnection ) );
        httpServer.createContext( "/api/table-structure", new TableStructureEndpoint( clusterConnection ) );

        httpServer.createContext( "/api/users/list", new UsersEndpoint( userManager ) );
        httpServer.createContext( "/api/users/create", new CreateUserEndpoint( userManager ) );
        httpServer.createContext( "/api/users/change-password", new UserChangePasswordEndpoint( userManager ) );

        httpServer.start();
    }

}

package me.lukas81298.cqluster.user;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lukas
 * @since 28.10.2017
 */
public class GroupManager {

    @Getter
    private List<Group> groups = new ArrayList<>();
    private final Map<String, Group> byName = new ConcurrentHashMap<>();
    @Getter
    private final List<Permission> permissions = Arrays.asList(
            new Permission( "login", "Allows to log in", "General" ),
            new Permission( "system-info", "Allows displaying system information", "General" ),
            new Permission( "user-create", "Allows creating users", "User Management" ),
            new Permission( "user-change-password", "Allows changing passwords of any user", "User Management" ),
            new Permission( "user-delete", "Allows deleting any user", "User Management" ),
            new Permission( "insert-query", "Allows executing INSERT or UPDATE cql queries", "Queries" ),
            new Permission( "select-query", "Allows executing SELECT cql queries", "Queries" ),
            new Permission( "describe-query", "Allows executing DESCRIBE cql queries", "Queries" ),
            new Permission( "create-table", "Allows executing CREATE TABLE queries and creating tables with the ui", "Queries" ),
            new Permission( "create-keyspace", "Allows executing CREATE KEYSPACE queries and creating keyspaces with the ui", "Queries" )
    );

    private final File file = new File( "groups.json" );
    private final Gson gson = new Gson();

    public GroupManager() {
        this.load();
    }

    public Group addGroup( String name ) {
        Group group = new Group( UUID.randomUUID(), name, new HashSet<>(), false );
        this.groups.add( group );
        this.byName.put( name, group );
        save();
        return group;
    }

    public Permission getPermissionByName( String name ) {
        for ( Permission permission : this.permissions ) {
            if ( name.equals( permission.getName() ) ) {
                return permission;
            }
        }
        return null;
    }

    public List<Permission> getPermissionsByCategory( String category ) {
        List<Permission> l = new ArrayList<>();
        for ( Permission permission : this.permissions ) {
            if ( category.equals( permission.getCategory() ) ) {
                l.add( permission );
            }
        }
        return l;
    }

    public Group getGroup( String name ) {
        return this.byName.get( name );
    }

    public Group getAnonymousGroup() {
        for ( Group group : this.groups ) {
            if( group.isAnonymous() ) {
                return group;
            }
        }
        return null;
    }

    public Group getGroup( UUID uuid ) {
        for ( Group group : this.groups ) {
            if ( group.getUuid().equals( uuid ) ) {
                return group;
            }
        }
        return null;
    }

    private void load() {
        this.byName.clear();
        if ( this.file.exists() ) {
            try {
                this.groups = new ArrayList<>( this.gson.fromJson( new InputStreamReader( new FileInputStream( file ), Charsets.UTF_8 ), new TypeToken<ArrayList<Group>>() {
                }.getType() ) );
                for ( Group group : this.groups ) {
                    this.byName.put( group.getName(), group );
                }
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            }
        } else {
            this.groups.add( new Group( UUID.randomUUID(), "admin", this.permissions.stream().map( p -> p.getName() ).collect( Collectors.toSet() ), false ) );

            Set<String> perm = new HashSet<>();
            perm.add( "login" );
            for ( Permission queries : this.getPermissionsByCategory( "Queries" ) ) {
                perm.add( queries.getName() );
            }
            this.groups.add( new Group( UUID.randomUUID(), "default", perm, false ) );
            this.groups.add( new Group( UUID.randomUUID(), "anonymous", new HashSet<>(), true ) );
            this.save();
        }
    }

    public void save() {
        try ( PrintWriter printWriter = new PrintWriter( file ) ) {
            printWriter.write( this.gson.toJson( this.groups ) );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

}

package me.lukas81298.cqluster.user;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lukas
 * @since 28.10.2017
 */
public class UserManager {


    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final File file = new File( "users.json" );
    private final Gson gson = new Gson();
    private final GroupManager groupManager;

    public List<User> dump() {
        return new ArrayList<>( this.users.values() );
    }

    public UserManager( GroupManager groupManager ) {
        this.groupManager = groupManager;
        this.load();
    }

    public User getUser( UUID uuid ) {
        return this.users.get( uuid );
    }

    public User getUser( String name ) {
        for ( User user : this.users.values() ) {
            if ( user.getUsername().equals( name ) ) {
                return user;
            }
        }
        return null;
    }

    public User matchUser( String username, String password ) {
        String hash = DigestUtils.sha512Hex( password );
        for ( Map.Entry<UUID, User> entry : this.users.entrySet() ) {
            if ( entry.getValue().getUsername().equals( username ) && entry.getValue().getPasswordHash().equals( hash ) ) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void load() {
        if ( this.file.exists() ) {

            try {
                Map<UUID, User> map = this.gson.fromJson( new InputStreamReader( new FileInputStream( this.file ), Charsets.UTF_8 ), new TypeToken<Map<UUID, User>>() {
                }.getType() );
                this.users.putAll( map );
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            }
        } else {
            UUID uuid = UUID.randomUUID();
            this.users.put( uuid, new User( "admin", uuid, DigestUtils.sha512Hex( "1234" ), this.groupManager.getGroup( "admin" ).getUuid() ) );
            save();
        }
    }

    private void save() {
        try {
            try ( FileWriter fileWriter = new FileWriter( this.file ) ) {
                fileWriter.write( this.gson.toJson( this.users ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public UUID addUser( String username, String password ) {
        UUID id = UUID.randomUUID();
        this.users.put( id, new User( username, id, DigestUtils.sha512Hex( password ), this.groupManager.getGroup( "admin" ).getUuid() ) );
        this.save();
        return id;
    }
}

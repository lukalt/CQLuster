package me.lukas81298.cqluster.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author lukas
 * @since 15.08.2017
 */
public class JsonObjectBuilder {

    private final JsonObject jsonObject = new JsonObject();

    public static JsonObjectBuilder create( String key, String value ) {
        return new JsonObjectBuilder().add( key, value );
    }

    public JsonObjectBuilder add( String key, JsonElement element ) {
        this.jsonObject.add( key, element );
        return this;
    }

    public JsonObjectBuilder add( String key, boolean value ) {
        return add( key, Boolean.toString( value ) );
    }

    public JsonObjectBuilder add( String key, int value ) {
        return add( key, Integer.toString( value ) );
    }

    public JsonObjectBuilder add( String key, double value ) {
        return add( key, Double.toString( value ) );
    }

    public JsonObjectBuilder add( String key, String value ) {
        this.jsonObject.addProperty( key, value );
        return this;
    }

    public JsonObject build() {
        return this.jsonObject;
    }
}

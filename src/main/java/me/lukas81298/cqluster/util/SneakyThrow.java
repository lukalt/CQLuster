package me.lukas81298.cqluster.util;

/**
 * @author lukas
 * @since 27.10.2017
 */
public class SneakyThrow {

    public static <K> K throwSilently( Throwable t ) {
        throw new RuntimeException( t );
    }

}

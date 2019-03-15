package bgu.spl181.net.srv.bidi;

import java.io.Closeable;

public interface Server<T> extends Closeable {

    /**
     * The main loop of the server, Starts listening and handling new clients.
     */
    void serve();
    
    /**
     * Inserts a given handler to the connections map with a unique id.
     * Implementing classes must have a number to support the id.
     */
    int insert_to_connections(ConnectionHandler<T> handler);
}

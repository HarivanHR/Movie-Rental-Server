package bgu.spl181.net.api.bidi;


import bgu.spl181.net.srv.bidi.ConnectionHandler;


public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);
    
    void add_to_map(int connectionId, ConnectionHandler<T> handler);
}

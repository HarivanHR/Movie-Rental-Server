package bgu.spl181.net.api.bidi;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class ImplementingConnections<T> implements Connections<T> {
	private final ConcurrentHashMap<Integer, ConnectionHandler<T>> connections_map;
	
	public ImplementingConnections() {
		connections_map = new ConcurrentHashMap<>();
	}
	
	@Override
	public boolean send(int connectionId, T msg) {
		if (!connections_map.containsKey(connectionId)){ //Doesn't exist.
			return false;
		}
		connections_map.get(connectionId).send(msg);
		return true;
	}

	@Override
	public void broadcast(T msg) {
		Enumeration<ConnectionHandler<T>> active_users =  connections_map.elements();
		while (active_users.hasMoreElements()){
			active_users.nextElement().send(msg);
		}
		
	}
	
	@Override
	public void disconnect(int connectionId) {
		ConnectionHandler<T> connection_to_be_closed = connections_map.remove(connectionId);
		if (connection_to_be_closed == null) {
			System.out.println("Connection doesn't exist in system");
			return;
		}
		try {
			connection_to_be_closed.close();
		} catch (IOException e) {
			System.out.println("An error was encountered while trying to close the connection");
			e.printStackTrace();
		}
	}	
	
	public void add_to_map(int connectionId, ConnectionHandler<T> handler_of_the_id){
		connections_map.putIfAbsent(connectionId, handler_of_the_id);	
	}
}

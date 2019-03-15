package bgu.spl181.net.srv.bidi;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.api.bidi.ImplementingConnections;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    protected final int _port;
    protected final Supplier<BidiMessagingProtocol<T>> _protocolFactory;
    protected final Supplier<MessageEncoderDecoder<T>> _encdecFactory;
    protected final Connections<T> _connections;
    protected final AtomicInteger _id_supplier;
    protected ServerSocket _sock;

    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory) {
    	_connections = new ImplementingConnections<>();
    	_id_supplier = new AtomicInteger(0);
        _port = port;
        _protocolFactory = protocolFactory;
        _encdecFactory = encdecFactory;
		_sock = null;
    }



	@Override
    public void close() throws IOException {
		if (_sock != null)
			_sock.close();
    }
   
    public Connections<T> get_connections(){
    	return _connections;
    }

    /**
     * Called by serve in order to insert a new handler to the map.
     * @param handler
     * @return connection id
     */
    public int insert_to_connections(ConnectionHandler<T> handler){
    	int id = _id_supplier.incrementAndGet();
    	_connections.add_to_map(id, handler);
    	return id;
    }
    
    @Override
    /**
     * Contains the logic of the actual implementation of the server.
     */
    public abstract void serve(); 
    

}

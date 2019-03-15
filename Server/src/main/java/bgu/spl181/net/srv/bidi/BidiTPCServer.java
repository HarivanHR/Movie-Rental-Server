package bgu.spl181.net.srv.bidi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.SharedDataForUSTBP;
import bgu.spl181.net.api.bidi.UserServiceTextBasedProtocol;
import ustbpActions.AbstractUserServiceCommand;

public class BidiTPCServer<T> extends BaseServer<T> {
	public BidiTPCServer(int port, Supplier<BidiMessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
		super(port, protocolFactory, encoderDecoderFactory);
	}

	@Override
	public void serve() {
        try (ServerSocket serverSock = new ServerSocket(_port)) {
        	 
            this._sock = serverSock; //just to be able to close
            System.out.println("Server started");
            while (!Thread.currentThread().isInterrupted()) {
 
                Socket clientSock = serverSock.accept();

                BidiMessagingProtocol<T> temp_protocol = _protocolFactory.get();
                BidiBlockingConnectionHandler<T> handler = 
                		new BidiBlockingConnectionHandler<T>(clientSock, 
                				_encdecFactory.get(), 
                				temp_protocol);
                int id = insert_to_connections(handler);
                temp_protocol.start(id, get_connections());

                execute(handler);
            }
        } catch (IOException ex) {
        }
 
        System.out.println("Server was closed."); //TODO maybe remove
		
	}


	protected void execute(BidiBlockingConnectionHandler<T> handler) {
		Thread thread_to_run = new Thread(handler);
		thread_to_run.start();
	}
}

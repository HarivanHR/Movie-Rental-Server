package bgu.spl181.net.srv.bidi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import bbActions.BlockBusterRegisterCommand;
import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.BlockBusterProtocol;
public class BidiBlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {
    private final BidiMessagingProtocol<T> _protocol;
    private final MessageEncoderDecoder<T> _encdec;
    private final Socket _sock;
    private BufferedInputStream _in;
    private BufferedOutputStream _out;
    private volatile boolean _connected = true;
    
    
	public BidiBlockingConnectionHandler(Socket clientSock, 
			MessageEncoderDecoder<T> messageEncoderDecoder,
			BidiMessagingProtocol<T> messagingProtocol) {
		_sock = clientSock;
		_encdec = messageEncoderDecoder;
		_protocol = messagingProtocol;
	}


	@Override
    public void run() {
        try (Socket sock = this._sock) { //just for automatic closing
            int read;
            _in = new BufferedInputStream(sock.getInputStream());
            while (!_protocol.shouldTerminate() && _connected && (read = _in.read()) >= 0) {
                T nextMessage = _encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    _protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        _connected = false;
        _sock.close();
    }

	@Override
	public void send(T msg) {
		if (msg != null) {
	        try {
				_out = new BufferedOutputStream(_sock.getOutputStream());
			} catch (IOException e1) {
				//In case the socket was closed.
				e1.printStackTrace();
				return;
			}
            try {
            	byte[] message = _encdec.encode(msg);
				_out.write(message);
	            _out.flush();
			} catch (IOException e) {
				//Again, in case the socket was closed.
				e.printStackTrace();
				return;
			}
        }		
	}
}

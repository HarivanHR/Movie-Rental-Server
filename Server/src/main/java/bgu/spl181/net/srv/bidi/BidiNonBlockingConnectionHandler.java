package bgu.spl181.net.srv.bidi;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.srv.bidi.ConnectionHandler;
import ustbpActions.AbstractUserServiceCommand;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BidiNonBlockingConnectionHandler<T> implements ConnectionHandler<T> {

    private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
    private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();
    private final BidiMessagingProtocol<T> _protocol;
    private final MessageEncoderDecoder<T> _encdec;
    private final Queue<ByteBuffer> _writeQueue = new ConcurrentLinkedQueue<>();
    private final SocketChannel _chan;
    private final BidiReactorServer<T> _reactor;

    public BidiNonBlockingConnectionHandler(
            MessageEncoderDecoder<T> reader,
            BidiMessagingProtocol<T> protocol,
            SocketChannel chan,
            BidiReactorServer<T> reactor) {
        _chan = chan;
        _encdec = reader;
        _protocol = protocol;
        _reactor = reactor;
    }

    public Runnable continueRead() {
        ByteBuffer buf = leaseBuffer();
        boolean success = false;
        try {
            success = _chan.read(buf) != -1; //Checking we haven't reached the end of stream.
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (success) {
            buf.flip(); //Changes the buffer to read the exact number of bytes received.
            return () -> {
                try {
                    while (buf.hasRemaining()) {
                        T nextMessage = _encdec.decodeNextByte(buf.get());
                        if (nextMessage != null) {
                            _protocol.process(nextMessage);
                        }
                    }
                } finally {
                    releaseBuffer(buf);
                }
            };
        } else {
            releaseBuffer(buf);
            close();
            return null;
        }
    }


    public void close() {
        try {
            while (!_writeQueue.isEmpty())
                Thread.yield();
            _chan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isClosed() {
        return !_chan.isOpen();
    }

    public void continueWrite() {
        while (!_writeQueue.isEmpty()) {
            try {
                if (!isClosed()) {
                    ByteBuffer top = _writeQueue.peek();
                    _chan.write(top);
                    if (top.hasRemaining()) {
                        return;
                    } else {
                        _writeQueue.remove();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
                break;
            }
        }

        if (_writeQueue.isEmpty()) {
            if (_protocol.shouldTerminate()) {
                close();
            }
            else {
                _reactor.updateInterestedOps(_chan, SelectionKey.OP_READ);
            }
        }
    }

    private static ByteBuffer leaseBuffer() {
        ByteBuffer buff = BUFFER_POOL.poll();
        if (buff == null) {
            return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
        }

        buff.clear();
        return buff;
    }

    private static void releaseBuffer(ByteBuffer buff) {
        BUFFER_POOL.add(buff);
    }


    @Override
    public void send(T msg) {
        _writeQueue.add(ByteBuffer.wrap(_encdec.encode(msg)));
        _reactor.updateInterestedOps(_chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
}

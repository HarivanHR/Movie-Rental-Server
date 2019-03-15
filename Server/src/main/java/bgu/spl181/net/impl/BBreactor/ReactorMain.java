package bgu.spl181.net.impl.BBreactor;

import java.util.function.Supplier;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.BlockBusterProtocol;
import bgu.spl181.net.api.bidi.SharedDataForBBP;
import bgu.spl181.net.srv.bidi.BBPEncoderDecoder;
import bgu.spl181.net.srv.bidi.BidiReactorServer;
import ustbpActions.AbstractUserServiceCommand;

public class ReactorMain {

	public static void main(String[] args) {


		SharedDataForBBP high_tier_shared_data = new SharedDataForBBP();
		
		
		Supplier<BidiMessagingProtocol<AbstractUserServiceCommand>> BBPFactory 
        = new Supplier<BidiMessagingProtocol<AbstractUserServiceCommand>>() {

			@Override
			public BidiMessagingProtocol<AbstractUserServiceCommand> get() {
				BlockBusterProtocol temp = new BlockBusterProtocol(high_tier_shared_data);
				return temp;
			}
		};	
		
		Supplier<MessageEncoderDecoder<AbstractUserServiceCommand>> BBEncdec = 
        		new Supplier<MessageEncoderDecoder<AbstractUserServiceCommand>>() {

			@Override
			public MessageEncoderDecoder<AbstractUserServiceCommand> get() {
				return new BBPEncoderDecoder();
			}
		};
		
		BidiReactorServer<AbstractUserServiceCommand> _reactor_server =
				new BidiReactorServer<>(10, Integer.parseInt(args[0]), BBPFactory, BBEncdec);
		_reactor_server.serve();

	}

}

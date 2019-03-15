package bbActions;

import bgu.spl181.net.api.bidi.BlockBusterProtocol;
import bgu.spl181.net.api.bidi.UserServiceTextBasedProtocol;
import ustbpActions.AbstractUserServiceCommand;

public abstract class AbstractBlockBusterCommand extends AbstractUserServiceCommand {

	protected BlockBusterProtocol _myBB_protocol;
	
	@Override
	public abstract void runCommand();
	
	@Override
	public void setProtocol(UserServiceTextBasedProtocol my_protocol) {
		_myBB_protocol= (BlockBusterProtocol)my_protocol;
		
	}
}

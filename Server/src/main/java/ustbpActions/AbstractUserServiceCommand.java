package ustbpActions;

import bgu.spl181.net.api.bidi.UserServiceTextBasedProtocol;

/**
 * An abstract class that represents a command to be executed by the protocol.
 * @author Aviv Metz
 *
 */
public abstract class AbstractUserServiceCommand {	
	/**
	 * Executes the command created.
	 */

	protected UserServiceTextBasedProtocol _myUSTBP_protocol;
	protected String _message;
	
	public abstract void runCommand();	
	
	public String getMessage() {
		return _message;
	}
	
	
	public void setProtocol(UserServiceTextBasedProtocol my_protocol) {
		_myUSTBP_protocol= my_protocol;
	}
}

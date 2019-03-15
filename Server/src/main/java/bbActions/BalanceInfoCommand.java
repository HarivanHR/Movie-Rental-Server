package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.ErrorCommand;

public class BalanceInfoCommand extends AbstractBlockBusterCommand {


	@Override
	public void runCommand() {
		if(!_myBB_protocol.get_logged_in().get()) {
			AbstractUserServiceCommand error = new ErrorCommand("request balance failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		}
		else {
			String user = _myBB_protocol.get_my_user_name();
			long balance = _myBB_protocol.get_shared_data().balance_info(user);
			AbstractUserServiceCommand ack = new AckCommand("balance " + balance);
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
		}
	}

}

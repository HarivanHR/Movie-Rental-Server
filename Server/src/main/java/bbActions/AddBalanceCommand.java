package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.ErrorCommand;

public class AddBalanceCommand extends AbstractBlockBusterCommand {

	private Long _additional_balance;

	public AddBalanceCommand(String[] arguments_to_translate) {
		try {
			_additional_balance = Long.parseLong(arguments_to_translate[3]);
		} catch (NumberFormatException e) {
			_additional_balance = null;
		} catch (ArrayIndexOutOfBoundsException ex) {
			_additional_balance = null;
		}
	}

	@Override
	public void runCommand() {
		if (!_myBB_protocol.get_logged_in().get()) {
			AbstractUserServiceCommand error = new ErrorCommand("request balance failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		} else {
			if (_additional_balance == null) {
				AbstractUserServiceCommand error = new ErrorCommand("request balance failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
				return;
			}
			if (_additional_balance <= 0) {
				AbstractUserServiceCommand error = new ErrorCommand("request balance failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
				return;
			}
			String user = _myBB_protocol.get_my_user_name();
			long balance = _myBB_protocol.get_shared_data().add_balance(user, _additional_balance);
			AbstractUserServiceCommand ack = new AckCommand("balance " + balance + " added " + _additional_balance);
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
		}
	}
}

package ustbpActions;

public class SignoutCommand extends AbstractUserServiceCommand {
	//TODO edit the new hashmap
	public void runCommand() {
		String my_user_name = _myUSTBP_protocol.get_my_user_name();
		if(my_user_name != null) {
			_myUSTBP_protocol.get_shared_data().signout(my_user_name);
			_myUSTBP_protocol.get_logged_in().set(false);
			_myUSTBP_protocol.set_my_user_name(null);
			AbstractUserServiceCommand ack = new AckCommand("signout succeeded");
			_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), ack);
			_myUSTBP_protocol.terminate();
			_myUSTBP_protocol.get_connections().disconnect(_myUSTBP_protocol.get_connectionID());
		}
		else {
			AbstractUserServiceCommand error = new ErrorCommand("signout failed");
			_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
		}
	}
}

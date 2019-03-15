package ustbpActions;

public class LoginCommand extends AbstractUserServiceCommand {
	
	private String _name;
	private String _password;
	
	public LoginCommand(String[] arguments_to_translate) {
		if (!(arguments_to_translate.length < 3)){
			_name = arguments_to_translate[1];
			_password = arguments_to_translate[2];
		} else {
			_name = null;
			_password = null;
		}
	}

	@Override
	public void runCommand() {
		if (_name  == null || _password == null){
			AbstractUserServiceCommand error = new ErrorCommand("login failed");
			_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
		}
		else {
			if(!_myUSTBP_protocol.get_logged_in().get()) {
				boolean logged_in = _myUSTBP_protocol.get_shared_data().login(_name, _password, _myUSTBP_protocol.get_connectionID());
				if(logged_in) {
					_myUSTBP_protocol.set_my_user_name(_name);
					_myUSTBP_protocol.get_logged_in().set(true);
					AbstractUserServiceCommand ack = new AckCommand("login succeeded");
					_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), ack);
				}
				else {
					AbstractUserServiceCommand error = new ErrorCommand("login failed");
					_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
				}
			}
			else {
				AbstractUserServiceCommand error = new ErrorCommand("login failed");
				_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
			}
		}
	}
	
}
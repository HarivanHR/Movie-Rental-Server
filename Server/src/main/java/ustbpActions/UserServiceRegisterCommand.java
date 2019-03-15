package ustbpActions;

import java.util.LinkedList;

public class UserServiceRegisterCommand extends AbstractUserServiceCommand {

	private String _name;
	private String _password;
	private LinkedList<String> _extra_info;

	
	public UserServiceRegisterCommand(String[] arguments_to_translate) {
		if(arguments_to_translate.length>=3) {
			_name = arguments_to_translate[1];
			_password = arguments_to_translate[2];
			_extra_info = new LinkedList<>();
			for (int i=3 ; i<arguments_to_translate.length ; i++)
			{
				_extra_info.add(arguments_to_translate[i]);
			}
		}

	}
	
	public void runCommand() {
		if(_name == null || _password == null){
			AbstractUserServiceCommand error = new ErrorCommand("registration failed");
			_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
		}
		else {
			if(!_myUSTBP_protocol.get_logged_in().get()) {		
				boolean registered = _myUSTBP_protocol.get_shared_data().user_service_register(_name, _password);
				if (registered) {
					AbstractUserServiceCommand ack = new AckCommand("registration succeeded");
					_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), ack);
				}
				else {
					AbstractUserServiceCommand error = new ErrorCommand("registration failed");
					_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
				}	
			}
			else {
				AbstractUserServiceCommand error = new ErrorCommand("registration failed");
				_myUSTBP_protocol.get_connections().send(_myUSTBP_protocol.get_connectionID(), error);
			}	
		}
	}
}

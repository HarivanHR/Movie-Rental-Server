package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.ErrorCommand;

import java.util.LinkedList;

public class BlockBusterRegisterCommand extends AbstractBlockBusterCommand {

	private String _name;
	private String _password;
	private LinkedList<String> _extra_info;
	
	public BlockBusterRegisterCommand(String[] arguments_to_translate) {
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

	@Override
	public void runCommand() {
		if (_name == null || _password == null || _extra_info.size() < 1){
			AbstractUserServiceCommand error = new ErrorCommand("registration failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
			return;
		}
		if(!_myBB_protocol.get_logged_in().get()) {
			if (_extra_info.size() <= 1){
				AbstractUserServiceCommand error = new ErrorCommand("registration failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
				return;
			}
			String country = _extra_info.get(1);
			boolean registered = _myBB_protocol.get_shared_data().block_buster_register(_name, _password, country);
			if (registered) {
				AbstractUserServiceCommand ack = new AckCommand("registration succeeded");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
			}
			else {
				AbstractUserServiceCommand error = new ErrorCommand("registration failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
			}
		}
		else {
			AbstractUserServiceCommand error = new ErrorCommand("registration failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		}
		
	}		
}

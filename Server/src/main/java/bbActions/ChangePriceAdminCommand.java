package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.BroadcastCommand;
import ustbpActions.ErrorCommand;

public class ChangePriceAdminCommand extends AbstractBlockBusterCommand {

	private String _movie_name;
	private long _new_price;
	
	public ChangePriceAdminCommand(String[] arguments_to_translate) {
		try {
			_movie_name = arguments_to_translate[2];
			_new_price = Long.parseLong(arguments_to_translate[3]);
		} catch (ArrayIndexOutOfBoundsException e){

		}
		catch(NumberFormatException e) {
			_new_price = -1;
		}
	}
	
	@Override
	public void runCommand() {

		if(_movie_name == null || !_myBB_protocol.get_logged_in().get() ||!_myBB_protocol.get_shared_data().is_admin(_myBB_protocol.get_my_user_name())
				|| _new_price <= 0) {
			AbstractUserServiceCommand error = new ErrorCommand("request changeprice failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		}
		else {
			String toBroadcast = _myBB_protocol.get_shared_data().change_price(_movie_name, _new_price);
			if(!"".equals(toBroadcast)) {
				AbstractUserServiceCommand ack = new AckCommand("changeprice \"" + _movie_name + "\" success");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
				AbstractUserServiceCommand broadcast = new BroadcastCommand(toBroadcast);
				_myBB_protocol.broadcast_to_logged_in_users(broadcast);
			}
			else {
				AbstractUserServiceCommand error = new ErrorCommand("request changeprice failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
			}
		}

	}

}

package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.BroadcastCommand;
import ustbpActions.ErrorCommand;

public class RentMovieCommand extends AbstractBlockBusterCommand {

	private String _movie_name;
	
	public RentMovieCommand(String[] arguments_to_translate) {
		try {
			_movie_name = arguments_to_translate[2];
		} catch (ArrayIndexOutOfBoundsException e){
			//Not enough arguments.
		}
	}
	
	@Override
	public void runCommand() {

		if (_movie_name == null || !_myBB_protocol.get_logged_in().get()) {
			AbstractUserServiceCommand error = new ErrorCommand("request rent failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		}
		else {
			String toBroadcast = _myBB_protocol.get_shared_data().rent_movie(_myBB_protocol.get_my_user_name(), _movie_name);
			if(!"".equals(toBroadcast)) {
				AbstractUserServiceCommand ack = new AckCommand("rent \"" + _movie_name + "\" success");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
				AbstractUserServiceCommand broadcast = new BroadcastCommand(toBroadcast);
				_myBB_protocol.broadcast_to_logged_in_users(broadcast);
			}
			else {
				AbstractUserServiceCommand error = new ErrorCommand("request rent failed");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
			}
		}
	}

}

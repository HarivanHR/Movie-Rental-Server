package bbActions;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.ErrorCommand;

public class MovieInfoCommand extends AbstractBlockBusterCommand {

	private String _movie_name="";
	
	public MovieInfoCommand(String[] arguments_to_translate) {
		if (arguments_to_translate.length>2) {
			_movie_name = arguments_to_translate[2];
		}
	}
	@Override
	public void runCommand() {

		if(!_myBB_protocol.get_logged_in().get()) {
			AbstractUserServiceCommand error = new ErrorCommand("request info failed");
			_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
		}
		else {
			String info="";
			if ("".equals(_movie_name)) {
				info = _myBB_protocol.get_shared_data().all_movies_info().trim();
			}
			else {
				info = _myBB_protocol.get_shared_data().movie_info(_movie_name).trim();
			}
			if("".equals(info) && (_myBB_protocol.get_shared_data().all_movies_info().trim().length() != 0)) {
				AbstractUserServiceCommand error = new ErrorCommand("movie doesn't exist");
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
			}
			else {
				AbstractUserServiceCommand ack = new AckCommand("info " + info);
				_myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
			}
		}
	}	
}

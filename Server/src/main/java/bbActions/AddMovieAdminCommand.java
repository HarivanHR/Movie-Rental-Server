package bbActions;

import java.util.ArrayList;

import ustbpActions.AbstractUserServiceCommand;
import ustbpActions.AckCommand;
import ustbpActions.BroadcastCommand;
import ustbpActions.ErrorCommand;

public class AddMovieAdminCommand extends AbstractBlockBusterCommand {

	private String _movie_name;
	private long _amount_of_copies;
	private long _price_of_movie;
	private ArrayList<String> _banned_countries;


    public AddMovieAdminCommand(String[] arguments_to_translate) {
        try {
            _movie_name = arguments_to_translate[2];
            try { // this try and catch is for illegal input. if we get non-number argument, we change it to a negative number and the runCommand will send error
                _amount_of_copies = Long.parseLong(arguments_to_translate[3]);
                _price_of_movie = Long.parseLong(arguments_to_translate[4]);
            } catch (NumberFormatException e) {
                _amount_of_copies = -1;
                _price_of_movie = -1;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            _amount_of_copies = -1;
            _price_of_movie = -1;
        }
        _banned_countries = new ArrayList<>();
        for (int i = 5 ; i<arguments_to_translate.length ; i++) {
            _banned_countries.add(arguments_to_translate[i]);
        }
    }
    @Override
    public void runCommand() {
        if(!_myBB_protocol.get_logged_in().get() ||!_myBB_protocol.get_shared_data().is_admin(_myBB_protocol.get_my_user_name())
                | _amount_of_copies <= 0 | _price_of_movie <= 0) {
            AbstractUserServiceCommand error = new ErrorCommand("request addmovie failed");
            _myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
        }
        else {
            String toBroadcast = _myBB_protocol.get_shared_data().add_movie(_movie_name, _amount_of_copies, _price_of_movie, _banned_countries);
            if(!"".equals(toBroadcast)) {
                AbstractUserServiceCommand ack = new AckCommand("addmovie \"" + _movie_name + "\" success");
                _myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), ack);
                AbstractUserServiceCommand broadcast = new BroadcastCommand(toBroadcast);
                _myBB_protocol.broadcast_to_logged_in_users(broadcast);
            }
            else {
                AbstractUserServiceCommand error = new ErrorCommand("request addmovie failed");
                _myBB_protocol.get_connections().send(_myBB_protocol.get_connectionID(), error);
            }
        }
    }
}

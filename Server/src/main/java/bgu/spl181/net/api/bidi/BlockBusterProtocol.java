package bgu.spl181.net.api.bidi;

import java.util.HashMap;

import bbActions.AbstractBlockBusterCommand;
import ustbpActions.AbstractUserServiceCommand;

public class BlockBusterProtocol extends UserServiceTextBasedProtocol{

	private SharedDataForBBP _shared_data;
	
	public BlockBusterProtocol(SharedDataForBBP shared_data){
		_shared_data = shared_data;
	}
	
	@Override
	public void process(AbstractUserServiceCommand message) {
		message.setProtocol(this);
		message.runCommand();
	}
	
	@Override
	public SharedDataForBBP get_shared_data() {
		return _shared_data;
	}
	
	public void broadcast_to_logged_in_users(AbstractUserServiceCommand to_broadcast) {
		HashMap<String, Integer> logged_in_users = _shared_data.get_user_to_id_clone();
		for (Integer connection_id : logged_in_users.values()) {
			get_connections().send(connection_id, to_broadcast);
		}
	}
	
}

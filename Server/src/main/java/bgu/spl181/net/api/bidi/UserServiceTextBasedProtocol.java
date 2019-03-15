package bgu.spl181.net.api.bidi;

import java.util.concurrent.atomic.AtomicBoolean;

import ustbpActions.AbstractUserServiceCommand;

public class UserServiceTextBasedProtocol implements BidiMessagingProtocol<AbstractUserServiceCommand> {

	private AtomicBoolean _shouldTerminate;
	private int _connectionID;
	private Connections<AbstractUserServiceCommand> _connections;
	private AtomicBoolean _logged_in;
	private String _my_user_name;
	private SharedDataForUSTBP _shared_data;

	
	
	@Override
	public void start(int connectionId, Connections<AbstractUserServiceCommand> connections) {
		_shouldTerminate = new AtomicBoolean(false);
		_connectionID = connectionId;
		_connections = connections;
		_logged_in = new AtomicBoolean(false);
	}

	@Override
	public void process(AbstractUserServiceCommand message) {
		message.setProtocol(this);
		message.runCommand();
	}
	
	@Override
	public boolean shouldTerminate() {
		return _shouldTerminate.get();
	}

	public int get_connectionID() {
		return _connectionID;
	}

	public void terminate(){ _shouldTerminate.set(true); }

	public Connections<AbstractUserServiceCommand> get_connections() {
		return _connections;
	}

	public AtomicBoolean get_logged_in() {
		return _logged_in;
	}

	public SharedDataForUSTBP get_shared_data() {
		return _shared_data;
	}

	public void set_shared_data(SharedDataForUSTBP _shared_data) {
		this._shared_data = _shared_data;
	}

	public void set_my_user_name(String _my_user_name) {
		this._my_user_name = _my_user_name;
	}

	public String get_my_user_name() {
		return _my_user_name;
	}
}

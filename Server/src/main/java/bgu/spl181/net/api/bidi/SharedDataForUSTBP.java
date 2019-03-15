package bgu.spl181.net.api.bidi;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SharedDataForUSTBP {
		
	private ConcurrentHashMap<String, AtomicBoolean> _registered_and_logged_in_users;
	private ConcurrentHashMap<String, String> _user_password_map;
	private ConcurrentHashMap<String, Integer> _user_to_id;
	
	public SharedDataForUSTBP() {
		_registered_and_logged_in_users = new ConcurrentHashMap<>();
		_user_password_map = new ConcurrentHashMap<>();
		_user_to_id = new ConcurrentHashMap<>();
	}
		
	/**
	 * 
	 * @return a copy of logged in users
	 */
	
	public HashMap<String, Integer> get_user_to_id_clone() {
		boolean great_success = false;
		HashMap<String, Integer> returned_val = null;
		while (!great_success){
			try {
				returned_val =  new HashMap<String, Integer>();
				returned_val.putAll(_user_to_id);
				great_success = true;
			}
			catch (ConcurrentModificationException e){
				//Action wasn't completed, trying again.
			}
		}
		return returned_val;
	}

	/**
	 * tries to register the user.
	 * @param name The username trying to register.
	 * @return true if the user registered successfully.
	 */
	public boolean user_service_register(String name, String password) {
		synchronized (_registered_and_logged_in_users) {
			if(!_registered_and_logged_in_users.containsKey(name)){
				_registered_and_logged_in_users.put(name, new AtomicBoolean(false));
				_user_password_map.put(name, password);
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	/**
	 *  login the user.
	 * @param name The username of the user trying to login.
	 * @return true if the user successfully logged in.
	 */
	
	public boolean login(String name, String password, Integer connectionId) {
		if(_registered_and_logged_in_users.containsKey(name)) {
			if(_user_password_map.get(name).equals(password)) {
				boolean successful_login =  _registered_and_logged_in_users.get(name).compareAndSet(false, true);
				if(successful_login) {
					_user_to_id.put(name, connectionId);
				}
				return successful_login;
			}
		}
		return false;
	}
	
	/**
	 *  Tries to signout the user
	 * @param name username that tries to signout.
	 * @return if the user successfully signed out.
	 */
	public boolean signout(String name) {
		if(!_registered_and_logged_in_users.containsKey(name)) {
			return false;
		}
		boolean successful_signout= _registered_and_logged_in_users.get(name).compareAndSet(true, false);
		if(successful_signout) {
			_user_to_id.remove(name);
		}
		return successful_signout;
	}
	
	protected void initializer(String name, String password) {
		_registered_and_logged_in_users.put(name, new AtomicBoolean(false));
		_user_password_map.put(name, password);
	}
}

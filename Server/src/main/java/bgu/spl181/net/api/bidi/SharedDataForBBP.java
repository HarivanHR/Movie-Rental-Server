package bgu.spl181.net.api.bidi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import jsonClasses.JsonMovie;
import jsonClasses.JsonMoviesList;
import jsonClasses.JsonUser;
import jsonClasses.JsonUsersList;
import jsonClasses.RentedMovie;
/**
 * Shared data for the BlockBuster protocol. Also reads and parses the json file.
 */
public class SharedDataForBBP extends SharedDataForUSTBP {

	private JsonUsersList _registered_users;
	private JsonMoviesList _movie_list;
	private String _user_path = "Database" + File.separatorChar + "Users.json";
	private String _movie_path = "Database" + File.separatorChar + "Movies.json";
	private ReentrantReadWriteLock _movie_lock;
	private ReentrantReadWriteLock _users_lock;

	public SharedDataForBBP() {
		super();
		Gson gson = new Gson();
		Reader reader = null;
		try {
			File file = new File(_user_path);
			 reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			return;}
		_registered_users = gson.fromJson(reader, JsonUsersList.class);
		try {
			File file = new File(_movie_path);
			 reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			return;}
		_movie_list = gson.fromJson(reader, JsonMoviesList.class);
		_movie_lock = new ReentrantReadWriteLock(true);
		_users_lock = new ReentrantReadWriteLock(true);
		for(JsonUser current_user : _registered_users.getUsersList()) {
			String name = current_user.getUserName();
			String password = current_user.getPassword();
			initializer(name, password);
		}
	}
	/**
	 *
	 * @param user_name the name of the Jsonuser we are looking for.
	 * @return the Jsonuser
	 */
	public JsonUser get_user_by_name(String user_name) {
		try {
			_users_lock.readLock().lock();
			for (JsonUser wanted_user : _registered_users.getUsersList()) {
				if(wanted_user.getUserName().equalsIgnoreCase(user_name)) {
					return wanted_user;
				}
			}
			throw new NullPointerException("SHOULDNT GET HERE! no user with such name");
		}
		finally {
			_users_lock.readLock().unlock();
		}
	}

	/**
	 *
	 * @param name checks if the user belong's to an admin.
	 * @return true if the user belong's to an admin.
	 */

	public boolean is_admin(String name) {
		boolean is_admin = false;
		_users_lock.readLock().lock();
		for (JsonUser current_user : _registered_users.getUsersList()) {
			if (current_user.getUserName().equalsIgnoreCase(name)) {
				if (current_user.getType().equalsIgnoreCase("admin")) {
					is_admin = true;
				}
				break;
			}
		}
		_users_lock.readLock().unlock();
		return is_admin;
	}


	/**
	 * Updates the users json file.
	 */

	public void updateUsersJson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gsonBuilder.create();
		try (FileWriter writer = new FileWriter(_user_path)){
			String file = gson.toJson(_registered_users);
			writer.write(file);

		} catch (IOException e) {
			System.out.println("IOException");
			return;
		}
	}

	/**
	 * Updates the movie json file.
	 */
	public void updateMoviesJson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gsonBuilder.create();
		try (FileWriter writer = new FileWriter(_movie_path)){
			String file =  gson.toJson(_movie_list);
			writer.write(file);
		} catch (IOException e) {
			System.out.println("IOException");
			return;
		}
	}

	public JsonUsersList get_registered_users() {
		return _registered_users;
	}

	public JsonMoviesList get_movie_list() {
		return _movie_list;
	}

	public boolean block_buster_register(String name, String password, String country) {
		_users_lock.writeLock().lock();
		boolean registered = user_service_register(name, password);
		if (registered) {
			_registered_users.addUser(name, password, country);
			updateUsersJson();
		}
		_users_lock.writeLock().unlock();
		return registered;
	}

	public long balance_info(String user) {
		try {
			_users_lock.readLock().lock();
			for (JsonUser curr_user : _registered_users.getUsersList()) {
				String curr_user_name = curr_user.getUserName();
				if (curr_user_name.equalsIgnoreCase(user)) {
					long balance = curr_user.getBalance();
					return balance;
				}
			}
		}
			finally {
				_users_lock.readLock().unlock();
			}
		throw new NullPointerException("SHOULDNT GET HERE");
	}

	public Long add_balance(String user, long additional_balance) {
		try {
			_users_lock.writeLock().lock();
			for (JsonUser curr_user : _registered_users.getUsersList()) {
				String curr_user_name = curr_user.getUserName();
				if (curr_user_name.equalsIgnoreCase(user)) {
					curr_user.setBalance(curr_user.getBalance()+additional_balance);
					updateUsersJson();
					return curr_user.getBalance();
				}
			}
		}
		finally {
			_users_lock.writeLock().unlock();
		}
		throw new NullPointerException("SHOULDNT GET HERE");
	}

	public String all_movies_info() {
		try {
			_movie_lock.readLock().lock();
			return _movie_list.getAllMovies();
		}
		finally {
			_movie_lock.readLock().unlock();
		}
	}
	public String movie_info(String movie_name) {
		try {
			_movie_lock.readLock().lock();
			for(JsonMovie curr_movie : _movie_list.getMoviesList()) {
				if(curr_movie.getName().equalsIgnoreCase(movie_name)) {
					return curr_movie.infoString();
				}
			}
		}
		finally {
			_movie_lock.readLock().unlock();
		}
		return "";
	}

	public String rent_movie(String user, String movieToRentName) {
		try {
			String string_to_broadcast ="movie \"" + movieToRentName + "\" ";
			//Getting the user
			JsonUser renting_user = get_user_by_name(user);

			//Getting the movie
			_movie_lock.writeLock().lock();
			JsonMovie movie_to_rent = null;
			for (JsonMovie current_movie : _movie_list.getMoviesList()) {
				if (current_movie.getName().equalsIgnoreCase(movieToRentName)) {
					movie_to_rent = current_movie;
					break;
				}
			}
			if(movie_to_rent == null) { // the movie doesnt exist
				return "";
			}
			//Testing for failed reasons.
			if (!renting_user.hasMovie(movieToRentName)) {		 // user already has the movie.
				if(movie_to_rent.getAvailableAmount()>0) { 		 // the are no more available copies
					string_to_broadcast += Long.toString(movie_to_rent.getAvailableAmount()-1) + " ";
					String user_country = renting_user.getCountry();
					boolean banned_flag = false;
					for (String banned_country: movie_to_rent.getBannedCountries()){
						if (user_country.equalsIgnoreCase(banned_country)){
							banned_flag = true;
							break;
						}
					}
					if(!banned_flag) { 		// the user is in a banned country
						if(renting_user.getBalance()>=movie_to_rent.getPrice()) { 		// the user doesnt have enough money to rent
							string_to_broadcast += Long.toString(movie_to_rent.getPrice());
							movie_to_rent.setAvailableAmount(movie_to_rent.getAvailableAmount()-1);
							updateMoviesJson();
							_users_lock.writeLock().lock();
							renting_user.setBalance(renting_user.getBalance()-movie_to_rent.getPrice());
							renting_user.getMovies().add(new RentedMovie(movie_to_rent.getId(), movieToRentName));
							updateUsersJson();
							_users_lock.writeLock().unlock();
							return string_to_broadcast;
						}
					}
				}
			}
			return "";
		}
		finally {
			_movie_lock.writeLock().unlock();
		}
	}

	public String return_movie(String user, String movie_to_return_name) {
		try {
			String string_to_broadcast ="movie \"" + movie_to_return_name + "\" ";
			//Getting the user
			JsonUser returning_user = get_user_by_name(user);

			_movie_lock.writeLock().lock();
			JsonMovie movie_to_return = null;
			for (JsonMovie current_movie : _movie_list.getMoviesList()) {
				if (current_movie.getName().toLowerCase().equals(movie_to_return_name.toLowerCase())) {
					movie_to_return = current_movie;
					break;
				}
			}
			if(movie_to_return == null) { // the movie doesnt exist
				return "";
			}
			//Testing for failed reasons.
			if (returning_user.hasMovie(movie_to_return_name)) {		 // user doesnt have the movie.
					movie_to_return.setAvailableAmount(movie_to_return.getAvailableAmount()+1);
					updateMoviesJson();
					_users_lock.writeLock().lock();
					RentedMovie movie_to_remove = returning_user.remove_movie(movie_to_return_name);
					returning_user.getMovies().remove(movie_to_remove);
					updateUsersJson();
					string_to_broadcast += Long.toString(movie_to_return.getAvailableAmount()) + " " + Long.toString(movie_to_return.getPrice());
					_users_lock.writeLock().unlock();
					return string_to_broadcast;
			}
			return "";
		}
		finally {
			_movie_lock.writeLock().unlock();
		}
	}

	public String add_movie(String name, long copies, long price, ArrayList<String> banned_countries) {
		try {
			_movie_lock.writeLock().lock();
			String string_to_broadcast ="movie \"" + name + "\" ";
			boolean does_movie_exist = false;
			long max_movie_id = 0;
			for(JsonMovie movie : _movie_list.getMoviesList()) {
				if(movie.getName().toLowerCase().equals(name.toLowerCase())) {
					does_movie_exist = true;
				}
				if (movie.getId()>max_movie_id) {
					max_movie_id=movie.getId();
				}
			}
			if (does_movie_exist) {
				return "";
			}
			else {
				long new_movie_id = max_movie_id+1;
				JsonMovie new_movie = new JsonMovie(new_movie_id, name, price, banned_countries, copies);
				_movie_list.getMoviesList().add(new_movie);
				updateMoviesJson();
				string_to_broadcast += Long.toString(copies) + " " + Long.toString(price);
				return string_to_broadcast;
			}
		}
		finally {
			_movie_lock.writeLock().unlock();
		}
	}

	public String remove_movie(String name) {
		try {
			_movie_lock.writeLock().lock();
			for(JsonMovie movie : _movie_list.getMoviesList()) {
				if(movie.getName().toLowerCase().equals(name.toLowerCase())) {
					if(movie.getAvailableAmount() != movie.getTotalAmount()){
						return "";
					}
					_movie_list.getMoviesList().remove(movie);
					updateMoviesJson();
					String string_to_broadcast ="movie \"" + name + "\" removed";
					return string_to_broadcast;
				}
			}
			return "";
		}
		finally {
			_movie_lock.writeLock().unlock();
		}
	}

	public String change_price(String name, long new_price) {
		try {
			_movie_lock.writeLock().lock();
			for(JsonMovie movie : _movie_list.getMoviesList()) {
				if(movie.getName().toLowerCase().equals(name.toLowerCase())) {
					movie.setPrice(new_price);
					updateMoviesJson();
					String string_to_broadcast ="movie \"" + name + "\" " + Long.toString(movie.getAvailableAmount()) + " " + Long.toString(new_price);
					return string_to_broadcast;
				}
			}
			return "";
		}
		finally {
			_movie_lock.writeLock().unlock();
		}
	}
}



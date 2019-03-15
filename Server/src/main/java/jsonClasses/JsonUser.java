package jsonClasses;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonUser implements Serializable {

	@SerializedName("username")
	@Expose
	private String userName;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("country")
	@Expose
	private String country;
	@SerializedName("movies")
	@Expose
	private ArrayList<RentedMovie> movies;
	@SerializedName("balance")
	@Expose
	private long balance;
	
	public JsonUser(String name, String myPassword, String newCountry) {
		userName = name;
		type = "normal";
		password = myPassword;
		country = newCountry; 
		movies = new ArrayList<RentedMovie>();
		balance = 0;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public ArrayList<RentedMovie> getMovies() {
		return movies;
	}
	public void setMovies(ArrayList<RentedMovie> movies) {
		this.movies = movies;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	public boolean hasMovie(String movie_name) {
		for (RentedMovie current_movie : movies) {
			if(current_movie.getName().equalsIgnoreCase(movie_name))
				return true;
		}
		return false;
	}
	
	public RentedMovie remove_movie(String movie_name) {
		for(RentedMovie movie : movies) {
			if (movie.getName().equals(movie_name))
				return movie;
		}
		return null;
	}
	
	public String toString() {
		String output="";
		output = "username: " + getUserName() + "\n";
		output += "user password: " + getPassword() + "\n";
		output += "type: " + getType() + "\n";
		output += "country: " + getCountry() + "\n";
		output += "balance: " + getBalance() +"\n";
		output += "rented movies: \n";
		for(RentedMovie movie : movies) {
			output +=   movie.toString();
		}
		output += "\n";
		return output;
	}
}

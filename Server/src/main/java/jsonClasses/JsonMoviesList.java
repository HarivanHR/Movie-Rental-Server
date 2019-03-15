package jsonClasses;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonMoviesList implements Serializable {

	@SerializedName("movies")
	@Expose
	private ArrayList<JsonMovie> moviesList = new ArrayList<JsonMovie>();

	public ArrayList<JsonMovie> getMoviesList() {
		return moviesList;
	}

	public void setMoviesList(ArrayList<JsonMovie> moviesList) {
		this.moviesList = moviesList;
	}
	
	public String getAllMovies() {
		String output="";
		for (JsonMovie movie : moviesList) {
			output += "\"" +  movie.getName() + "\" ";
		}
		return output;
	}
	
	public String toString() {
		String output = "movies in jsonFile: \n";
		for (JsonMovie movie : moviesList) {
			output += movie.toString();
		}
		output += "total amount of movies: " + moviesList.size() + "\n";
		return output;
	}
}

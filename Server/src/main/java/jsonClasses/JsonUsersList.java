package jsonClasses;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonUsersList implements Serializable  {

	@SerializedName("users")
	@Expose
	private ArrayList<JsonUser> usersList = new ArrayList<JsonUser>();

	public ArrayList<JsonUser> getUsersList() {
		return usersList;
	}

	public void setUsersList(ArrayList<JsonUser> usersList) {
		this.usersList = usersList;
	}
	
	public void addUser(String name, String password, String country) {
		JsonUser user_to_add = new JsonUser(name, password, country);
		usersList.add(user_to_add);
	}

	public String toString() {
		String output = "users in jsonFile: \n";
		for (JsonUser user : usersList) {
			output += user.toString();
		}
		output += "total amount of users: " + usersList.size() + "\n";
		return output;
	}
}

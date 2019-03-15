package jsonClasses;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonMovie implements Serializable {

	@SerializedName("id")
	@Expose
	private long id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("price")
	@Expose
	private long price;
	@SerializedName("bannedCountries")
	@Expose
	private ArrayList<String> bannedCountries;
	@SerializedName("availableAmount")
	@Expose
	private long availableAmount;
	@SerializedName("totalAmount")
	@Expose
	private long totalAmount;
	
	public JsonMovie(long new_id, String new_name, long new_price, ArrayList<String> new_banned_countries, long new_amount) {
		id = new_id;
		name = new_name;
		price = new_price;
		bannedCountries = new_banned_countries;
		availableAmount = new_amount;
		totalAmount = new_amount;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public ArrayList<String> getBannedCountries() {
		return bannedCountries;
	}
	public void setBannedCountries(ArrayList<String> bannedCountries) {
		this.bannedCountries = bannedCountries;
	}
	public long getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(long availableAmount) {
		this.availableAmount = availableAmount;
	}
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String infoString() {
		String output="";
		output = "\"" + getName() + "\" " + getAvailableAmount() + " " + getPrice() + " ";
		for(String bannedCountry : bannedCountries) {
			output += "\"" + bannedCountry + "\" ";
		}
		return output;
	}
	
	public String toString() {
		String output="";
		output = "movie Name: " + getName() + "\n";
		output += "movie ID: " + getId() + "\n";
		output += "available amount: " + getAvailableAmount() + "\n";
		output += "total amount: " + getTotalAmount() + "\n";
		output += "price: " + getPrice() +"\n";
		output += "banned countries: ";
		for(String bannedCountry : bannedCountries) {
			output += "\"" + bannedCountry + "\" ";
		}
		output += "\n\n";
		return output;
	}
	
}
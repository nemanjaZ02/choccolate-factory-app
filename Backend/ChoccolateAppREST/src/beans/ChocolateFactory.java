package beans;

import java.util.ArrayList;

import enums.WorkingStatus;

public class ChocolateFactory {
	private int id;
	private String name;
	private ArrayList<Chocolate> chocolates;
	private WorkTime workTime;
	private WorkingStatus status;
	private Location location;
	private String logo;
	private double rating;
	private boolean isDeleted;
	
	public ChocolateFactory() {
		super();
		chocolates = new ArrayList<Chocolate>();
	}

	public ChocolateFactory(int id, String name, ArrayList<Chocolate> chocolates, WorkTime workTime, WorkingStatus status,
			Location location, String logo, double rating, boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.chocolates = chocolates;
		this.workTime = workTime;
		this.status = status;
		this.location = location;
		this.logo = logo;
		this.rating = rating;
		this.isDeleted = isDeleted;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

	public WorkTime getWorkTime() {
		return workTime;
	}

	public void setWorkTime(WorkTime workTime) {
		this.workTime = workTime;
	}

	public WorkingStatus getStatus() {
		return status;
	}

	public void setStatus(WorkingStatus status) {
		this.status = status;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}	
}

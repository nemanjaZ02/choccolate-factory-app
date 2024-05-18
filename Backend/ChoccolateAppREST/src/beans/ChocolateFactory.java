package beans;

import java.util.List;

import enums.WorkingStatus;

public class ChocolateFactory {
	private String name;
	private List<Chocolate> chocolates;
	private WorkTime workTime;
	private WorkingStatus status;
	private Location location;
	private String logo;
	private double rating;
	
	public ChocolateFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChocolateFactory(String name, List<Chocolate> chocolates, WorkTime workTime, WorkingStatus status,
			Location location, String logo, double rating) {
		super();
		this.name = name;
		this.chocolates = chocolates;
		this.workTime = workTime;
		this.status = status;
		this.location = location;
		this.logo = logo;
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(List<Chocolate> chocolates) {
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
}

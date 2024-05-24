package beans;

public class Comment {
	private int id;
	private int customerId;
	private int factoryId;
	private String text;
	private double rating;
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(int id, int customerId, int factoryId, String text, double rating) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.factoryId = factoryId;
		this.text = text;
		this.rating = rating;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public double getRating() {
		return rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
}

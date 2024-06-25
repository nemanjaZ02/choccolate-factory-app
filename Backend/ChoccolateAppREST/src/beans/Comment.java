package beans;

import enums.CommentStatus;

public class Comment {
	private int id;
	private Customer customer;
	private int factoryId;
	private String text;
	private double rating;
	private CommentStatus status;
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(int id, Customer customer, int factoryId, String text, double rating, CommentStatus status) {
		super();
		this.id = id;
		this.customer = customer;
		this.factoryId = factoryId;
		this.text = text;
		this.rating = rating;
		this.status = status;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public CommentStatus getStatus() {
		return status;
	}

	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	
	
}

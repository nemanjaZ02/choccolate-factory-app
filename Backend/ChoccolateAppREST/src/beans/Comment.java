package beans;

public class Comment {
	
	private Customer customer;
	private ChocolateFactory factory;
	private String text;
	private double rating;
	
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Comment(Customer customer, ChocolateFactory factory, String text, double rating) {
		super();
		this.customer = customer;
		this.factory = factory;
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


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public ChocolateFactory getFactory() {
		return factory;
	}


	public void setFactory(ChocolateFactory factory) {
		this.factory = factory;
	}


	@Override
	public String toString() {
		return "Comment [customer=" + customer + ", factory=" + factory + ", text=" + text + ", rating=" + rating + "]";
	}

}

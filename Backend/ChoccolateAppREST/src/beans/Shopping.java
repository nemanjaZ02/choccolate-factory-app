package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

import enums.ShoppingState;

public class Shopping {

	private String id;
	private ArrayList<Chocolate> chocolate = new ArrayList<Chocolate>();
	private ChocolateFactory factory;
	private LocalDateTime dateAndTime;
	private double price;
	private Customer customer;
	private ShoppingState state;
	
	public Shopping() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Shopping(String id, ArrayList<Chocolate> chocolate, ChocolateFactory factory, LocalDateTime dateAndTime,
			double price, Customer customer, ShoppingState state) {
		super();
		this.id = id;
		this.chocolate = chocolate;
		this.factory = factory;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.customer = customer;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Chocolate> getChocolate() {
		return chocolate;
	}

	public void setChocolate(ArrayList<Chocolate> chocolate) {
		this.chocolate = chocolate;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ShoppingState getState() {
		return state;
	}

	public void setState(ShoppingState state) {
		this.state = state;
	}



	public ChocolateFactory getFactory() {
		return factory;
	}



	public void setFactory(ChocolateFactory factory) {
		this.factory = factory;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	@Override
	public String toString() {
		return "Shopping [id=" + id + ", chocolate=" + chocolate + ", factory=" + factory + ", dateAndTime="
				+ dateAndTime + ", price=" + price + ", state=" + state + "]";
	}
	
}

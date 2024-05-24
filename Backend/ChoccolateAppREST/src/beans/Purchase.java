package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

import enums.PurchaseState;

public class Purchase {
	private ArrayList<Chocolate> chocolate = new ArrayList<Chocolate>();
	private ChocolateFactory factory;
	private LocalDateTime dateAndTime;
	private double price;
	private Customer customer;
	private PurchaseState state;
	
	public Purchase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Purchase(ArrayList<Chocolate> chocolate, ChocolateFactory factory, LocalDateTime dateAndTime,
			double price, Customer customer, PurchaseState state) {
		super();
		this.chocolate = chocolate;
		this.factory = factory;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.customer = customer;
		this.state = state;
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

	public PurchaseState getState() {
		return state;
	}

	public void setState(PurchaseState state) {
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
}

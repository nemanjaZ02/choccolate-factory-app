package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;



import enums.PurchaseState;

public class Purchase {
	private int id;
	private ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
	private int factoryId;
	private LocalDateTime dateAndTime;
	private double price;
	private Customer customer;
	private PurchaseState state;
	private String declineReason;
	
	public Purchase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Purchase(int id, ArrayList<Chocolate> chocolate, int factoryId, LocalDateTime dateAndTime,
			double price, Customer customerId, PurchaseState state, String declineReason ) {
		super();
		this.id = id;
		this.chocolates = chocolate;
		this.factoryId = factoryId;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.customer = customer;
		this.state = state;
		this.declineReason = declineReason;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolate) {
		this.chocolates = chocolate;
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

	public int getFactoryId() {
		return factoryId;
	}

	public String getDeclineReason() {
		return declineReason;
	}

	public void setDeclineReason(String declineReason) {
		this.declineReason = declineReason;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
}

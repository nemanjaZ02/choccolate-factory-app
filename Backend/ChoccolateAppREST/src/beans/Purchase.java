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
	private int customerId;
	private PurchaseState state;
	
	public Purchase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Purchase(int id, ArrayList<Chocolate> chocolate, int factoryId, LocalDateTime dateAndTime,
			double price, int customerId, PurchaseState state) {
		super();
		this.id = id;
		this.chocolates = chocolate;
		this.factoryId = factoryId;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.customerId = customerId;
		this.state = state;
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

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
}

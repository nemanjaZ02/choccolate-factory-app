package beans;


import java.util.ArrayList;

public class Cart {

	private ArrayList <Chocolate> chocolates = new ArrayList<Chocolate>();
	Customer customer;
	private double price;
	
	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cart(ArrayList<Chocolate> chocolates, Customer customer, double price) {
		super();
		this.chocolates = chocolates;
		this.customer = customer;
		this.price = price;
	}

	public ArrayList<Chocolate> getChocolates() {
		return chocolates;
	}

	public void setChocolates(ArrayList<Chocolate> chocolates) {
		this.chocolates = chocolates;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Cart [chocolates=" + chocolates + ", price=" + price + "]";
	}
}

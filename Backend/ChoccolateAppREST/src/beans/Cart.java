package beans;


import java.util.ArrayList;

public class Cart {
	private ArrayList<Chocolate> chocolates;
	private double price;
	
	public Cart() {
		super();
		chocolates = new ArrayList<Chocolate>();
	}

	public Cart(ArrayList<Chocolate> chocolates, Customer customer, double price) {
		super();
		this.chocolates = chocolates;
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

	@Override
	public String toString() {
		return "Cart [chocolates=" + chocolates + ", price=" + price + "]";
	}
}

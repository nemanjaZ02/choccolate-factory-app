package beans;

import java.util.Date;
import java.util.List;

import enums.Gender;
import enums.Role;

public class Customer extends User {
	private List<Purchase> purchases;
	private Cart cart = new Cart();
	private double points;
	private CustomerType type;
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role) {
		super(id, username, password, name, surname, gender, birthday, role);
		// TODO Auto-generated constructor stub
	}
	
	public Customer(List<Purchase> purchases, Cart cart, double points, CustomerType type) {
		super();
		this.purchases = purchases;
		this.cart = cart;
		this.points = points;
		this.type = type;
	}
	
	public Customer(User user) {
		super(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getGender(), user.getBirthday(), user.getRole());
		// TODO Auto-generated constructor stub
	}
	
	public List<Purchase> getShoppings() {
		return purchases;
	}
	public void setShoppings(List<Purchase> shoppings) {
		this.purchases = shoppings;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
}

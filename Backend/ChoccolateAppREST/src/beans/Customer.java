package beans;

import java.util.Date;
import java.util.List;

import enums.Gender;
import enums.Role;

public class Customer extends User {
	private List<Shopping> shoppings;
	private Cart cart;
	private int points;
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
	
	public Customer(List<Shopping> shoppings, Cart cart, int points, CustomerType type) {
		super();
		this.shoppings = shoppings;
		this.cart = cart;
		this.points = points;
		this.type = type;
	}
	
	public Customer(User user) {
		super(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getGender(), user.getBirthday(), user.getRole());
		// TODO Auto-generated constructor stub
	}
	
	public List<Shopping> getShoppings() {
		return shoppings;
	}
	public void setShoppings(List<Shopping> shoppings) {
		this.shoppings = shoppings;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
}

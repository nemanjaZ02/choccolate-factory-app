package beans;

import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
	private  String name;
	private double price;
	private ChocolateType type;
	private ChocolateKind kind;
	private ChocolateStatus status;
	private ChocolateFactory factory;
	private double grams; 
	private String description;
	private String image;
	private int quantity;
	

	public Chocolate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Chocolate(String name, double price, ChocolateType type, ChocolateKind kind, ChocolateStatus status,
			ChocolateFactory factory,	double grams, String description, String image, int quantity) {
		super();
		this.name = name;
		this.price = price;
		this.type = type;
		this.kind = kind;
		this.status = status;
		this.factory = factory;
		this.grams = grams;
		this.description = description;
		this.image = image;
		this.quantity = quantity;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ChocolateType getType() {
		return type;
	}
	public void setType(ChocolateType type) {
		this.type = type;
	}
	public ChocolateKind getKind() {
		return kind;
	}
	public void setKind(ChocolateKind kind) {
		this.kind = kind;
	}
	public ChocolateStatus getStatus() {
		return status;
	}
	public void setStatus(ChocolateStatus status) {
		this.status = status;
	}
	public double getGrams() {
		return grams;
	}
	public void setGrams(double grams) {
		this.grams = grams;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	public ChocolateFactory getFactory() {
		return factory;
	}

	public void setFactory(ChocolateFactory factory) {
		this.factory = factory;
	}

	@Override
	public String toString() {
		return "Chocolate [name=" + name + ", price=" + price + ", type=" + type + ", kind=" + kind + ", status="
				+ status + ", factory=" + factory + ", grams=" + grams + ", description=" + description + ", image="
				+ image + ", quantity=" + quantity + "]";
	}

}

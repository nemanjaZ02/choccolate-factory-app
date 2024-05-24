package beans;

import enums.ChocolateStatus;

public class Chocolate {
	private int id;
	private int factoryId;
	private  String name;
	private double price;
	private String type;
	private String kind;
	private ChocolateStatus status;
	private double grams; 
	private String description;
	private String image;
	private int quantity;

	public Chocolate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Chocolate(int id, String name, double price, String type, String kind, ChocolateStatus status,
			int factoryId,	double grams, String description, String image, int quantity) {
		super();
		this.id = id;
		this.factoryId = factoryId;
		this.name = name;
		this.price = price;
		this.type = type;
		this.kind = kind;
		this.status = status;
		this.grams = grams;
		this.description = description;
		this.image = image;
		this.quantity = quantity;
	}	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
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
}

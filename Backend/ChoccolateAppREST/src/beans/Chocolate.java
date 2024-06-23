package beans;

import enums.ChocolateKind;
import enums.ChocolateStatus;
import enums.ChocolateType;

public class Chocolate {
	private int id;
	private int factoryId;
	private  String name;
	private double price;
	private ChocolateType type;
	private ChocolateKind kind;
	private ChocolateStatus status;
	private double weight; 
	private String description;
	private String image;
	private int quantity;
	private boolean isDeleted;

	public Chocolate() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Chocolate(int id, int factoryId, String name, double price, ChocolateType type, ChocolateKind kind, ChocolateStatus status,
				double weight, String description, String image, int quantity, boolean isDeleted) {

		super();
		this.id = id;
		this.factoryId = factoryId;
		this.name = name;
		this.price = price;
		this.type = type;
		this.kind = kind;
		this.status = status;
		this.weight = weight;
		this.description = description;
		this.image = image;
		this.quantity = quantity;
		this.isDeleted = isDeleted;
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

	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;

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
	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}	
}

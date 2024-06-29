package beans;

public class CustomerType {
	private int id;
	private String typeName;
	private double discount;
	private int requiredPoints;
	
	public CustomerType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerType(int id, String typeName, double discount, int requiredPoints) {
		super();
		this.id = id;
		this.typeName = typeName;
		this.discount = discount;
		this.requiredPoints = requiredPoints;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getRequiredPoints() {
		return requiredPoints;
	}

	public void setRequiredPoints(int requiredPoints) {
		this.requiredPoints = requiredPoints;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

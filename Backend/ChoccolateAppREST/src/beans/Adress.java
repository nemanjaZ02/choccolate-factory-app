package beans;

public class Adress {
	private String street;
	private String streetNum;
	private String city;
	private int postNum;
	
	public Adress() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Adress(String street, String streetNum, String city, int postNum) {
		super();
		this.street = street;
		this.streetNum = streetNum;
		this.city = city;
		this.postNum = postNum;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNum() {
		return streetNum;
	}
	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPostNum() {
		return postNum;
	}
	public void setPostNum(int postNum) {
		this.postNum = postNum;
	}
	
	
}

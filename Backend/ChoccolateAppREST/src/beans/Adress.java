package beans;

public class Adress {
	private String street;
	private String streetNum;
	private String city;
	private String country;
	private String postNum;
	
	public Adress() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Adress(String street, String streetNum, String city, String postNum) {
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
	public String getPostNum() {
		return postNum;
	}
	public void setPostNum(String postNum) {
		this.postNum = postNum;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	
}

package beans;

import java.util.Date;

import enums.Gender;
import enums.Role;

public class Manager extends User {
	private int factoryId;

	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manager(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role) {
		super(id, username, password, name, surname, gender, birthday, role);
		// TODO Auto-generated constructor stub
	}
	public Manager(User user) {
		super(user.getId(),user.getUsername(),user.getPassword(),user.getName(),user.getSurname(),user.getGender(),user.getBirthday(),user.getRole());
		// TODO Auto-generated constructor stub
	}

	public Manager(int factoryId) {
		super();
		this.factoryId = factoryId;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}	
}

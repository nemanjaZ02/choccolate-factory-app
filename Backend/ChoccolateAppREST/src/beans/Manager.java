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

	public Manager(int factoryId) {
		super();
		this.factoryId = factoryId;
	}

	public int getChocolateFactory() {
		return factoryId;
	}

	public void setChocolateFactory(int factoryId) {
		this.factoryId = factoryId;
	}	
}

package beans;

import java.util.Date;

import enums.Gender;
import enums.Role;

public class Manager extends User {
	private ChocolateFactory chocolateFactory;

	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Manager(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role) {
		super(id, username, password, name, surname, gender, birthday, role);
		// TODO Auto-generated constructor stub
	}

	public Manager(ChocolateFactory chocolateFactory) {
		super();
		this.chocolateFactory = chocolateFactory;
	}

	public ChocolateFactory getChocolateFactory() {
		return chocolateFactory;
	}

	public void setChocolateFactory(ChocolateFactory chocolateFactory) {
		this.chocolateFactory = chocolateFactory;
	}	
}

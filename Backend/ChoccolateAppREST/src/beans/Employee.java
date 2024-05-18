package beans;

import java.util.Date;

import enums.Gender;
import enums.Role;

public class Employee extends User {

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role) {
		super(id, username, password, name, surname, gender, birthday, role);
		// TODO Auto-generated constructor stub
	}
}

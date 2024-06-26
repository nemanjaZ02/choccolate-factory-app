package beans;

import java.util.Date;

import enums.Gender;
import enums.Role;

public class Employee extends User {
	private int factoryId;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role, int factoryId) {
		super(id, username, password, name, surname, gender, birthday, role);
		
		this.factoryId = factoryId;
	}
	
	public Employee(User user) {
		super(user.getId(),user.getUsername(),user.getPassword(),user.getName(),user.getSurname(),user.getGender(),user.getBirthday(),user.getRole());
		// TODO Auto-generated constructor stub
	}

	public Employee(int factoryId) {
		super();
		this.factoryId = factoryId;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	
	public void update(User u)
	{
		setName(u.getName());
		setSurname(u.getSurname());
		setBirthday(u.getBirthday());
		setUsername(u.getUsername());
		setGender(u.getGender());
	}
}

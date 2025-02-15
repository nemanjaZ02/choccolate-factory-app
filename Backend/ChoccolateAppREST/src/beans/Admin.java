package beans;

import java.time.LocalDate;
import java.util.Date;

import enums.Gender;
import enums.Role;

public class Admin extends User {

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role, boolean isDeleted, boolean isSuspicious, boolean isBanned) {
		super(id, username, password, name, surname, gender, birthday, role, isDeleted, isSuspicious, isBanned);
		// TODO Auto-generated constructor stub
	}	
	
	public void update(User u)
	{
		setName(u.getName());
		setSurname(u.getSurname());
		setBirthday(u.getBirthday());
		setUsername(u.getUsername());
		setGender(u.getGender());
		setDeleted(u.getIsDeleted());
	}
}

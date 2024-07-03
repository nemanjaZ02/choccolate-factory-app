package beans;

import java.time.LocalDate;
import java.util.Date;

import enums.Gender;
import enums.Role;

public class User {
	private int id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private Gender gender;
	private Date birthday;
	private Role role;
	private boolean isDeleted;
	private boolean isSuspicious;
	private boolean isBanned;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id, String username, String password, String name, String surname, Gender gender, Date birthday,
			Role role, boolean isDeleted, boolean isSuspicious, boolean isBanned) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.isDeleted = isDeleted;
		this.isSuspicious = isSuspicious;
		this.isBanned = isBanned;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public boolean getIsSuspicious() {
		return isSuspicious;
	}
	public void setSuspicious(boolean isSuspicious) {
		this.isSuspicious = isSuspicious;
	}
	public boolean getIsBanned() {
		return isBanned;
	}
	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}
}

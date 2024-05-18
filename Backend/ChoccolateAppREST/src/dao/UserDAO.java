package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;

import beans.Admin;
import beans.Customer;
import beans.Employee;
import beans.Manager;
import beans.User;
import enums.Role;

public class UserDAO {
	
	private ArrayList<Customer> customers = new ArrayList<Customer>();
	private ArrayList<Manager> managers = new ArrayList<Manager>();
	private ArrayList<Employee> employees = new ArrayList<Employee>();
	private ArrayList<Admin> admins = new ArrayList<Admin>();
	
	public UserDAO() {	
		
	}
	
	public UserDAO(String contextPath) {
		loadCustomers(contextPath);
		loadManagers(contextPath);
		loadAdmins(contextPath);
		loadEmployees(contextPath);
	}
	
	public ArrayList<Customer> findAllCustomers() {
		return customers;
	}
	
	public User find(String username, String password) {
		for (User customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }
	
	private void loadEmployees(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "employees.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Employee[] employeeArray = gson.fromJson(jsonData, Employee[].class);
            if (employeeArray != null) {
                for (Employee employee : employeeArray) {
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}
	
	private void loadAdmins(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "admins.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Admin[] adminArray = gson.fromJson(jsonData, Admin[].class);
            if (adminArray != null) {
                for (Admin admin : adminArray) {
                    admins.add(admin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void loadManagers(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "managers.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Manager[] managerArray = gson.fromJson(jsonData, Manager[].class);
            if (managerArray != null) {
                for (Manager manager : managerArray) {
                    managers.add(manager);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void loadCustomers(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "customers.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Customer[] customerArray = gson.fromJson(jsonData, Customer[].class);
            if (customerArray != null) {
                for (Customer customer : customerArray) {
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public User registerNewUser(User newUser, String contextPath) {
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;
			
			if(newUser.getRole() == Role.CUSTOMER)
			{
				int maxId = -1;
				for(Customer customer : customers)
				{
					if(customer.getId() > maxId)
					{
						maxId = customer.getId();
					}
				}
				
				if(maxId == -1)
				{
					maxId = 0;
				}
				
				newUser.setId(maxId + 1);
				User customerUser = new Customer(newUser);
				customers.add((Customer)customerUser);
				
				filePath = Paths.get(contextPath, "/customers.json");
				Customer[] updatedUserArray = customers.toArray(new Customer[0]);
				updatedJsonData = gson.toJson(updatedUserArray);
			}
			else if(newUser.getRole() == Role.MANAGER)
			{
				int maxId = -1;
				for(Manager manager : managers)
				{
					if(manager.getId() > maxId)
					{
						maxId = manager.getId() + 1;
					}
				}
				
				if(maxId == -1)
				{
					maxId = 0;
				}
				
				newUser.setId(maxId);
				managers.add((Manager)newUser);
				filePath = Paths.get(contextPath, "/managers.json");
				Manager[] updatedUserArray = managers.toArray(new Manager[0]);
				updatedJsonData = gson.toJson(updatedUserArray);
			}
			else
			{
				int maxId = -1;
				for(Employee employee : employees)
				{
					if(employee.getId() > maxId)
					{
						maxId = employee.getId() + 1;
					}
				}
				
				newUser.setId(maxId);
				employees.add((Employee)newUser);
				filePath = Paths.get(contextPath, "/employees.json");
				Employee[] updatedUserArray = employees.toArray(new Employee[0]);
				updatedJsonData = gson.toJson(updatedUserArray);
			}

            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newUser;	
	}
}

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
import beans.Chocolate;
import beans.ChocolateFactory;
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
		
		for (User manager : managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return manager;
            }
        }
		for (User admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
		
		for (User employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return employee;
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
	
	
	public User registerNewUser(Customer newUser, String contextPath) {
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

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
			customers.add(newUser);
				
			filePath = Paths.get(contextPath, "/customers.json");
			Customer[] updatedUserArray = customers.toArray(new Customer[0]);
			updatedJsonData = gson.toJson(updatedUserArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newUser;	
	}
	
	public boolean isUserManager(int userId)
	{
		for(Manager manager: managers )
		{
			if(manager.getId()==userId)
				return true;
		}
		return false;
	}
	public Manager registerNewManager(Manager newUser, String contextPath) {
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

			int maxId = -1;
			for(Manager manager : managers)
			{
				if(manager.getId() > maxId)
				{
					maxId = manager.getId();
				}
			}
				
			if(maxId == -1)
			{
				maxId = 0;
			}
				
			newUser.setId(maxId + 1);
			Manager managerUser = new Manager(newUser);
			managers.add(managerUser);
				
			filePath = Paths.get(contextPath, "/managers.json");
			Manager[] updatedUserArray = managers.toArray(new Manager[0]);
			updatedJsonData = gson.toJson(updatedUserArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newUser;	
	}
	public ArrayList<Manager> getAvailableManagers()
	{
		ArrayList<Manager> availableManagers = new ArrayList<Manager>();
		for(Manager manager: managers)
		{
			if(manager.getFactoryId()==0)
				availableManagers.add(manager);
		}
		return availableManagers;
	}
	
	
	
	public Admin GetAdminById(int id) {
		for(Admin a : admins)
		{
			if(a.getId() == id)
			{
				return a;
			}
		}
		
		return null;
	}
	
	public Employee GetEmployeeById(int id) {
		for(Employee e : employees)
		{
			if(e.getId() == id)
			{
				return e;
			}
		}
		
		return null;
	}
	
	public Manager updateManager(Manager updatedManager,String contextPath )
	{
		int i = -1;
		for(Manager manager:managers)
		{
			i++;
			if(manager.getId()==updatedManager.getId())
			{
				manager.setFactoryId(updatedManager.getFactoryId());
				managers.remove(manager);
				managers.add(i, manager);
				
				
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/managers.json");
				Manager[] updateManagerArray = managers.toArray(new Manager[0]);
				updatedJsonData = gson.toJson(updateManagerArray);
				
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            
	            
	            
				return manager;
			}
		}
		return null;
	}
	
	public Customer GetCustomerById(int id) {
		for(Customer c : customers)
		{
			if(c.getId() == id)
			{
				return c;
			}
		}
		
		return null;
	}
	public Manager GetManagerById(int id) {
		for(Manager m : managers)
		{
			if(m.getId() == id)
			{
				return m;
			}
		}
		
		return null;
	}
	
	public Customer updateCustomer(Customer newCustomer, String contextPath) {
		
		int i = -1;
		for(Customer customer : customers)
		{
			i++;
		    if(customer.getId() == newCustomer.getId())
		    {
		    	customer = newCustomer;
		    	
		    	customers.remove(i);
		    	customers.add(i, newCustomer);
						
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/customers.json");
				Customer[] updateCustomerArray = customers.toArray(new Customer[0]);
				updatedJsonData = gson.toJson(updateCustomerArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            return newCustomer;
		    }

		}
		
		return null;	
	}
	
	public Admin updateAdmin(Admin newAdmin, String contextPath) {
		
		int i = -1;
		for(Admin admin : admins)
		{
			i++;
		    if(admin.getId() == newAdmin.getId())
		    {
		    	admin = newAdmin;
		    	
		    	admins.remove(i);
		    	admins.add(i, newAdmin);
						
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/admins.json");
				Admin[] updateAdminArray = admins.toArray(new Admin[0]);
				updatedJsonData = gson.toJson(updateAdminArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            return newAdmin;
		    }

		}
		
		return null;	
	}
	
	public Employee updateEmployee(Employee newEmployee, String contextPath) {
		
		int i = -1;
		for(Employee employee : employees)
		{
			i++;
		    if(employee.getId() == newEmployee.getId())
		    {
		    	employee = newEmployee;
		    	
		    	employees.remove(i);
		    	employees.add(i, newEmployee);
						
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/employees.json");
				Employee[] updateEmployeeArray = employees.toArray(new Employee[0]);
				updatedJsonData = gson.toJson(updateEmployeeArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            return newEmployee;
		    }

		}
		
		return null;	
	}
}

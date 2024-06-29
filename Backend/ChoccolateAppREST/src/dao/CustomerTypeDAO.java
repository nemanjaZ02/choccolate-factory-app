package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.Gson;

import beans.CustomerType;

public class CustomerTypeDAO {
private ArrayList<CustomerType> customerTypes = new ArrayList<CustomerType>();
	
	public CustomerTypeDAO() {		
	}
	
	public CustomerTypeDAO(String contextPath) {
		loadCustomerTypes(contextPath);
	}
	
	public ArrayList<CustomerType> getAll() {
		return customerTypes;
	}
	
	public void loadCustomerTypes(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "customerTypes.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            CustomerType[] customerTypeArray = gson.fromJson(jsonData, CustomerType[].class);
            if (customerTypeArray != null) {
                for (CustomerType c : customerTypeArray) {
                	customerTypes.add(c);
                }
                
                Collections.sort(customerTypes, new Comparator<CustomerType>() {
                    @Override
                    public int compare(CustomerType c1, CustomerType c2) {
                        return Integer.compare(c2.getRequiredPoints(), c1.getRequiredPoints());
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public CustomerType getCustomerType(double points) {
		
		for(CustomerType ct : customerTypes)
		{
			if(points >= ct.getRequiredPoints())
			{
				return ct;
			}
		}
		
		return customerTypes.get(customerTypes.size() - 1);
	}
}

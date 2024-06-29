package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Customer;
import beans.Manager;
import beans.Purchase;
import enums.PurchaseState;

public class PurchaseDAO {
	private ArrayList<Purchase> purchases = new ArrayList<Purchase>();
	
	public PurchaseDAO() {		
	}
	
	public PurchaseDAO(String contextPath) {
		loadPurchases(contextPath);
	}
	
	public ArrayList<Purchase> getAll() {
		return purchases;
	}
	
	public void loadPurchases(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "purchases.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Purchase[] purchasesArray = gson.fromJson(jsonData, Purchase[].class);
            if (purchasesArray != null) {
                for (Purchase p : purchasesArray) {
                    purchases.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Purchase savePurchase(Purchase newPurchase, String contextPath)
	{		
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

			int maxId = -1;
			for(Purchase p : purchases)
			{
				if(p.getId() > maxId)
				{
					maxId = p.getId();
				}
			}
				
			if(maxId == -1)
			{
				maxId = 0;
			}
				
			newPurchase.setId(maxId + 1);
			purchases.add(newPurchase);
			filePath = Paths.get(contextPath, "/purchases.json");
			Purchase[] updatePurchaseArray = purchases.toArray(new Purchase[0]);
			updatedJsonData = gson.toJson(updatePurchaseArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newPurchase;	
	}
	public ArrayList<Purchase> getAllForManager(Manager manager)
	{
		ArrayList<Purchase> purchasesForManager = new ArrayList<Purchase>();
		
		
		
		
		
		for(Purchase p:purchases)
		{
			if(p.getFactoryId()==manager.getFactoryId())
			{
				purchasesForManager.add(p);
			}
		}
		return purchasesForManager;
	}
	public ArrayList<Purchase> getAllForCustomer(Customer customer)
	{
		ArrayList<Purchase> purchasesForCustomer = new ArrayList<Purchase>();	
		
		for(Purchase p:purchases)
		{
			if(p.getCustomer().getId()==customer.getId())
			{
				purchasesForCustomer.add(p);
			}
		}
		return purchasesForCustomer;
	}
	
	public Purchase getById(int id)
	{
		for(Purchase p:purchases)
		{
			if(p.getId()==id)
			{
				return p;
			}
		}
		return null;
	}

	public Purchase updatePurchaseSatusByManager(Purchase newPurchase, String contextPath)
	{
		
	
		for(Purchase purchase: purchases)
		{
			//int i = -1;
			if(purchase.getId()==newPurchase.getId())
			{
				
			//	i++;
				
				purchase.setState(newPurchase.getState());
				purchase.setDeclineReason(newPurchase.getDeclineReason());
				
			
			//	purchases.remove(purchase);
			//	purchases.add(i,purchase);			
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/purchases.json");
				Purchase[] updatePurchaseArray = purchases.toArray(new Purchase[0]);
				updatedJsonData = gson.toJson(updatePurchaseArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            return purchase;
			}
		}
		return null;
	}
}

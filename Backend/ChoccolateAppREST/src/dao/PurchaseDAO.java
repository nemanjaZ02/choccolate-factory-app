package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Purchase;

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
}

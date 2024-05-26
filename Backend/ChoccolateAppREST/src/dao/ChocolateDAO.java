package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import beans.Chocolate;
import enums.ChocolateStatus;


public class ChocolateDAO {

	private ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
	
	public ChocolateDAO(String contextPath)
	{
		loadChocolates(contextPath);
	}
	
	
	public ArrayList<Chocolate> findAllChocolates() {
		return chocolates;
	}
	
	
	public Chocolate saveChocolate(Chocolate newChocolate, String contextPath)
	{
		
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

			int maxId = -1;
			for(Chocolate chocolate : chocolates)
			{
				if(chocolate.getId() > maxId)
				{
					maxId = chocolate.getId();
				}
			}
				
			if(maxId == -1)
			{
				maxId = 0;
			}
				
			newChocolate.setId(maxId + 1);
			newChocolate.setQuantity(0);
			newChocolate.setStatus(ChocolateStatus.NotInStock);
			chocolates.add(newChocolate);
				
			filePath = Paths.get(contextPath, "/chocolates.json");
			Chocolate[] updateChocolateArray = chocolates.toArray(new Chocolate[0]);
			updatedJsonData = gson.toJson(updateChocolateArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newChocolate;	
	}
	
	public void loadChocolates(String contextPath)
	{
		try {
            Path filePath = Paths.get(contextPath, "chocolates.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Chocolate[] chocolateArray = gson.fromJson(jsonData, Chocolate[].class);
            if (chocolateArray != null) {
                for (Chocolate chocolate : chocolateArray) {
                    chocolates.add(chocolate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	
	
	
	
}

package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Adress;
import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Location;
import beans.WorkTime;
import enums.ChocolateStatus;
import enums.WorkingStatus;

public class ChocolateFactoryDAO {
	private ArrayList<ChocolateFactory> factories = new ArrayList<ChocolateFactory>();
	
	public ChocolateFactoryDAO() {
		
		
	}
	
	public ChocolateFactoryDAO(String contextPath) {
		loadChocolateFactories(contextPath);
	}
	
	public ArrayList<ChocolateFactory> getAll() {
		return factories;
	}
	
	public void loadChocolateFactories(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "chocolateFactories.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            ChocolateFactory[] factoriesArray = gson.fromJson(jsonData, ChocolateFactory[].class);
            if (factoriesArray != null) {
                for (ChocolateFactory cf : factoriesArray) {
                    factories.add(cf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ChocolateFactory getById(int id) {
		for(ChocolateFactory c : factories)
		{
			if(c.getId() == id)
			{
				return c;
			}
		}
		
		return null;
	}
	public void addChocolateToFactory(Chocolate newChocolate, String contextPath)
	{
		ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
		
		int id = -1;
		for(ChocolateFactory factory: factories)
		{
			id ++;
			if(factory.getId()==newChocolate.getFactoryId())
			{
				chocolates = factory.getChocolates();
				chocolates.add(newChocolate);
				factory.setChocolates(chocolates);
			
				factories.remove(id);
				factories.add(id, factory);
				
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/chocolateFactories.json");
				ChocolateFactory[] updateChocolateFactoryArray = factories.toArray(new ChocolateFactory[0]);
				updatedJsonData = gson.toJson(updateChocolateFactoryArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}

				return;
			}
		}
	}
	
	public void updateChocolateInFactory(Chocolate updatedChocolate, String contextPath)
	{
		ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
		
		for(ChocolateFactory factory: factories)
		{
			if(factory.getId()==updatedChocolate.getFactoryId())
			{
				chocolates = factory.getChocolates();
				
				int i = -1;
				for(Chocolate chocolate:chocolates)
				{
					i++;
					if(chocolate.getId()==updatedChocolate.getId())
					{
						chocolates.remove(chocolate);
						chocolates.add(i, updatedChocolate);
						break;
					}
				}
				
				factory.setChocolates(chocolates);
			
				factories.remove(factory.getId()-1);
				factories.add(factory);
				
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/chocolateFactories.json");
				ChocolateFactory[] updateChocolateFactoryArray = factories.toArray(new ChocolateFactory[0]);
				updatedJsonData = gson.toJson(updateChocolateFactoryArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}

				return;
			}
		}
	}
	
	public ChocolateFactory deleteChocolateFromFactory(Chocolate chocolate, String contextPath) {
		ChocolateFactory factory = new ChocolateFactory();
		
		for(ChocolateFactory cf : factories)
		{
			if(cf.getId() == chocolate.getFactoryId())
			{
				factory = cf;
			}
		}
		
		if(factory == null)
			return null;
		
		ArrayList<Chocolate> chocolates = factory.getChocolates();
		
		int i = -1;
		for(Chocolate c : chocolates)
		{
			i++;
			if(c.getId() == chocolate.getId())
			{
				c.setDeleted(true);
				chocolates.set(i, c);	
			}
		}
		
		factory.setChocolates(chocolates);
		
		Gson gson = new Gson();  
		String updatedJsonData;
		
		Path filePath = Paths.get(contextPath, "/chocolateFactories.json");
		ChocolateFactory[] updateChocolateFactoryArray = factories.toArray(new ChocolateFactory[0]);
		updatedJsonData = gson.toJson(updateChocolateFactoryArray);
		
        try {
			Files.write(filePath, updatedJsonData.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return factory;	
	}
}

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
	public ChocolateFactory saveChocolateFactory(ChocolateFactory newChocolateFactory, String contextPath)
	{
		
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

			int maxId = -1;
			for(ChocolateFactory chocolateFactory : factories)
			{
				if(newChocolateFactory.getName().equals(chocolateFactory.getName()))
				{
					return null;
				}
				if(chocolateFactory.getId() > maxId)
				{
					maxId = chocolateFactory.getId();
				}
			}
				
			if(maxId == -1)
			{
				maxId = 0;
			}
				
			newChocolateFactory.setId(maxId + 1);
			newChocolateFactory.setChocolates(new ArrayList<Chocolate>());
			factories.add(newChocolateFactory);
			filePath = Paths.get(contextPath, "/chocolateFactories.json");
			ChocolateFactory[] updateChocolateFactoryArray = factories.toArray(new ChocolateFactory[0]);
			updatedJsonData = gson.toJson(updateChocolateFactoryArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newChocolateFactory;	
	}
	
	public boolean factoryAlreadyHasChocolate(Chocolate newChocolate)
	{
		ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
		
		int id = -1;
		for(ChocolateFactory factory: factories)
		{
			id ++;
			if(factory.getId()==newChocolate.getFactoryId())
			{
				chocolates = factory.getChocolates();
				
				for(Chocolate c : chocolates)
				{
					if(c.getName().equals(newChocolate.getName()) && !c.getIsDeleted())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Chocolate addChocolateToFactory(Chocolate newChocolate, String contextPath)
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

				return newChocolate;
			}
		}
		
		return null;
	}
	
	public void updateChocolateInFactory(Chocolate updatedChocolate, String contextPath)
	{
		ArrayList<Chocolate> chocolates = new ArrayList<Chocolate>();
		int j = -1;
		for(ChocolateFactory factory: factories)
		{
			j++;
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
			
				factories.remove(factory);
				factories.add(j,factory);
				
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

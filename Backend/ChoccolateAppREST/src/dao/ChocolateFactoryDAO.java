package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;

import com.google.gson.Gson;

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
		
		/*private int id;
		private String name;
		private ArrayList<Chocolate> chocolates;
		private WorkTime workTime;
		private WorkingStatus status;
		private Location location;
		private String logo;
		private double rating;*/
		
		/*private int id;
		private int factoryId;
		private  String name;
		private double price;
		private String type;
		private String kind;
		private ChocolateStatus status;
		private double weight; 
		private String description;
		private String image;
		private int quantity;*/
		
		
		ArrayList<Chocolate> chocolates1 = new ArrayList<Chocolate>();
		chocolates1.add(new Chocolate(1, 1, "cokolada11", 100, "crna", "zdrava", ChocolateStatus.NotInStock, 50, "dobra", "@/../public/Images/chocolate.jpg", 0));
		chocolates1.add(new Chocolate(2, 1, "cokolada21", 200, "bela", "slatka", ChocolateStatus.NotInStock, 50, "sa jagodama", "@/../public/Images/chocolate.jpg", 0));
		
		ArrayList<Chocolate> chocolates2 = new ArrayList<Chocolate>();
		chocolates2.add(new Chocolate(1, 2, "cokolada12", 100, "crna", "zdrava", ChocolateStatus.NotInStock, 50, "dobra", "@/../public/Images/chocolate.jpg", 0));
		chocolates2.add(new Chocolate(2, 2, "cokolada22", 200, "bela", "slatka", ChocolateStatus.NotInStock, 50, "sa jagodama", "@/../public/Images/chocolate.jpg", 0));
		
		ArrayList<Chocolate> chocolates3 = new ArrayList<Chocolate>();
		chocolates3.add(new Chocolate(1, 3, "cokolada13", 100, "crna", "zdrava", ChocolateStatus.NotInStock, 50, "dobra", "@/../public/Images/chocolate.jpg", 0));
		chocolates3.add(new Chocolate(2, 3, "cokolada23", 200, "bela", "slatka", ChocolateStatus.NotInStock, 50, "sa jagodama", "@/../public/Images/chocolate.jpg", 0));
		
		factories.add(new ChocolateFactory(1, "fabrika1", chocolates1, new WorkTime(new Time(5, 15, 0), new Time(18, 15, 0)), WorkingStatus.OPEN, new Location(), "@/../public/Images/logo.jpg", 5.2));
		factories.add(new ChocolateFactory(2, "fabrika2", chocolates2, new WorkTime(new Time(5, 15, 0), new Time(18, 15, 0)), WorkingStatus.OPEN, new Location(), "@/../public/Images/logo.jpg", 5.2));
		factories.add(new ChocolateFactory(3, "fabrika3", chocolates3, new WorkTime(new Time(5, 15, 0), new Time(18, 15, 0)), WorkingStatus.OPEN, new Location(), "@/../public/Images/logo.jpg", 5.2));
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
}

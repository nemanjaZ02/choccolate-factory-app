package services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimbusds.jose.shaded.json.parser.ParseException;

import beans.Cart;
import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Customer;
import beans.Manager;
import beans.Purchase;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.CustomerTypeDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import enums.PurchaseState;
import jwt.JwtUtils;


@Path("/purchases")
public class PurchaseService {
	
	@Context
	ServletContext ctx;
	
	public PurchaseService(){
		
	}
	
	@PostConstruct
	public void init()
	{
		String contextPath = ctx.getRealPath("");
		
		if(ctx.getAttribute("purchaseDAO") == null)
		{
			ctx.setAttribute("purchaseDAO", new PurchaseDAO(contextPath));
		}	
		
		if(ctx.getAttribute("chocolateDAO") == null)
		{
			ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
		}	
		
		if(ctx.getAttribute("chocolateFactoryDAO") == null)
		{
			ctx.setAttribute("chocolateFactoryDAO", new ChocolateFactoryDAO(contextPath));
		}
		
		if(ctx.getAttribute("userDAO") == null)
		{
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		
		if(ctx.getAttribute("customerTypeDAO") == null)
		{
			ctx.setAttribute("customerTypeDAO", new CustomerTypeDAO(contextPath));
		}
	}
	
	@GET
	@Path("/getPurchases")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChocolates()
	{
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		ArrayList<Purchase> purchases = dao.getAll();
		
		if(purchases == null)
		{
			return Response.status(205).entity("There are no chocolates to show").build();
		}
		else
		{
			return Response.status(200).build();
		}
	}
	
	@OPTIONS
	@Path("/addPurchase")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsAddChocolate() {
		return true;
	}
	
	@POST
	@Path("/addPurchase")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPurchase(Cart cart, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
        if (!JwtUtils.isCustomer(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only customers can make purchases").build();
        }
        
        ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		ChocolateFactoryDAO chocolateFactoryDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		PurchaseDAO purchaseDAO = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		for(Chocolate check : chocolateDAO.findAllChocolates())
		{
			for(Chocolate c : cart.getChocolates())
			{		
				if(c.getId() == check.getId() && (c.getQuantity() > check.getQuantity() || c.getQuantity() <= 0))
				{
					return Response.status(405).entity("There is not enough chocolates for this purchase").build();
				}
			}
		}
		
		for(Chocolate c1 : cart.getChocolates())
		{		
			for(Chocolate c2 : cart.getChocolates())
			{		
				if(c1.getFactoryId() != c2.getFactoryId())
				{
					return Response.status(405).entity("Not all chocolates are from the same factory").build();
				}
			}
		}

		if(cart == null || cart.getPrice() == 0 || cart.getChocolates() == null)
		{
			return Response.status(405).entity("invalid input").build();
		}
		else
		{
			String contextPath = ctx.getRealPath("");
			
			Purchase newPurchase = new Purchase();
			newPurchase.setChocolates(cart.getChocolates());
			
			Chocolate cfc = new Chocolate();
			for(Chocolate check : chocolateDAO.findAllChocolates())
			{
				for(Chocolate c : newPurchase.getChocolates())
				{
					if(c.getId() == check.getId())
					{
						cfc = c;
						check.setQuantity(check.getQuantity() - c.getQuantity());
						chocolateDAO.updateChocolate(check, contextPath);
						chocolateFactoryDAO.updateChocolateInFactory(check, contextPath);
					}
				}
			}
			
			Customer customer = userDAO.GetCustomerById(JwtUtils.getUserId(authorizationHeader));
			
			newPurchase.setFactoryId(cfc.getFactoryId());
			newPurchase.setDateAndTime(LocalDateTime.now());
			int customerId = JwtUtils.getUserId(authorizationHeader);
			newPurchase.setCustomer(customer);
			newPurchase.setState(PurchaseState.Processing);
				
			double price = 0;
			for(Chocolate c : cart.getChocolates())
			{
				price += c.getQuantity() * c.getPrice();
			}
			price = price - (price * customer.getType().getDiscount() / 100); 
			newPurchase.setPrice(price);
			
			Purchase purchase = purchaseDAO.savePurchase(newPurchase, contextPath);
			return Response.status(200).entity(purchase).build();
		}	
	}
	@GET
	@Path("/getPurchasesForManager/{managerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchacesForManager(@PathParam("managerId") int id)
	{
		
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		Manager manager = userDao.GetManagerById(id);
		if(manager == null)
		{
			return Response.status(405).entity("Unauthorized: Only managers can see their purchases").build();
		}
		
		ArrayList<Purchase> purchases = dao.getAllForManager(manager);
		
		if(purchases == null)
		{
			return Response.status(205).entity("There are no purchases to show").build();
		}
		else
		{
			return Response.status(200).entity(purchases).build();
		}
	}
	
	@GET
	@Path("/getById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchaseById(@PathParam("id") int id)
	{
	        
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		Purchase purchase = dao.getById(id);
		
		
		if(purchase == null)
		{
			return Response.status(205).entity("Purchase with this id doesn't exist").build();
		}
		else
		{
			return Response.status(200).entity(purchase).build();
		}
	}
	@OPTIONS
	@Path("/updatePurchaseStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updatePurchaseStatusByManager() {
		return true;
	}
	
	@PUT
	@Path("/updatePurchaseStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePurchaseStatus(Purchase newPurchase, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		
		if (!(JwtUtils.isManager(authorizationHeader)||JwtUtils.isCustomer(authorizationHeader))) {
            return Response.status(401).entity("Unauthorized: Only managers or customers can change purchase state").build();
        }
		if(!(newPurchase.getState().equals(PurchaseState.Accepted) || newPurchase.getState().equals(PurchaseState.Declined)) && JwtUtils.isManager(authorizationHeader))
		{
			 return Response.status(401).entity("Invalid state: Managers can only accept and decline purchases").build();
		}
		if(!(newPurchase.getState().equals(PurchaseState.Canceled)) && JwtUtils.isCustomer(authorizationHeader))
		{
			 return Response.status(401).entity("Invalid state: Customers can only cancel purchases").build();
		}
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		CustomerTypeDAO customerTypeDAO = (CustomerTypeDAO) ctx.getAttribute("customerTypeDAO");
		String contextPath = ctx.getRealPath("");
		Purchase purchase = dao.updatePurchaseSatusByManager(newPurchase, contextPath);
		
		if(purchase==null)
		{
			return Response.status(405).entity("Purchase with this id doesn't exist").build();
		}
		
		Customer customer = purchase.getCustomer();
		if(newPurchase.getState() == PurchaseState.Canceled)
		{	
			customer.setPoints(customer.getPoints() - (newPurchase.getPrice()/1000 * 133 * 4));	
		}
		else if(newPurchase.getState() == PurchaseState.Accepted)
		{
			customer.setPoints(customer.getPoints() + (newPurchase.getPrice()/1000 * 133));	
		}
		
		customer.setType(customerTypeDAO.getCustomerType(customer.getPoints()));
		userDAO.updateCustomer(customer, contextPath);
		return Response.status(200).entity(purchase).build();
	}
	
	@GET
	@Path("/getPurchasesForCustomer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchacesForCustomer(@PathParam("customerId") int id)
	{
		
		PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		Customer customer = userDao.GetCustomerById(id);
		if(customer==null)
		{
			return Response.status(405).entity("Unauthorized: Only customers can see their purchases").build();
		}
		
		ArrayList<Purchase> purchases = dao.getAllForCustomer(customer);
		
		if(purchases == null)
		{
			return Response.status(205).entity("There are no purchases to show").build();
		}
		else
		{
			return Response.status(200).entity(purchases).build();
		}
	}
	
}

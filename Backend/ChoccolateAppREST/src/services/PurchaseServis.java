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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimbusds.jose.shaded.json.parser.ParseException;

import beans.Cart;
import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Customer;
import beans.Purchase;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import enums.PurchaseState;
import jwt.JwtUtils;


@Path("/purchases")
public class PurchaseServis {
	
	@Context
	ServletContext ctx;
	
	public PurchaseServis(){
		
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
			
			newPurchase.setFactoryId(cfc.getFactoryId());
			newPurchase.setDateAndTime(LocalDateTime.now());
			newPurchase.setPrice(cart.getPrice());
			int customerId = JwtUtils.getUserId(authorizationHeader);
			newPurchase.setCustomerId(customerId);
			newPurchase.setState(PurchaseState.Processing);
			
			Customer customer = userDAO.GetCustomerById(customerId);
			customer.setPoints(customer.getPoints() + (newPurchase.getPrice()/1000 * 133 * 4));
			
			//proveriti CustomerType!
			
			userDAO.updateCustomer(customer, contextPath);
			
			Purchase purchase = purchaseDAO.savePurchase(newPurchase, contextPath);
			return Response.status(200).entity(purchase).build();
		}	
	}
}

package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.ChocolateFactory;
import dao.ChocolateFactoryDAO;
import dao.UserDAO;

@Path("/ChocolateFactoryService")
public class ChocolateFactoryService {
	@Context
	ServletContext ctx;
	
	public ChocolateFactoryService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("chocolateFactoryDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("chocolateFactoryDAO", new ChocolateFactoryDAO(contextPath));
		}
		if (ctx.getAttribute("userDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ChocolateFactory> getAll() {
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		return dao.getAll();	
	}	
	
	@GET
	@Path("/getById/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ChocolateFactory getById(@PathParam("factoryId") int id) {
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		return dao.getById(id);	
	}
	
	@OPTIONS
	@Path("/addChocolate/{loggedInUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsAddChocolate() {
		return true;
	}
	
	@PUT
	@Path("/addChocolate/{loggedInUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addChocolateToFactory(@PathParam("loggedInUserId") int userId, Chocolate newChocolate )
	{
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		if(!userDAO.isUserManager(userId))
		{
			return Response.status(405).entity("trying to bypass ranking").build(); 
		}
		ChocolateFactoryDAO chocolateDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		String contextPath = ctx.getRealPath("");
		chocolateDAO.addChocolateToFactory(newChocolate, contextPath);
		return Response.status(200).build();
	}
}

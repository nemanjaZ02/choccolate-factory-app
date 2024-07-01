package services;

import java.util.ArrayList;

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

import beans.Chocolate;
import beans.ChocolateFactory;
import beans.Employee;
import beans.Manager;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.UserDAO;
import jwt.JwtUtils;

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
		if(ctx.getAttribute("userDAO")==null)
		{
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
	
	@GET
	@Path("/getFactoryById/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFactoryById(@PathParam("factoryId") int id) {
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		ChocolateFactory factory = dao.getById(id);
		if(factory == null)
		{
			return Response.status(405).entity("Factory with this id doens't exist").build();
		}
		
		return Response.status(200).entity(factory).build();
		 	
	}
	
	@OPTIONS
	@Path("/updateChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateChocolateInFactory() {
		return true;
	}
	
	@PUT()
	@Path("/updateChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateChocolateInFactory(Chocolate newChocolate, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isManager(authorizationHeader) && !JwtUtils.isEmployee(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers and employees can edit chocolates").build();
        }
		
		ChocolateFactoryDAO chocolateDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		String contextPath = ctx.getRealPath("");
		chocolateDAO.updateChocolateInFactory(newChocolate, contextPath);
		return Response.status(200).build();
	}
	@OPTIONS
	@Path("/addChocolateFactory")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addChocolateFactory() {
		return true;
	}
	@POST
	@Path("/addChocolateFactory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addChocolateFactory(ChocolateFactory newChocolateFactory, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
        if (!JwtUtils.isAdministrator(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only admins can add chocolate factories").build();
        }

		if(newChocolateFactory.getName()=="" || newChocolateFactory.getWorkTime().toString()=="" || newChocolateFactory.getLocation().getAdress().getCountry()=="")
		{
			return Response.status(405).entity("invalid input").build();
		}
		else
		{
			ChocolateFactoryDAO chocolateFactoryDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
			
			String contextPath = ctx.getRealPath("");
			ChocolateFactory chocolateFactory = chocolateFactoryDAO.saveChocolateFactory(newChocolateFactory, contextPath);
			if(chocolateFactory == null)
			{
				return Response.status(405).entity("Chocolate Factory with that name already exists").build();
			}
			
			
		
			return Response.status(200).entity(chocolateFactory).build();
		}
		
	}
	@GET
	@Path("/getAvailableManagers")
	@Produces(MediaType.APPLICATION_JSON)
	public 	ArrayList<Manager> getAvailableManagers() {
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
	   return  dao.getAvailableManagers();	
	}
	@OPTIONS
	@Path("/updateManager")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateManager() {
		return true;
	}
	
	@PUT()
	@Path("/updateManager")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateManager(Manager newManager, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isAdministrator(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only admins can edit managers").build();
        }
		
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		String contextPath = ctx.getRealPath("");
		if(userDAO.updateManager(newManager, contextPath) == null)
		{
			return Response.status(405).entity("This manager does not exist!").build();
		}
		userDAO.updateManager(newManager, contextPath);
		return Response.status(200).build();
	}
	
	
	@OPTIONS
	@Path("/updateChocolateFactoryStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsUpdateChocolateFactory() {
		return true;
	}
	@POST
	@Path("/updateChocolateFactoryStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response corsUpdateChocolateFactory(ChocolateFactory newChocolateFactory)
	{
		String contextPath = ctx.getRealPath("");
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		dao.updateChocolateFactoryStatus(newChocolateFactory, contextPath);
		return Response.status(200).entity(newChocolateFactory).build();
	}
	
	@OPTIONS
	@Path("/deleteChocolateFactory")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsDeleteChocolateFactory() {
		return true;
	}
	@POST
	@Path("/deleteChocolateFactory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteChocolateFactory(ChocolateFactory chocolateFactory, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isAdministrator(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only admins can delete factories").build();
        }
		
		String contextPath = ctx.getRealPath("");
		ChocolateFactoryDAO dao = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		if(dao.deleteChocolateFactory(chocolateFactory, contextPath) == null)
		{
			return Response.status(404).entity("Factory not found").build();
		}
		
		for(Manager m : userDAO.findAllManagers())
		{
			if(m.getFactoryId() == chocolateFactory.getId())
			{
				m.setFactoryId(0);
				userDAO.updateManager(m, contextPath);
			}
		}
		
		for(Employee e : userDAO.findAllEmployees())
		{
			if(e.getFactoryId() == chocolateFactory.getId())
			{
				e.setFactoryId(0);
				userDAO.updateEmployee(e, contextPath);
			}
		}
		
		return Response.status(200).entity(chocolateFactory).build();
	}
	
}

package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
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
	@Path("/addChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsAddChocolate() {
		return true;
	}
	
	@PUT
	@Path("/addChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addChocolateToFactory(Chocolate newChocolate, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isManager(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers can add chocolates").build();
        }
		
		ChocolateFactoryDAO chocolateDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		String contextPath = ctx.getRealPath("");
		chocolateDAO.addChocolateToFactory(newChocolate, contextPath);
		return Response.status(200).build();
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
	
}

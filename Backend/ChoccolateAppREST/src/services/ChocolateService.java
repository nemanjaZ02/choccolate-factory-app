package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.parser.ParseException;

import beans.Chocolate;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.UserDAO;
import jwt.JwtUtils;

@Path("/chocolates")
public class ChocolateService {

	@Context
	ServletContext ctx;
	
	public ChocolateService()
	{
		
	}
	
	@PostConstruct
	public void init()
	{
		String contextPath = ctx.getRealPath("");
		System.out.println(contextPath);
		
		if(ctx.getAttribute("chocolateDAO")==null)
		{
			ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
		}
		
		if(ctx.getAttribute("chocolateFactoryDAO")==null)
		{
			ctx.setAttribute("chocolateFactoryDAO", new ChocolateFactoryDAO(contextPath));
		}	
	}
	
	@GET
	@Path("/getChocolates")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChocolates()
	{
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		ArrayList<Chocolate> chocolates = dao.findAllChocolates();
		
		if(chocolates == null)
		{
			return Response.status(205).entity("There are no chocolates to show").build();
		}
		else
		{
			return Response.status(200).build();
		}
	}
	
	@OPTIONS
	@Path("/addChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsAddChocolate() {
		return true;
	}
	
	@POST
	@Path("/addChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addChocolate(Chocolate newChocolate, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
        if (!JwtUtils.isManager(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers can add chocolates").build();
        }

		if(newChocolate.getName()=="" || newChocolate.getKind()=="" || newChocolate.getDescription()=="" || newChocolate.getPrice()<=0 || newChocolate.getImage() == "" || newChocolate.getWeight()<=0 || newChocolate.getType()=="")
		{
			return Response.status(405).entity("invalid input").build();
		}
		else
		{
			ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
			String contextPath = ctx.getRealPath("");
			Chocolate choco = chocolateDAO.saveChocolate(newChocolate, contextPath);
			return Response.status(200).entity(choco).build();
		}
		
	}
	
	@OPTIONS
	@Path("/updateChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsUpdateChocolate() {
		return true;
	}
	
	@PUT
	@Path("/updateChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateChocolate(Chocolate updatedChocolate, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isManager(authorizationHeader) && !JwtUtils.isEmployee(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers or emplyees can update chocolates").build();
        }
		
		if(updatedChocolate.getName()=="" || updatedChocolate.getKind()=="" || updatedChocolate.getDescription()=="" || updatedChocolate.getPrice()<=0 || updatedChocolate.getImage() == "" || updatedChocolate.getWeight()<=0 || updatedChocolate.getType()=="")
		{
			return Response.status(405).entity("invalid input").build();
		}
		else
		{
			ChocolateDAO chocolateDAO = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
			String contextPath = ctx.getRealPath("");
			Chocolate editedChocolate= chocolateDAO.updateChocolate(updatedChocolate, contextPath);
			return Response.status(200).entity(editedChocolate).build();
		}
		
	}
	
	@GET
	@Path("/getChocolate/{chocolateId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChocolateById(@PathParam("chocolateId") int id)
	{
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		
		Chocolate chocolate =dao.getChocolateById(id);
		if(chocolate==null)
		{
			return Response.status(405).entity("there is no chocolate with this id").build();
		}
		
		return Response.status(200).entity(chocolate).build();	
	}
	
	@OPTIONS
	@Path("/deleteChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsDeleteChocolate() {
		return true;
	}
	@POST
	@Path("/deleteChocolate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteChocolate(Chocolate chocolate, @HeaderParam("Authorization") String authorizationHeader)  throws ParseException 
	{
		ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
		ChocolateFactoryDAO factoryDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		String contextPath = ctx.getRealPath("");
		
		if (!JwtUtils.isManager(authorizationHeader) && !JwtUtils.isEmployee(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers or emplyees can update chocolates").build();
        }
		
		if(dao.deleteChocolate(chocolate, contextPath) == null) {
			return Response.status(405).entity("there is no chocolate with this id").build();
		}
		
		if(factoryDAO.deleteChocolateFromFactory(chocolate, contextPath) == null) {
			return Response.status(405).entity("there is no chocolate with this id").build();
		}
		
		return Response.status(200).build();
	}
}

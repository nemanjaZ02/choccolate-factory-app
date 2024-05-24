package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import dao.ChocolateDAO;

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
		if(ctx.getAttribute("chocolateDAO")==null)
		{
			String contextPath = ctx.getRealPath("");
			System.out.println(contextPath);
			ctx.setAttribute("chocolateDAO", new ChocolateDAO(contextPath));
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
	public Response addChocolate(Chocolate newChocolate)
	{
		 
		if(newChocolate.getName()=="" || newChocolate.getKind()=="" || newChocolate.getDescription()=="" || newChocolate.getPrice()<=0 || newChocolate.getImage() == "" || newChocolate.getWeight()<=0 || newChocolate.getType()=="")
		{
			return Response.status(205).entity("invalid input").build();
		}
		else
		{
			ChocolateDAO dao = (ChocolateDAO) ctx.getAttribute("chocolateDAO");
			String contextPath = ctx.getRealPath("");
		    dao.saveChocolate(newChocolate, contextPath);
			return Response.status(200).build();
		}
		
	}
	
	
}

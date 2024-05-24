package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.ChocolateFactory;
import dao.ChocolateFactoryDAO;

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
}

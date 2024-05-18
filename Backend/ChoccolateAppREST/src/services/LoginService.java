package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.UserDAO;

@Path("")
public class LoginService {
	@Context
	ServletContext ctx;
	
	public LoginService() {
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("productDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
	    	System.out.println(contextPath);
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@OPTIONS
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsLogin() {
		return true;
	}
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		if (loggedUser == null) {
			return Response.status(400).entity("Invalid username and/or password").build();
		}
		ctx.setAttribute("loggedUser", loggedUser);
		return Response.status(200).build();
	}
	
	@OPTIONS
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsRegister() {
		return true;
	}
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User registerNewUser(User newUser) {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		return dao.registerNewUser(newUser, contextPath);
	}
	
	@GET
	@Path("/logOut")
	@Produces(MediaType.APPLICATION_JSON)
	public void logOut() {
		ctx.setAttribute("loggedUser", null);
	}
	
	@GET
	@Path("/getLoggedInUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getLoggedInUser(@Context HttpServletRequest request) {
		return (User) ctx.getAttribute("loggedUser");
	}
}

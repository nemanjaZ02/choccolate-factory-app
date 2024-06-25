package services;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

import com.nimbusds.jose.JOSEException;

import beans.Admin;
import beans.Customer;
import beans.Employee;
import beans.Manager;
import beans.User;
import dao.UserDAO;
import enums.Role;
import jwt.JwtConstants;
import jwt.JwtUtils;

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
	public Response login(User user) {
		UserDAO userDao = (UserDAO) ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		if (loggedUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Invalid username and/or password")
                           .build();
        }
        
        try {
            String token = JwtUtils.generateToken(loggedUser, JwtConstants.SECRET_KEY);
            
            return Response.status(Response.Status.OK)
                           .entity(token)
                           .build();
        } catch (JOSEException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to generate JWT token")
                           .build();
        }
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
	public Response registerNewUser(User newUser) {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		if(newUser.getName() == "" || newUser.getSurname() == "" || newUser.getBirthday() == null || newUser.getUsername() == "" || newUser.getPassword() == "" || newUser.getRole() != Role.CUSTOMER || newUser.getGender() == null)
		{
			return Response.status(205).entity("Not all fields are filled/or trying to bypass ranking").build();
		}
		else
		{
			dao.registerNewUser(newUser, contextPath);
			ctx.setAttribute("loggedUser", newUser);
			return Response.status(200).build();
		}	
	}
	@OPTIONS
	@Path("/registerManager")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerNewManager() {
		return true;
	}
	@POST
	@Path("/registerManager")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerNewManager(Manager newUser) {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		if(newUser.getName() == "" || newUser.getSurname() == "" || newUser.getBirthday() == null || newUser.getUsername() == "" || newUser.getPassword() == "" || newUser.getRole() != Role.MANAGER || newUser.getGender() == null)
		{
			return Response.status(205).entity("Not all fields are filled/or trying to bypass ranking").build();
		}
		else
		{
			Manager newManager = dao.registerNewManager(newUser, contextPath);
			return Response.status(200).entity(newManager).build();
		}	
	}
	
	@OPTIONS
	@Path("/getCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsGetCustomer() {
		return true;
	}
	
	@GET
	@Path("/getCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@HeaderParam("Authorization") String authorizationHeader) {
		
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		int id = JwtUtils.getUserId(authorizationHeader);
		Customer c = dao.GetCustomerById(id);
		
		if(c == null)
		{
			return Response.status(404).entity("Customer not found").build();
		}
		
		return Response.status(200).entity(c).build();
	}
}

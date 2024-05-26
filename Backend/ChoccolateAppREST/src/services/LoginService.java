package services;

import java.security.NoSuchAlgorithmException;
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

import com.nimbusds.jose.JOSEException;

import beans.Admin;
import beans.Employee;
import beans.Manager;
import beans.User;
import dao.UserDAO;
import enums.Role;
import jwt.JwtConstants;
import jwt.JwtUtils;
import jwt.LoginResponse;

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

            ctx.setAttribute("loggedUser", loggedUser);
            
            return Response.status(Response.Status.OK)
                           .entity(new LoginResponse(loggedUser, token))
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
	
	@GET
	@Path("/logOut")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logOut() {
		if(ctx.getAttribute("loggedUser") != null)
		{
			ctx.setAttribute("loggedUser", null);
			return Response.status(200).entity("User succesfully logged out").build();
		}
		else
		{
			return Response.status(205).entity("Nobody was logged in").build();
		}		
	}
	
	@GET
	@Path("/getLoggedInUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getLoggedInUser() {
		User user = (User) ctx.getAttribute("loggedUser");
        
        if (user != null) {
            switch (user.getRole()) {
                case MANAGER:
                    return (Manager) user;
                case EMPLOYEE:
                    return (Employee) user;
                case ADMIN:
                    return (Admin) user;
                default:
                    return user;
            }
        } else {
            return null;
        }
	}
	
	
}

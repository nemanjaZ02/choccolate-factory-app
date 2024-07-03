package services;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.parser.ParseException;

import beans.Admin;
import beans.Customer;
import beans.Employee;
import beans.Manager;
import beans.Purchase;
import beans.User;
import dao.CustomerTypeDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import enums.PurchaseState;
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
	    	
	    	if(ctx.getAttribute("userDAO") == null)
			{
	    		ctx.setAttribute("userDAO", new UserDAO(contextPath));
			}
			
			if(ctx.getAttribute("customerTypeDAO") == null)
			{
				ctx.setAttribute("customerTypeDAO", new CustomerTypeDAO(contextPath));
			}
			
			if(ctx.getAttribute("purchaseDAO") == null)
			{
				ctx.setAttribute("purchaseDAO", new PurchaseDAO(contextPath));
			}
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
		if (loggedUser == null || loggedUser.getIsDeleted()) {
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
	public Response registerNewUser(Customer newUser) {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		CustomerTypeDAO customerTypeDAO = (CustomerTypeDAO) ctx.getAttribute("customerTypeDAO");
		
		newUser.setRole(Role.CUSTOMER);
		newUser.setType(customerTypeDAO.getCustomerType(0));
		
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
	public Response registerNewManager(Manager newUser,@HeaderParam("Authorization") String authorizationHeader) throws ParseException {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		if (!JwtUtils.isAdministrator(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only admins can register a new manager").build();
        }
		
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
	@Path("/registerEmployee")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerNewEmployee() {
		return true;
	}
	@POST
	@Path("/registerEmployee")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerNewEmployee(Employee newUser, @HeaderParam("Authorization") String authorizationHeader) throws ParseException {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		if (!JwtUtils.isManager(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only managers can register a new employee").build();
        }
		
		if(newUser.getName() == "" || newUser.getSurname() == "" || newUser.getBirthday() == null || newUser.getUsername() == "" || newUser.getPassword() == "" || newUser.getRole() != Role.EMPLOYEE || newUser.getGender() == null)
		{
			return Response.status(205).entity("Not all fields are filled").build();
		}
		else
		{
			Employee newEmployee = dao.registerNewEmployee(newUser, contextPath);
			return Response.status(200).entity(newEmployee).build();
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
	
	@OPTIONS
	@Path("/updateUser")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsUpdateUser() {
		return true;
	}
	
	@GET
	@Path("/getCustomer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("customerId") int id) {
		
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		Customer customer = dao.GetCustomerById(id);
		if(customer==null)
		{
			return Response.status(405).entity("There is not customer with this id").build();
		}
		else
			
		{
			return Response.status(200).entity(customer).build();
		}
	}
	
	
	@POST
	@Path("/updateUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response corsUpdateUser(User updatedUser, @HeaderParam("Authorization") String authorizationHeader) {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		int id = JwtUtils.getUserId(authorizationHeader);
		
		if(dao.GetAdminById(id) != null)
		{
			Admin a = dao.GetAdminById(id);
			a.update(updatedUser);
			dao.updateAdmin(a, contextPath);
			return Response.status(200).entity(a).build();
		}
		else if (dao.GetCustomerById(id) != null)
		{
			Customer c = dao.GetCustomerById(id);
			c.update(updatedUser);
			dao.updateCustomer(c, contextPath);
			return Response.status(200).entity(c).build();
		}
		else if (dao.GetManagerById(id) != null)
		{
			Manager m = dao.GetManagerById(id);
			m.update(updatedUser);
			dao.updateManager(m, contextPath);
			return Response.status(200).entity(m).build();
		}
		else if (dao.GetEmployeeById(id) != null)
		{
			Employee e = dao.GetEmployeeById(id);
			e.update(updatedUser);
			dao.updateEmployee(e, contextPath);
			return Response.status(200).entity(e).build();
		}
		
		
		return Response.status(404).entity("User not found").build();
	}
	
	@OPTIONS
    @Path("/getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean getAllUsers() {
        return true;
    }
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(@HeaderParam("Authorization") String authorizationHeader) throws ParseException {
		
		if (!JwtUtils.isAdministrator(authorizationHeader)) {
            return Response.status(401).entity("Unauthorized: Only admins have access to all registered users").build();
        }
		
		String contextPath = ctx.getRealPath("");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<User> users = userDAO.getAllUsers();
		
		if(users == null)
		{
			return Response.status(404).entity("There are no registered users").build();
		}
		int id = JwtUtils.getUserId(authorizationHeader);
		
		User userToDelete = new User();
		for(User user : users)
		{
			if(user.getId() == id)
			{
				userToDelete = user;
			}
			
			if(user.getRole() == Role.CUSTOMER)
			{
				user.setSuspicious(checkIsSuspicious(user));
				userDAO.setSuspicious(user, contextPath);
			}
		}		
		users.remove(userToDelete);
			
		return Response.status(200).entity(users).build();	
	}
	
	private boolean checkIsSuspicious(User user)
	{
		PurchaseDAO purchaseDAO = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		
		int canceled = 0;
		
		for(Purchase p : purchaseDAO.getAll())
		{
			if(p.getCustomer().getId() == user.getId() && p.getState() == PurchaseState.Canceled && p.getIsDeleted() == false)
			{
				canceled++;
				for(Purchase p1 : purchaseDAO.getAll())
				{
					if(p1.getCustomer().getId() == user.getId() && p1.getState() == PurchaseState.Canceled && p1.getIsDeleted() == false
							&& p1.getDateAndTime().isAfter(p.getDateAndTime()) && p1.getDateAndTime().isBefore(p.getDateAndTime().plusMonths(1)))
					{
						canceled ++;
					}
				}
				
				if(canceled >= 5)
				{
					return true;
				}
				else
				{
					canceled = 0;
				}
			}
		}
		
		return false;
	}
	
	
	@OPTIONS
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteuser() {
        return true;
    }
	
	@POST
	@Path("/deleteUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteuser(int userId, @HeaderParam("Authorization") String authorizationHeader) throws ParseException {
		String contextPath = ctx.getRealPath("");
		UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
		
		if(!JwtUtils.isAdministrator(authorizationHeader))
		{
			 return Response.status(401).entity("Unauthorized: Only admins can delete users").build();
		}
		
		if(dao.GetCustomerById(userId)!=null)
		{
			Customer c = dao.GetCustomerById(userId);
			c.setDeleted(true);
			dao.updateCustomer(c, contextPath);
			return Response.status(200).entity(c).build();
		}
		else if (dao.GetManagerById(userId) != null)
		{
			Manager m = dao.GetManagerById(userId);
			m.setDeleted(true);
			dao.updateManager(m, contextPath);
			return Response.status(200).entity(m).build();
		}
		else if (dao.GetEmployeeById(userId) != null)
		{
			Employee e = dao.GetEmployeeById(userId);
			e.setDeleted(true);
			dao.updateEmployee(e, contextPath);
			return Response.status(200).entity(e).build();
		}
		
		
		return Response.status(404).entity("User not found").build();
	}
	
}

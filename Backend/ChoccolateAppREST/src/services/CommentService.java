package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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

import com.nimbusds.jose.shaded.json.parser.ParseException;

import beans.ChocolateFactory;
import beans.Comment;
import beans.Purchase;
import dao.ChocolateDAO;
import dao.ChocolateFactoryDAO;
import dao.CommentDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import enums.CommentStatus;
import enums.PurchaseState;
import jwt.JwtUtils;

@Path("/comments")
public class CommentService {
	
	@Context
	ServletContext ctx;
	
	public CommentService(){
		
	}
	
	@PostConstruct
	public void init()
	{
		String contextPath = ctx.getRealPath("");
		
		if(ctx.getAttribute("commentDAO") == null)
		{
			ctx.setAttribute("commentDAO", new CommentDAO(contextPath));
		}
		
		if(ctx.getAttribute("purchaseDAO") == null)
		{
			ctx.setAttribute("purchaseDAO", new PurchaseDAO(contextPath));
		}	
		
		if(ctx.getAttribute("chocolateFactoryDAO") == null)
		{
			ctx.setAttribute("chocolateFactoryDAO", new ChocolateFactoryDAO(contextPath));
		}
		
		if(ctx.getAttribute("userDAO") == null)
		{
			ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}
	
	@OPTIONS
	@Path("/canComment/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsCanComment() {
		return true;
	}
	
	@GET
	@Path("/canComment/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response canComment(@PathParam("factoryId") int factoryId, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if (!JwtUtils.isCustomer(authorizationHeader)) {
			return Response.status(401).entity("Unauthorized: Only customers can make purchases").build();
	    }

		PurchaseDAO purchaseDAO = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		
		int customerId = JwtUtils.getUserId(authorizationHeader);
		
		int purchasesInFactoryNum = 0;
		for(Purchase purchase : purchaseDAO.getAll())
		{
			if(purchase.getFactoryId() == factoryId && purchase.getCustomer().getId() == customerId && purchase.getState() == PurchaseState.Accepted)
			{
				purchasesInFactoryNum++;
			}
		}
		
		int commentsInFactoryNum = 0;
		for(Comment comment : commentDAO.getAll())
		{
			if(comment.getCustomer().getId() == customerId && comment.getFactoryId() == factoryId)
			{
				commentsInFactoryNum++;
			}
		}
		
		if(commentsInFactoryNum == 0 && purchasesInFactoryNum != 0)
		{
			return Response.status(200).entity(true).build();
		}
		else
		{
			return Response.status(200).entity(false).build();
		}		
	}
	
	@OPTIONS
	@Path("/addComment")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsAddComment() {
		return true;
	}
	
	@POST
	@Path("/addComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(Comment comment, @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		String contextPath = ctx.getRealPath("");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		
		if(canComment(comment.getFactoryId(), authorizationHeader) == Response.status(200).entity(false).build())	
		{
			return Response.status(200).entity("Not able to comment on this factory").build();
		}
		
		if(comment.getRating() > 5 || comment.getRating() < 1)
		{
			return Response.status(200).entity("Rating greater than 5 or smaller than 1").build();
		}
		
		if(comment.getStatus() !=  CommentStatus.PENDING)
		{
			comment.setStatus(CommentStatus.PENDING);
		}
		
		if(comment.getText()  == "")
		{
			return Response.status(200).entity("Comment must have text").build();
		}
		
		comment.setCustomer(userDAO.GetCustomerById(JwtUtils.getUserId(authorizationHeader)));
		comment.setFactoryId(comment.getFactoryId());
		commentDAO.saveComment(comment, contextPath);

		
		return Response.status(200).entity(comment).build();
	}
	
	@OPTIONS
	@Path("/getCommentsForFactory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean corsGetCommentsForFactory() {
		return true;
	}
	
	@GET
	@Path("/getCommentsForFactory/{factoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommentsForFactory(@PathParam("factoryId") int factoryId , @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		UserDAO userDAO = (UserDAO) ctx.getAttribute("userDAO");
		ArrayList<Comment> comments = new ArrayList<Comment>();
		if(JwtUtils.isAdministrator(authorizationHeader) || (JwtUtils.isManager(authorizationHeader) && userDAO.GetManagerById(JwtUtils.getUserId(authorizationHeader)).getFactoryId() == factoryId))
		{
			for(Comment c : commentDAO.getAll())
			{
				if(c.getFactoryId() == factoryId)
				{
					comments.add(c);
				}	
			}
			return Response.status(200).entity(comments).build();
		}
		
		for(Comment c : commentDAO.getAll())
		{
			if(c.getStatus() == CommentStatus.APPROVED && c.getFactoryId() == factoryId)
			{
				comments.add(c);
			}		
		}
		
		return Response.status(200).entity(comments).build();
	}
	
	@OPTIONS
	@Path("/approveComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean corsApproveComment() {
		return true;
	}
	
	@POST
	@Path("/approveComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response approveComment(Comment comment , @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if(!JwtUtils.isManager(authorizationHeader))
		{
			return Response.status(401).entity("Unauthorized: Only customers can make purchases").build();
		}		
		
		String contextPath = ctx.getRealPath("");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		ChocolateFactoryDAO chocolateFactoryDAO = (ChocolateFactoryDAO) ctx.getAttribute("chocolateFactoryDAO");
		
		comment.setStatus(CommentStatus.APPROVED);
		
		if(commentDAO.updateComment(comment, contextPath) != null)
		{
			int factoryCommentNum = 0;
			for(Comment c : commentDAO.getAll())
			{
				if(c.getFactoryId() == comment.getFactoryId())
				{
					factoryCommentNum++;
				}
			}
			
			ChocolateFactory factory = chocolateFactoryDAO.getById(comment.getFactoryId());
			chocolateFactoryDAO.recalculateRating(factory, factoryCommentNum, comment.getRating(), contextPath);
			
			return Response.status(200).entity(comment).build(); 
		}	
		
		return Response.status(404).entity("Comment not found").build();
	}
	
	@OPTIONS
	@Path("/declineComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean corsDeclineComment() {
		return true;
	}
	
	@POST
	@Path("/declineComment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response declineComment(Comment comment , @HeaderParam("Authorization") String authorizationHeader) throws ParseException
	{
		if(!JwtUtils.isManager(authorizationHeader))
		{
			return Response.status(401).entity("Unauthorized: Only customers can make purchases").build();
		}		
		
		String contextPath = ctx.getRealPath("");
		CommentDAO commentDAO = (CommentDAO) ctx.getAttribute("commentDAO");
		
		comment.setStatus(CommentStatus.DECLINED);
		
		if(commentDAO.updateComment(comment, contextPath) != null)
		{
			return Response.status(200).entity(comment).build(); 
		}	
		
		return Response.status(404).entity("Comment not found").build();
	}
}

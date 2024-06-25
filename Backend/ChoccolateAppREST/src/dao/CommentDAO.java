package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;

import beans.Chocolate;
import beans.Comment;
import beans.Purchase;

public class CommentDAO {
private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public CommentDAO() {		
	}
	
	public CommentDAO(String contextPath) {
		loadComments(contextPath);
	}
	
	public ArrayList<Comment> getAll() {
		return comments;
	}
	
	public void loadComments(String contextPath) {
		try {
            Path filePath = Paths.get(contextPath, "comments.json");

            String jsonData = Files.readString(filePath);

            Gson gson = new Gson();
            Comment[] commentsArray = gson.fromJson(jsonData, Comment[].class);
            if (commentsArray != null) {
                for (Comment c : commentsArray) {
                    comments.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Comment saveComment(Comment newComment, String contextPath)
	{		
		try {
			Gson gson = new Gson();  
			Path filePath;
			String updatedJsonData;

			int maxId = -1;
			for(Comment c : comments)
			{
				if(c.getId() > maxId)
				{
					maxId = c.getId();
				}
			}
				
			if(maxId == -1)
			{
				maxId = 0;
			}
				
			newComment.setId(maxId + 1);
			comments.add(newComment);
			filePath = Paths.get(contextPath, "/comments.json");
			Comment[] updateCommentArray = comments.toArray(new Comment[0]);
			updatedJsonData = gson.toJson(updateCommentArray);
			
            Files.write(filePath, updatedJsonData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return newComment;	
	}
	
	public Comment updateComment(Comment updatedComment, String contextPath)
	{
		int i = -1;
		for(Comment comment : comments)
		{
			i++;
		    if(comment.getId() == updatedComment.getId())
		    {
		    	comment = updatedComment;	
		    	comments.remove(i);
				comments.add(i, comment);
						
				Gson gson = new Gson();  
				String updatedJsonData;
				
				Path filePath = Paths.get(contextPath, "/comments.json");
				Comment[] updateCommentArray = comments.toArray(new Comment[0]);
				updatedJsonData = gson.toJson(updateCommentArray);
				
	            try {
					Files.write(filePath, updatedJsonData.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            return updatedComment;
		    }

		}
		
		return null;		
	}
	
}

package services;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.io.File;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/chocholate")
public class FileUploadService {
	@Context
	ServletContext ctx;
	
	public FileUploadService() {
	}
	
	@Path("/image")
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile (@FormDataParam("file") InputStream inputStream)
	{
        try {
            String path = splitInputStream(inputStream);
            return Response.status(Response.Status.OK).entity(path).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

	private String splitInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream firstPartBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream secondPartBuffer = new ByteArrayOutputStream();
        
        int counter = 0;
        int previousChar1 = -1;
        int previousChar2 = -1;
        int previousChar3 = -1;
        int currentChar;
        boolean delimiterFound = false;
 

        while ((currentChar = inputStream.read()) != -1) {
            if (!delimiterFound) {
                firstPartBuffer.write(currentChar);

                if(previousChar3 == '\r' && previousChar2 == '\n' && previousChar1 == '\r' && currentChar == '\n')
                	delimiterFound = true;
                
            } else {
                secondPartBuffer.write(currentChar);
            }
            counter++;
            
            if(counter == 1)
            	previousChar3 = currentChar;
            	
            if(counter == 2)
            	previousChar2 = currentChar;
            
            if(counter == 3)
            	previousChar3 = currentChar;
            
            if(counter > 3)
            {
            	previousChar3 = previousChar2;
            	previousChar2 = previousChar1;
            	previousChar1 = currentChar;
            }
        }
        
        byte[] firstPartBytes = firstPartBuffer.toByteArray();
        firstPartBytes = new String(firstPartBytes, 0, firstPartBytes.length - 2).getBytes();
        
        String firstPart = new String(firstPartBytes); 
        String path = "http://localhost:8080/ChoccolateAppREST/images/" + getFileName(firstPart);                   
        InputStream secondPart =  new ByteArrayInputStream(secondPartBuffer.toByteArray());
        saveToFile(secondPart, path);
        
        return path;
	}
	
	private String getFileName(String firstPart)
	{
		for(String str : firstPart.split(" "))
		{
			if(str.startsWith("filename="))
			{
				return str.split("=")[1].replaceAll("\"", "").split("\r")[0];
			}
		}
		
		return null;
	}
	
	
    private void saveToFile(InputStream uploadedInputStream, String path) throws IOException {
        try {
        	OutputStream outputStream = new FileOutputStream(new File(path));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        };
    }   
}


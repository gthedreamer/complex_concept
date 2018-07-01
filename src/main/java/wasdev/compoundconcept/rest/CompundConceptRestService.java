/*******************************************************************************
 * Copyright (c) 2017 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/ 
package wasdev.compoundconcept.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.sun.jersey.multipart.FormDataParam;

import wasdev.compoundconcept.ibmclient.IBMQueryClient;
import wasdev.compoundconcept.ibmclient.IBMUploadClient;
import wasdev.compoundconcept.parser.InputParser;

@ApplicationPath("api")
@Path("/discovery")
public class CompundConceptRestService extends Application {
  /**
   * Query collection
   * REST API example:
   * <code>
   * GET http://localhost:9080/api/discovery/query
   * </code>
   */
    @GET
    @Path("/query")
    @Produces({"application/json"})
    public String getVisitors(@QueryParam("count") long resultCount,
    		@QueryParam("queryString") String queryString
    		) {
		QueryResponse queryResponse = null;
		
    	try {
    		System.out.println("count : "+resultCount+" queryString : "+queryString);
    		
        	IBMQueryClient queryClient = new IBMQueryClient();
        	queryResponse = queryClient.query(resultCount,queryString);
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return new Gson().toJson(queryResponse);
    }
    
    /**
     * Upload a txt file to IBM cloud
     * <code>
     * POST	 http://localhost:9080/api/discovery/query/upload
     * </code>
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream uploadedInputStream) {

		try {
	    	String theString = IOUtils.toString(uploadedInputStream, Charset.forName("UTF-8")); 
//		    System.out.println("uploaded file content"+theString);
		    File file = new File("input.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
		    PrintWriter out = new PrintWriter("input.txt");
		    out.write(theString);
		    out.close();
		    InputParser parser = new InputParser();
		    int count = parser.parseToJson(file);
		    System.out.println("Parsed docs : "+count);
		    IBMUploadClient uploader = new IBMUploadClient();
		    for(int fileIndex = 1;fileIndex <= count; fileIndex++ ) {
		    	uploader.upload(fileIndex+".json");
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	    return Response.status(200).build();
    }

}
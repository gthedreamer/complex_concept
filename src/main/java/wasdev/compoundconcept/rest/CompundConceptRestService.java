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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.sun.jersey.multipart.FormDataParam;

import wasdev.compoundconcept.filemanager.DayOneDocIdFileManager;
import wasdev.compoundconcept.ibmclient.IBMDeleteDocumentClient;
import wasdev.compoundconcept.ibmclient.IBMQueryClient;
import wasdev.compoundconcept.ibmclient.IBMUploadClient;
import wasdev.compoundconcept.parser.InputParser;

@ApplicationPath("api")
@Path("/discovery")
public class CompundConceptRestService extends Application {
	
	private static final String UPLOADED_DAY_1_FILE = "day_one.txt";
	
  /**
   * Query collection
   * <code>
   * GET http://localhost:9080/api/discovery/query
   * </code>
   */
    @GET
    @Path("/query")
    @Produces({"application/json"})
    public String handleQuery(@QueryParam("count") long resultCount,
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
    		int status = 500;
    		
		try {
	    	String theString = IOUtils.toString(uploadedInputStream, Charset.forName("UTF-8")); 
//		    System.out.println("uploaded file content"+theString);
		    File file = new File(UPLOADED_DAY_1_FILE);
			if (!file.exists()) {
				file.createNewFile();
			}
		    PrintWriter day1InputDoc = new PrintWriter(UPLOADED_DAY_1_FILE);
		    day1InputDoc.write(theString);
		    day1InputDoc.close();
		    InputParser parser = new InputParser();
		    int count = parser.parseToJson(file);
		    System.out.println("Parsed docs : "+count);
		    IBMUploadClient uploader = new IBMUploadClient();
		    DocumentAccepted docAddResponse = null;
		    
		    List<String> docIdsUploaded = new ArrayList<String>();
		    for(int fileIndex = 1;fileIndex <= count; fileIndex++ ) {
		    	// HIT IBM api and upload 
		    	docAddResponse = uploader.upload(fileIndex+".json");
		    	docIdsUploaded.add(docAddResponse.getDocumentId());
		    	System.out.println("document uploaded id : "+docAddResponse.getDocumentId());
		    }
		    
		    // Writes all uploaded document ids to file; we will use it later to delete day 1 set alone
		    DayOneDocIdFileManager dayOneIdsFileManager = new DayOneDocIdFileManager();
		    dayOneIdsFileManager.appendDocumentIdsToFile(docIdsUploaded);
		    status = 200;
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

	    return Response.status(status).build();
    }

    /**
     * Delete Day 1 Set
     * <code>
     * DELETE http://localhost:9080/api/discovery/delete/dayone
     * </code>
     */
      @DELETE
      @Path("/delete/dayone")
      @Produces({"application/json"})
      public String deleteDayOneSet() {
      	try {
      		System.out.println("DELETE endpoint for deleting day 1 set");
      		DayOneDocIdFileManager dayOneIdsFileManager = new DayOneDocIdFileManager();
      		IBMDeleteDocumentClient ibmDeleteDocClient = new IBMDeleteDocumentClient();
      		
      		List<String> day1DocIds = dayOneIdsFileManager.readDocumentIds();
      		
      		for(String docId : day1DocIds) {
      			ibmDeleteDocClient.deleteDocument(docId);
      			System.out.println("Deleted document "+docId+"from IBM collection");
      		}
      		
      		//Reset the docId file
      		dayOneIdsFileManager.clearDocIds();
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			return new Gson().toJson("{status : failed }");
  		}
      	
  		return new Gson().toJson("{status : success }");
      }
}
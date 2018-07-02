package wasdev.compoundconcept.ibmclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.http.HttpMediaType;

public class IBMUploadClient {
	public boolean upload(String filename) throws Exception{
		boolean status = false;
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("1b26e43d-413f-4ad0-bd9f-0d5273d63b28", "vqVJFUpLES43");
//		discovery.setEndPoint(	"https://gateway.watsonplatform.net/discovery/api");
		String environmentId = "a5b2deb8-e89f-45a9-86ad-ac1856432547";
		String collectionId = "498f55d5-522a-4da2-99b6-524a0e15015f";
		AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder(environmentId, collectionId);
		InputStream documentStream = new FileInputStream(new File(filename));
		builder.file(documentStream);
		builder.fileContentType(HttpMediaType.APPLICATION_JSON);
		builder.filename(filename);
		discovery.addDocument(builder.build()).execute();
		System.out.println("IBM upload success for "+filename);
		
		return status;
	}

}

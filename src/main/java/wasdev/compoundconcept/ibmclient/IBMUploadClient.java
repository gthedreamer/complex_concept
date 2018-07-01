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
		discovery.setUsernameAndPassword("dca38471-c13c-44ef-85ff-88eb5a5b080d", "i2Levws58JXa");
		String environmentId = "8b66a4b2-0274-4fcf-a407-32b3257b127f";
		String collectionId = "aa576ab7-ee39-4c0d-a3cb-18eddc963038";
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

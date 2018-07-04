package wasdev.compoundconcept.ibmclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.ServiceCall;

public class IBMUploadClient {
	public DocumentAccepted upload(String filename) throws Exception{
		DocumentAccepted addDocResponse = null;
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("9c332ef7-ba8a-419d-9e5c-6f7363a0bb37", "vEXTxCO2dHS8");
		String environmentId = "dde1b791-3e20-4b60-9f7f-9ced24a4bc31";
		String collectionId = "436ba503-4708-474d-ae39-3b54af21ab0d";
		
		AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder(environmentId, collectionId);
		InputStream documentStream = new FileInputStream(new File(filename));
		builder.file(documentStream);
		builder.fileContentType(HttpMediaType.APPLICATION_JSON);
		builder.filename(filename);
		addDocResponse = discovery.addDocument(builder.build()).execute();
		System.out.println("IBM upload success for "+filename + addDocResponse);
		
		return addDocResponse;
	}

}

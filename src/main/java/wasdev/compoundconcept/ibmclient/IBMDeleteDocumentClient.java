package wasdev.compoundconcept.ibmclient;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.DeleteDocumentOptions;

public class IBMDeleteDocumentClient {
	public boolean deleteDocument(String documentId) {
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("1b26e43d-413f-4ad0-bd9f-0d5273d63b28", "vqVJFUpLES43");
		String environmentId = "a5b2deb8-e89f-45a9-86ad-ac1856432547";
		String collectionId = "498f55d5-522a-4da2-99b6-524a0e15015f";
		
		DeleteDocumentOptions deleteRequest = new DeleteDocumentOptions.Builder(environmentId, collectionId, documentId).build();
		discovery.deleteDocument(deleteRequest).execute();
		
		return true;
	}
}

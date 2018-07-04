package wasdev.compoundconcept.ibmclient;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.DeleteDocumentOptions;

public class IBMDeleteDocumentClient {
	public boolean deleteDocument(String documentId) {
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("9c332ef7-ba8a-419d-9e5c-6f7363a0bb37", "vEXTxCO2dHS8");
		String environmentId = "dde1b791-3e20-4b60-9f7f-9ced24a4bc31";
		String collectionId = "436ba503-4708-474d-ae39-3b54af21ab0d";
		
		DeleteDocumentOptions deleteRequest = new DeleteDocumentOptions.Builder(environmentId, collectionId, documentId).build();
		discovery.deleteDocument(deleteRequest).execute();
		
		return true;
	}
}

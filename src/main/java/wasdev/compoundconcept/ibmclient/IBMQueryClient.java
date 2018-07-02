package wasdev.compoundconcept.ibmclient;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;

public class IBMQueryClient {
	public QueryResponse query(long resultLimit,String queryString) throws Exception {
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("1b26e43d-413f-4ad0-bd9f-0d5273d63b28", "vqVJFUpLES43");
//		discovery.setEndPoint(	"https://gateway.watsonplatform.net/discovery/api");
		String environmentId = "a5b2deb8-e89f-45a9-86ad-ac1856432547";
		String collectionId = "498f55d5-522a-4da2-99b6-524a0e15015f";

		QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
		queryBuilder.query("CONTENT:"+queryString);
		queryBuilder.highlight(true);
		queryBuilder.count(resultLimit);
		
		QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
		System.out.println("queryStr = "+queryString+ "  Response = "+queryResponse);
		return queryResponse;
	}

}

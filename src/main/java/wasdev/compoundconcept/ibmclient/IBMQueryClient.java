package wasdev.compoundconcept.ibmclient;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;

public class IBMQueryClient {
	public QueryResponse query(long resultLimit,String queryString) throws Exception {
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("dca38471-c13c-44ef-85ff-88eb5a5b080d", "i2Levws58JXa");
		String environmentId = "8b66a4b2-0274-4fcf-a407-32b3257b127f";
		String collectionId = "aa576ab7-ee39-4c0d-a3cb-18eddc963038";

		QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
		queryBuilder.query("CONTENT:"+queryString);
		queryBuilder.highlight(true);
		queryBuilder.count(resultLimit);
		
		QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
		System.out.println(queryResponse);
		return queryResponse;
	}

}

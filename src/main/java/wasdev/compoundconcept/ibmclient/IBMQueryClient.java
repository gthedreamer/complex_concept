package wasdev.compoundconcept.ibmclient;

import java.net.URLEncoder;

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
		queryBuilder.query(mapToIBMQuery(queryString));
		queryBuilder.highlight(true);
		queryBuilder.count(resultLimit);
		
		QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute();
		System.out.println("queryStr = "+queryString+ "  Response = "+queryResponse);
		return queryResponse;
	}
	
	public String mapToIBMQuery(String rawQuery) {
		System.out.print(rawQuery);
		String ibmQueryFormat = null;
		
		ibmQueryFormat = rawQuery.replaceAll("AND", ",")
							.replaceAll("OR", "|")
							.replaceAll("NOT", "")
							.replaceAll("(?i)Concepts", "enriched_CONTENT.concepts.text")
							.replaceAll("(?i)Concept", "enriched_CONTENT.concepts.text")
							.replaceAll("(?i)Locations", "enriched_CONTENT.entities.relevance>0.8, enriched_CONTENT.entities.type::\"Location\", enriched_CONTENT.entities.text")
							.replaceAll("(?i)title", "TITLE")
							.replaceAll("(?i)content", "CONTENT")
							.replaceAll("(?i)Organizations", "enriched_CONTENT.entities.relevance>0.8,enriched_CONTENT.entities.type::\"Organization\",enriched_CONTENT.entities.text");
		
		System.out.println("IBM query : "+ibmQueryFormat);
		return ibmQueryFormat;
	}
}

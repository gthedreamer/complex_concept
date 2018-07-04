package wasdev.compoundconcept.ibmclient;

import java.net.URLEncoder;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;

public class IBMQueryClient {
	public QueryResponse query(long resultLimit,String queryString) throws Exception {
		Discovery discovery = new Discovery("2018-03-05");
		discovery.setUsernameAndPassword("9c332ef7-ba8a-419d-9e5c-6f7363a0bb37", "vEXTxCO2dHS8");
		String environmentId = "dde1b791-3e20-4b60-9f7f-9ced24a4bc31";
		String collectionId = "436ba503-4708-474d-ae39-3b54af21ab0d";

		QueryOptions.Builder queryBuilder = null;
		QueryResponse queryResponse= null;
		
		try {
			queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
			queryBuilder.query(mapToIBMQueryV2(queryString));
			queryBuilder.highlight(true);
			queryBuilder.count(resultLimit);
			
			queryResponse = discovery.query(queryBuilder.build()).execute();
		}catch(BadRequestException e) {
			System.out.println("V2 Exception..FallBack V1");
			queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
			queryBuilder.query(mapToIBMQueryV1(queryString));
			queryBuilder.highlight(true);
			queryBuilder.count(resultLimit);
			
			queryResponse = discovery.query(queryBuilder.build()).execute();
		}
		
		System.out.println("queryStr = "+queryString+ "  Response = "+queryResponse);
		return queryResponse;
	}
	
	public String mapToIBMQueryV1(String rawQuery) {
		System.out.print(rawQuery);
		String ibmQueryFormat = null;
		
		if(rawQuery.indexOf("Count") >= 0 ) {
			ibmQueryFormat = handleCountOperator(rawQuery);
		}
		else {
		ibmQueryFormat = rawQuery.replaceAll("AND", ",")
							.replaceAll("OR", "|")
							.replaceAll("NOT", "")
							.replaceAll("(?i)Concepts", "Concept")
							.replaceAll("(?i)Concept", "enriched_CONTENT.concepts.text")
							.replaceAll("(?i)Locations", "enriched_CONTENT.entities.relevance>0.8, enriched_CONTENT.entities.type::\"Location\", enriched_CONTENT.entities.text")
							.replaceAll("(?i)title", "TITLE")
							.replaceAll("(?i)content", "CONTENT")
							.replaceAll("(?i)Organizations", "enriched_CONTENT.entities.relevance>0.8,enriched_CONTENT.entities.type::\"Organization\",enriched_CONTENT.entities.text");
		}
		
		System.out.println("IBM query : "+ibmQueryFormat);
		return ibmQueryFormat;
	}
	
	public String mapToIBMQueryV2(String rawQuery) {
		System.out.print(rawQuery);
		String ibmQueryFormat = null;
		
		if(rawQuery.indexOf("Count") >= 0 ) {
			ibmQueryFormat = handleCountOperator(rawQuery);
		}
		else {
			ibmQueryFormat = rawQuery.replaceAll("AND", ",")
							.replaceAll("OR", "|")
							.replaceAll("\\*","")
							.replaceAll("(?i)NOT title :", "TITLE :!")
							.replaceAll("(?i)NOT content : ", "CONTENT :!")
							.replaceAll("(?i)title :", "TITLE :")
							.replaceAll("(?i)content : ", "CONTENT :")
							.replaceAll("(?i)Concepts", "Concept")
							.replaceAll("(?i)NOT concept :", "enriched_CONTENT.concepts.text : !")
							.replaceAll("(?i)Concept :", "enriched_CONTENT.concepts.text :")
							.replaceAll("(?i)NOT Locations :", "enriched_CONTENT.entities.relevance>0.8, enriched_CONTENT.entities.type::\"Location\", enriched_CONTENT.entities.text :!")
							.replaceAll("(?i)Locations :", "enriched_CONTENT.entities.relevance>0.8, enriched_CONTENT.entities.type::\"Location\", enriched_CONTENT.entities.text :")
							.replaceAll("(?i)NOT Organizations :", "enriched_CONTENT.entities.relevance>0.8,enriched_CONTENT.entities.type::\"Organization\",enriched_CONTENT.entities.text:!")	
							.replaceAll("(?i)Organizations :", "enriched_CONTENT.entities.relevance>0.8,enriched_CONTENT.entities.type::\"Organization\",enriched_CONTENT.entities.text:");
		}
		
		System.out.println("IBM query : "+ibmQueryFormat);
		return ibmQueryFormat;
	}
	
	private String handleCountOperator(String rawQuery) {
		System.out.println("handleCountOperator");
		String ibmQuery = null;
		String operandVal = null;
		String op = null;
		boolean valueFoundInsideCount = false;
		
		valueFoundInsideCount = ( rawQuery.indexOf(':') >= 0) ? true : false;
		
		String tempQuery = rawQuery.replaceAll("(?i)Count", "");
		System.out.println(" count removed "+ tempQuery);
		op = findOperator(tempQuery);
		System.out.println("operator "+op);
		String[] values = tempQuery.split(op,2);
		System.out.println(values);
		if(values.length == 2) {
			operandVal = values[1].replaceAll(" ", "");
		}
		System.out.println(" operand " + operandVal);
		ibmQuery  = tempQuery.replaceAll(op, "")
							 .replaceAll(operandVal, "")
							 .replaceAll("(?i)Locations", "enriched_CONTENT.entities.type::\"Location\",enriched_CONTENT.entities.count "
											+ op + operandVal + 
											(valueFoundInsideCount ? ",enriched_CONTENT.entities.text" : ""))
							 .replaceAll("(?i)Organizations","enriched_CONTENT.entities.type::\"Organization\",enriched_CONTENT.entities.count "
								+ op + operandVal+(valueFoundInsideCount ? ",enriched_CONTENT.entities.text" : ""))
							 .trim();
		
		return ibmQuery;
	}
	
	private String findOperator(String query) {
		String operator = null;
		
		if(query.indexOf(">=") >= 0) {
			operator = ">=";
		}else if(query.indexOf("<=") >= 0) {
			operator = "<=";
		}else if(query.indexOf("=") >= 0) {
			operator = "=";
		}else if(query.indexOf(">") >= 0) {
			operator = ">";
		}else if(query.indexOf("<") >= 0) {
			operator ="<";
		}
		
		return operator;
	}
}

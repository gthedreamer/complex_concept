package wasdev.compoundconcept.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject; 

public class InputParser {
	public int parseToJson(File inputTextFile) throws IOException {
		int count = 0;
		
		try {
			String str = FileUtils.readFileToString(inputTextFile, "UTF-8");
			System.out.println("Value of the file read:::" + str);
			System.out.println("\n\n\n");
	
			String[] arrSplit = str.split("<DOCID>=");
			int length = arrSplit.length - 1;
			System.out.println("Total number of docs received in txt " + length);
	
			for (int i = 1; i < arrSplit.length; i++) {
				
				File file=new File(String.valueOf(i)+".json");
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file);
				
	
				String tagsRemovedStr = arrSplit[i].replaceAll("<TOPICS>=", "\",\"TOPICS\":\"")
						.replaceAll("<TITLE>=", "\",\"TITLE\":\"").replaceAll("<BODY>=", "\",\"BODY\":\"")
						.replaceAll("\r", "").replaceAll("\n", "");
	
				String topicOrgStr = "{\"DOCID\":\"" + tagsRemovedStr + "\"}";
	
				JSONObject jsonObj = new JSONObject();
				String topics = null;
				String body;
				if(topicOrgStr.indexOf("TOPICS\":\"") >= 0) {
					topics = topicOrgStr.substring(topicOrgStr.indexOf("TOPICS"), topicOrgStr.indexOf("\"}"))
							.replaceAll("TOPICS\":\"", "").replaceAll("&lt;", "<");
					body = topicOrgStr.substring(topicOrgStr.indexOf("BODY"), topicOrgStr.indexOf("\",\"TOPICS\""))
							.replaceAll("BODY\":\"", "").replaceAll("&lt;", "<").replaceAll("&#3;", "");
				}
				else {
					body = topicOrgStr.substring(topicOrgStr.indexOf("BODY"), topicOrgStr.indexOf("\"}"))
							.replaceAll("BODY\":\"", "").replaceAll("&lt;", "<").replaceAll("&#3;", "");
				}
				
				String docid = topicOrgStr.substring(topicOrgStr.indexOf("DOCID"), topicOrgStr.indexOf("\",\"TITLE\""))
						.replaceAll("DOCID\":\"", "").replaceAll("&lt;", "<");
				String title = topicOrgStr.substring(topicOrgStr.indexOf("TITLE"), topicOrgStr.indexOf("\",\"BODY\""))
						.replaceAll("TITLE\":\"", "").replaceAll("&lt;", "<");
				
				jsonObj.put("TOPICS", topics);
				jsonObj.put("DOCID", docid);
				jsonObj.put("TITLE",title);
				jsonObj.put("BODY", body);
				jsonObj.put("CONTENT", title + " " + body);
	
				System.out.println("json to string:::" + jsonObj.toString());
				
				fileWriter.write(jsonObj.toJSONString());
				fileWriter.flush();
				fileWriter.close();
	
				count ++;
			}
			System.out.println("Docs parsed " + count);
			System.out.println("completed successfully");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
	
//	public static void main( String[] args ) {
//		InputParser parser = new InputParser();
//		try {
//			parser.parseToJson(new File("/Users/gpandian/Personal/TC/IBM_CompoundInterest/docs/docs_mini.txt"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}

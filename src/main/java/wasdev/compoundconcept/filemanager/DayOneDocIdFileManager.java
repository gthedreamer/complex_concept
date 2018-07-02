package wasdev.compoundconcept.filemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;

public class DayOneDocIdFileManager {
	
	private static final String DAY_1_DOC_ID_LIST_FILE = "day_one_doc_ids.txt";
	
	public List<String> readDocumentIds() {
		List<String> docIds = new ArrayList<String>();
		
		try {
			File file = new File(DAY_1_DOC_ID_LIST_FILE);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				docIds.add(line);
				stringBuffer.append(line+"\n");
			}
			fileReader.close();
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return docIds;
	}
	
	public boolean writeDocumentIds(List<String> docIds){
		boolean status = false;
		
		try {
		    // Append contents to the docId file so that we can use it later to delete day 1 set alone
		    PrintWriter day1DocIdsFile = new PrintWriter(new FileOutputStream(DAY_1_DOC_ID_LIST_FILE),true);
		    for(String docId : docIds ) {
		    	day1DocIdsFile.append(docId+"\n");
		    	System.out.println("document id : "+docId);
		    }
		    day1DocIdsFile.close();
		    status = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	public void clearDocIds() {
		try {
			PrintWriter fileWriter = new PrintWriter(DAY_1_DOC_ID_LIST_FILE);
			fileWriter.write("");
			fileWriter.close();
			System.out.println("Cleared DOC ids");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

package wasdev.compoundconcept.filemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DayOneDocIdFileManager {
	
	private static final String DAY_1_DOC_ID_LIST_FILE = "day_one_doc_ids.txt";
	
	public List<String> readDocumentIds() {
		List<String> docIds = new ArrayList<String>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try {
			File file = new File(DAY_1_DOC_ID_LIST_FILE);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				docIds.add(line);
				stringBuffer.append(line+"\n");
			}

			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return docIds;
	}
	
	public boolean appendDocumentIdsToFile(List<String> docIds){
		boolean status = false;

	    FileWriter docIdFileWriter = null;
	    BufferedWriter day1DocIdBuffWriter = null;
	    
		try {
		    // Append contents to the docId file so that we can use it later to delete day 1 set alone
		    docIdFileWriter = new FileWriter(DAY_1_DOC_ID_LIST_FILE,true);
		    day1DocIdBuffWriter = new BufferedWriter(docIdFileWriter);
		    
		    for(String docId : docIds ) {
		    	day1DocIdBuffWriter.write(docId+"\n");
		    	System.out.println("Writing document id to file : "+docId);
		    }
		    status = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
		    try {
				day1DocIdBuffWriter.close();
			    docIdFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

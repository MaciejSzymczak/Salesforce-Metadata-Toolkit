import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class SalesforceDependencies {
	
	static boolean ignoreCase = true;
	static boolean ignoreTestClass = true;	
	
	static Map itemList = new TreeMap();

    public static void 	writeToCsv(String folderName) throws IOException {
		  FileWriter fw = new FileWriter(folderName+"\\result.csv");
		  
		  System.out.println("Creating file: result.csv");
		  
		  fw.write( "Value,Primary Dependencies, Secondary Dependencies\n" );
		  for(Object item: itemList.keySet()) {
			  String primaryDependencies ="";
			  String secondaryDependencies ="";
			  List<String> dependencies = (List<String>) itemList.get(item.toString());
			  for(String dependency : dependencies)  {
				  //split() accepts a regular expression, so you need to escape . to not consider it as a regex meta character
				  String[] splittedDependency = dependency.split("\\.");
				  System.out.println(item.toString()+ " dependency:" + dependency);
				  String formatedDependency = splittedDependency[1] +": "+ splittedDependency[0]; 
				  if (dependency.toLowerCase().indexOf(".profile") !=-1 ||
					  dependency.toLowerCase().indexOf(".app") !=-1 ||
					  dependency.toLowerCase().indexOf(".permissionset") !=-1) {
					secondaryDependencies = secondaryDependencies + formatedDependency+"\n";					  
				  }
				  else {
					primaryDependencies = primaryDependencies + formatedDependency+"\n";					  
				  }
			  }
			  fw.write( item.toString() + ",\"" + primaryDependencies+"\",\"" + secondaryDependencies+"\"\n" );		  
		  }
		  
		  fw.close();
		  System.out.println("File created");

    }	
	

    public static void 	writeToCsvFlat(String folderName) throws IOException {
		  FileWriter fw = new FileWriter(folderName+"\\resultFlat.csv");
		  
		  System.out.println("Creating file: result.csv");
		  
		  fw.write( "Value,Object Type,Name\n" );
		  for(Object item: itemList.keySet()) {
			  List<String> dependencies = (List<String>) itemList.get(item.toString());
			  for(String dependency : dependencies)  {
				  //split() accepts a regular expression, so you need to escape . to not consider it as a regex meta character
				  String[] splittedDependency = dependency.split("\\.");
				  System.out.println(item.toString() + " dependency:" + dependency);
				  fw.write( item.toString() + ",\"" + splittedDependency[1]+"\",\"" + splittedDependency[0]+"\"\n" );		  
			  }
		  }
		  
		  fw.close();
		  System.out.println("File created");

    }	
    
    
    public static boolean containsText (String searchText, String path, String fileName, boolean ignoreCase) throws IOException, ParserConfigurationException {
		if ((searchText+"").length() ==0) return false;
    	FileReader fr;	
		fr = new FileReader(path+fileName);
		BufferedReader br = new BufferedReader(fr);
		String phisicalLine;
		
		while( (phisicalLine = br.readLine()) != null) {
			if (ignoreCase) 
				phisicalLine = (phisicalLine+"").toUpperCase();
			int positionFound = (phisicalLine+"").indexOf(searchText);
			
			//"City__c" should not be reported as found in a string "ShippingCity__c"
			//if string was found we check is the letter before the string is letter or digit
			//if this example it is the letter "g" so the function returns false.
			if (positionFound > 0) 
			    if (Character.isLetterOrDigit((phisicalLine+"").charAt(positionFound-1)) 
			    		  || ((phisicalLine+"").charAt(positionFound-1)=='.') 
			    		  || ((phisicalLine+"").charAt(positionFound-1)=='_'))   
			    	return false;
			
			if ( positionFound !=-1)
				return true;
		}	
		fr.close();		
		return false;
	}
		
	public static void readFilesFromFolder(String searchText1, String searchText2,final File folder) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
    	System.out.println("Processing:" + searchText1);
	    for (final File fileEntry : folder.listFiles()) {
			if (ignoreCase) {
				searchText1 = searchText1.toUpperCase();
				searchText2 = searchText2.toUpperCase();				
			}
	        if (fileEntry.isDirectory()) {
	        	readFilesFromFolder(searchText1, searchText2, fileEntry);
	        } else {
	            	Boolean ignoreThisFile = false;
	            	if (ignoreTestClass){
	            		if ((fileEntry.getName().endsWith(".cls") )	&& containsText("@ISTEST", folder.getPath()+"\\", fileEntry.getName(), true))
	            			ignoreThisFile = true;
	            	}
	            		
	            	if (!ignoreThisFile && 
	            			(containsText(searchText1, folder.getPath()+"\\", fileEntry.getName(), ignoreCase)
	            			||
	            			containsText(searchText2, folder.getPath()+"\\", fileEntry.getName(), ignoreCase)
	            			)
	            		) {
	            		
	            		if ( !itemList.containsKey(searchText1) ) {
		            		List<String> items = new ArrayList<String>();
		            		itemList.put(searchText1, items);	            			
	            		}
	            		
	            		List<String> items = (List<String>) itemList.get(searchText1);
	            		items.add(fileEntry.getName());
	            		
	            	}
	        }
	    }
	}		
	
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
		ignoreCase = true;
		ignoreTestClass = true;
		
		String folderName = args[0];

		File d = new File (folderName+"\\result.csv");
		d.delete();
		File d2 = new File (folderName+"\\resultFlat.csv");
		d2.delete();

		//Profiles
		readFilesFromFolder("Jenkins User","", new File(folderName));
		readFilesFromFolder("Warehouse Administrator - UA","", new File(folderName));
		readFilesFromFolder("Call Center Agent - US","", new File(folderName));
		readFilesFromFolder("JnJ Base- Console","", new File(folderName));
		readFilesFromFolder("JnJ Base- Standard","", new File(folderName));
		readFilesFromFolder("LifeScan Local Admin - US","", new File(folderName));
		readFilesFromFolder("Marketing Manager - US","", new File(folderName));
		readFilesFromFolder("Sales Rep - VALIDATE","", new File(folderName));
		readFilesFromFolder("Partner Community User","", new File(folderName));
		readFilesFromFolder("LFS Finance User","", new File(folderName));
		readFilesFromFolder("LFS Mkts (Channel) User","", new File(folderName));
		readFilesFromFolder("LFS Mkts (ID) User","", new File(folderName));
		readFilesFromFolder("LFS Mkts (Payer) User","", new File(folderName));
		readFilesFromFolder("LifeScan Compliance Team US","", new File(folderName));
		readFilesFromFolder("LifeScan Contract Local Admin US","", new File(folderName));

		//clauses
		readFilesFromFolder("@future", "", new File(folderName));
		readFilesFromFolder("@istest", "", new File(folderName));
		readFilesFromFolder("@RestResource", "", new File(folderName));
		readFilesFromFolder("webService ", "", new File(folderName));
		readFilesFromFolder("Batchable", "", new File(folderName));
		readFilesFromFolder("Schedulable", "", new File(folderName));
		readFilesFromFolder("Messaging.", "", new File(folderName));		
		
		//custom objects
		readFilesFromFolder("ANI_State_Codes__c", "", new File(folderName));
		readFilesFromFolder("APMarkerSetting__c", "", new File(folderName));
		readFilesFromFolder("Access_Contracting__c", "", new File(folderName));
		readFilesFromFolder("AccountContractValidationMatrix__c", "", new File(folderName));
		readFilesFromFolder("Account_Address__c", "", new File(folderName));
		readFilesFromFolder("Account_Attributes__c", "", new File(folderName));
		readFilesFromFolder("Account_Growth_Initiatives_Meas__c", "", new File(folderName));
		readFilesFromFolder("Account_Product__c", "", new File(folderName));
		readFilesFromFolder("Account_Sharing_Groups__c", "", new File(folderName));
		readFilesFromFolder("Administrators__c", "", new File(folderName));
		readFilesFromFolder("AllowableMarket__c", "", new File(folderName));
		readFilesFromFolder("AnimasOrder__c", "", new File(folderName));
		readFilesFromFolder("AnimasUserEmails__c", "", new File(folderName));
		readFilesFromFolder("Animas_Settings__c", "", new File(folderName));
		readFilesFromFolder("Approval_Process_Settings__c", "", new File(folderName));
		readFilesFromFolder("Assessment__c", "", new File(folderName));

		//generate result.csv and resultFlat.csv
		writeToCsv(folderName);
		writeToCsvFlat(folderName);
	}

}

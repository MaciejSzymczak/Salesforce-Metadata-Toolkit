import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This java program  produces a matrix that shows what fields are used on what page layouts (the matrix: fields x page layouts).
 * It reads the content of page layouts and the definition of the object (you can download the .layout and .object files using migration tool) and conglomerates data in a form of a readable matrix. 
 * It Reads given folder on the disk (entry parameter of java program) and saves the result in the file result.csv.
 * It analyzes page layouts, not visual force pages (so far).
 * It can be used for identification what region uses what fields and for identification what fields are not used at all.
 * Column C says the number of page layouts on which field is used. 0 mean the field is suspicious and should be prosecuted  rather than instant death sentence. 
 * 
 * @author Maciej Szymczak
 * @version 2016.11.23
 * @param  Folder name with <pageLayuouts>.layout and <objectdefinition>.object
 * @return result.csv
 */

public class WhoHasAccessToObject {
	
	//Treemap is an implementation of the SortedMap interface. It presents its keys in sorted order.
	static Map rows = new TreeMap<String, String>();
		
    public static void 	writeToCsv(String folderName) throws IOException {
		  FileWriter fw = new FileWriter(folderName+"\\result.csv");
		  
		  System.out.println("Creating file: result.csv");
		  
		  //file header
	      fw.write( "fileName,allowRead,allowCreate,allowDelete,allowEdit,viewAllRecords,modifyAllRecords\n" );
	      
		  for(Object row: rows.keySet()) {
			  String values = (String)rows.get(row);
		      fw.write( row+","+values+"\n" );
		  }		      
	      
		  fw.close();
    }
	
	/**
	* analyseXMLLayout
	 * @throws XPathExpressionException 
	*/
	public static void analyseXML (String path, String fileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		//rows.put(fileName, "");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);

		Document doc = factory.newDocumentBuilder().parse(new File(path+fileName));	

		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( "/Profile/objectPermissions[*]/object" );
		
		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String SObjectName = ((Element)currentNode).getTextContent();
			if (SObjectName.equals("Historical_PRN_Credit_Memo__c")) {
				String allowRead = "N/A";
				String allowCreate = "N/A";
				String allowDelete = "N/A";
				String allowEdit = "N/A";
				String viewAllRecords = "N/A";
				String modifyAllRecords = "N/A";
				NodeList childNodes = currentNode.getParentNode().getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node currentChildNode = childNodes.item(j);
					String name  = currentChildNode.getNodeName();
					String value = currentChildNode.getTextContent();
					if (name.equals("allowRead")) allowRead = value;
					if (name.equals("allowCreate")) allowCreate = value;
					if (name.equals("allowDelete")) allowDelete = value;
					if (name.equals("allowEdit")) allowEdit = value;
					if (name.equals("viewAllRecords")) viewAllRecords = value;
					if (name.equals("modifyAllRecords")) modifyAllRecords = value;
				}
				rows.put(fileName, allowRead+","+allowCreate+","+allowDelete+","+allowEdit+","+viewAllRecords+","+modifyAllRecords);
			}
		}	
	}
	

	/**
	* readFilesFromFolder
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	*/
	public static void readFilesFromFolder(final File folder) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	readFilesFromFolder(fileEntry);
	        } else {
	            if (fileEntry.getName().endsWith(".profile")  ) {
	            	System.out.println("profile:" + fileEntry.getName());
	            	analyseXML(folder.getPath()+"\\", fileEntry.getName());
	            }	
	            if (fileEntry.getName().endsWith(".permissionset")  ) {
	            	System.out.println("permissionset:" + fileEntry.getName());
	            	analyseXML(folder.getPath()+"\\", fileEntry.getName());
	            }	
	        }
	    }
	}		
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

		if (args.length!=1) {
			System.out.println("");
			System.out.println("**** usage: java -Dfile.encoding=UTF8 -jar jarname.jar <source folder>");
			System.exit(-1);
		}
		String folderName = args[0];
		readFilesFromFolder(new File(folderName));
		writeToCsv(folderName);
		System.out.println("Done!");
	}

}




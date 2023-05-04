import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
 * 
 * @author Maciej Szymczak
 * @version 2023.05.04
 * @param  Folder with profiles and object name
 * @return result.csv and result_you_can_open_me_in_Excel.xml
 */

public class WhoHasAccessToObject {
	
	//Treemap is an implementation of the SortedMap interface. It presents its keys in sorted order.
	static Map rows = new TreeMap<String, String>();
		
    public static void 	writeToCsv(String folderName) throws IOException {
		  System.out.println("Creating file: result.csv");

		  FileWriter fw = new FileWriter(folderName+"\\result.csv");
		  
		  //file header
	      fw.write( "fileName,allowRead,allowCreate,allowDelete,allowEdit,viewAllRecords,modifyAllRecords\n" );
	      
		  for(Object row: rows.keySet()) {
			  String values = (String)rows.get(row);
		      fw.write( row+","+values+"\n" );
		  }		      
	      
		  fw.close();
    }

    public static Node createRow(Document document, String pfileName, String commaSeparatedValues) {

	    StringTokenizer st1 = new StringTokenizer(commaSeparatedValues, ",");
	    String pallowRead = st1.nextToken();
	    String pallowCreate = st1.nextToken();
	    String pallowDelete = st1.nextToken();
	    String pallowEdit = st1.nextToken();
	    String pviewAllRecords = st1.nextToken();
	    String pmodifyAllRecords = st1.nextToken();
		       	
	    // create FirstName and LastName elements
	    Element fileName = document.createElement("fileName");
	    Element allowRead = document.createElement("allowRead");
	    Element allowCreate = document.createElement("allowCreate");
	    Element allowDelete = document.createElement("allowDelete");
	    Element allowEdit = document.createElement("allowEdit");
	    Element viewAllRecords = document.createElement("viewAllRecords");
	    Element modifyAllRecords = document.createElement("modifyAllRecords");

	    fileName.appendChild(document.createTextNode(pfileName));
	    allowRead.appendChild(document.createTextNode(pallowRead));
	    allowCreate.appendChild(document.createTextNode(pallowCreate));
	    allowDelete.appendChild(document.createTextNode(pallowDelete));
	    allowEdit.appendChild(document.createTextNode(pallowEdit));
	    viewAllRecords.appendChild(document.createTextNode(pviewAllRecords));
	    modifyAllRecords.appendChild(document.createTextNode(pmodifyAllRecords));

	    // create contact element
	    Element row = document.createElement("row");

	    row.appendChild(fileName);
	    row.appendChild(allowRead);
	    row.appendChild(allowCreate);
	    row.appendChild(allowDelete);
	    row.appendChild(allowEdit);
	    row.appendChild(viewAllRecords);
	    row.appendChild(modifyAllRecords);

	    return row;
	  }	
	
	private static void writeToXML(String folderName) throws SAXException, IOException, ParserConfigurationException {
    //source: http://www.java2s.com/Code/Java/XML/CreateselementnodeattributenodecommentnodeprocessinginstructionandaCDATAsection.htm
		
		/*
			<root>
			    <row>
			        <members>Historical_PRN_Credit_Memo__c</members>
			        <allowRead>CustomObject</allowRead>
			        <allowCreate>CustomObject</allowCreate>
			    </row>
			    ...
			    <row>
			        <members>*</members>
			        <allowRead>Profile</allowRead>
			        <allowCreate>CustomObject</allowCreate>
			    </row>
			</root>
		 */
		
		System.out.println("Creating file: result_you_can_open_me_in_Excel.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document;
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.newDocument();
		  
		Element root = document.createElement("root");
		document.appendChild(root);		
	    
		for(Object row: rows.keySet()) {
			  String commaSeparatedValues = (String)rows.get(row);
			  String pfileName = (String) row;
			  Node nodeRow = createRow(document, pfileName, commaSeparatedValues);
			  root.appendChild(nodeRow);		
		}		      
	    
		String fileName = folderName+"\\result_you_can_open_me_in_Excel.xml";
		saveChanges(document, fileName);				
	}
	
	private static void saveChanges(Document doc, String fileName) 
	{
		try {
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			tr.setOutputProperty(OutputKeys.METHOD, "xml");
			tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	
			// send DOM to file
			tr.transform(new DOMSource(doc), 
			new StreamResult(new FileOutputStream(fileName)));
	
		} catch (TransformerException te) {
			System.out.println(te.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}	
	
	/**
	* analyseXMLLayout
	 * @throws XPathExpressionException 
	*/
	public static void analyseXML (String path, String fileName, String sObjectName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {		
		/* analysed nodes:
		    <objectPermissions>
		        <allowCreate>false</allowCreate>
		        <allowDelete>false</allowDelete>
		        <allowEdit>false</allowEdit>
		        <allowRead>true</allowRead>
		        <modifyAllRecords>false</modifyAllRecords>
		        <object>Historical_PRN_Credit_Memo__c</object>
		        <viewAllRecords>true</viewAllRecords>
		    </objectPermissions>    
		 */		
		
		//to add all files to file
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
			if (SObjectName.equals(sObjectName)) {
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
	public static void readFilesFromFolder(final File folder, String sObjectName) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	readFilesFromFolder(fileEntry, sObjectName);
	        } else {
	            if (fileEntry.getName().endsWith(".profile")  ) {
	            	System.out.println("profile:" + fileEntry.getName());
	            	analyseXML(folder.getPath()+"\\", fileEntry.getName(), sObjectName);
	            }	
	            if (fileEntry.getName().endsWith(".permissionset")  ) {
	            	System.out.println("permissionset:" + fileEntry.getName());
	            	analyseXML(folder.getPath()+"\\", fileEntry.getName(), sObjectName);
	            }	
	        }
	    }
	}		
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

		if (args.length!=2) {
			System.out.println("@author Maciej Szymczak");
			System.out.println("**** usage: java -Dfile.encoding=UTF8 -jar jarname.jar <source folder> <objectname>");
			System.out.println("**** example: java -Dfile.encoding=UTF8 -jar jarname.jar C:\\salesforce_ant_44.0\\work\\adHocDownload Historical_PRN_Credit_Memo__c");
			System.exit(-1);
		}
		String folderName = args[0];
		String sObjectName = args[1];
		readFilesFromFolder(new File(folderName), sObjectName);
		writeToCsv(folderName);
		writeToXML(folderName);
		System.out.println("Done!");
	}

}




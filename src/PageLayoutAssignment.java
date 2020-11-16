import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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

public class PageLayoutAssignment {

	/*
	 * This class generates 3 csv files shown in the folder SampleDataPageLayoutAssignment
	 * 

	 * How to use it 
	 *  1. Download files. Here is an example package.xml 
	 *  Remember to add CustomObject and Layout section so that settings are included in the profile files. 
	 *  If you add Layout but with no CustomObject no Page layout on records type level will be added to the file.
	 *  Standard objects also can be added to CustomObjects.
	 *  

			<?xml version="1.0" encoding="UTF-8"?>
			<Package xmlns="http://soap.sforce.com/2006/04/metadata">
			    <types>
			        <members>*</members>
			        <members>Account</members>
			        <members>Opportunity</members>
			        <name>CustomObject</name>
			    </types>
			    <types>
			        <members>*</members>
			        <name>Layout</name>
			    </types>
			    <types>
			        <members>*</members>
			        <name>Profile</name>
			    </types>
			    <version>48.0</version>
			</Package>

	    2. Set the path in this apex class and run this class 
	 
	 * 
	 * */	
		
		public static String path = "C:\\Temp\\process\\";
		static String result = "profile,layout,recordType";
		
		//Treemap is an implementation of the SortedMap interface. It presents its keys in sorted order.
		static TreeMap<String, String> layouts = new TreeMap<String, String>();		
		static TreeMap<String, Integer> layoutsCnt = new TreeMap<String, Integer>();		
		
		public static void readXMLfile(String path, String fileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
			
			String profileName=fileName.substring(0, fileName.length() - 8);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setExpandEntityReferences(false);

			Document doc = factory.newDocumentBuilder().parse(new File(path+fileName));	

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			XPathExpression expression = xpath.compile( "//layoutAssignments[*]" ); 			
			NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currentNode = nodes.item(i);
				
				String layout = "";
				String recordType = "";
				
				NodeList childNodes = currentNode.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node currentChild = childNodes.item(j);
					if (currentChild.getNodeName()=="layout")     layout=  currentChild.getTextContent();
					if (currentChild.getNodeName()=="recordType") recordType=  currentChild.getTextContent();
				}
				layouts.put(layout,layouts.get(layout)+"|"+profileName);
				layoutsCnt.put(layout,layoutsCnt.get(layout)+1);
				result = result + profileName   +','+layout+','+recordType + "\n";
			}
			
		}
		
		public static void readLayoutsFromFolder(String path) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
			File folder = new File(path);
			for (final File fileEntry : folder.listFiles()) {
		    	//System.out.println("Reading "+ (fileEntry.getName())+"...");
		    	String layoutName=fileEntry.getName().substring(0, fileEntry.getName().length() - 7);
		    	layouts.put(layoutName,"");
		    	layoutsCnt.put(layoutName,0);
		    }
		}	
		public static void readProfilesFromFolder(String path) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
			File folder = new File(path);
			for (final File fileEntry : folder.listFiles()) {
		    	System.out.println("Reading "+ (fileEntry.getName())+"...");
		    	readXMLfile( path, fileEntry.getName()) ;
		    }
		}	

		public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
			readLayoutsFromFolder(path+"\\layouts\\");
			readProfilesFromFolder(path+"\\profiles\\");

			System.out.println("Creating file: "+path+"PageLayoutUsage.csv");
			FileWriter fw2 = new FileWriter(path+"PageLayoutUsage.csv");
            fw2.write("ProfileCnt,ObjectName,LayoutName,UsedByProfiles\n");
	        for(String key: layouts.keySet()){
	        	String objectName = key.substring(0,key.indexOf("-"));
	            fw2.write(layoutsCnt.get(key) +","+ objectName +","+ key +","+ layouts.get(key) + "\n");
	        }			
			fw2.flush();
	        
			System.out.println("Creating file: "+path+"OrphanedLayouts.csv");
			FileWriter fw3 = new FileWriter(path+"OrphanedLayouts.csv");
            fw3.write("LayoutName\n");
	        for(String key: layouts.keySet()){
	        	if (layouts.get(key)=="")
	              fw3.write(key+ "\n");
	        }			
			fw3.flush();

			System.out.println("Creating file: "+path+"PageLayoutAssignment.csv");
			FileWriter fw = new FileWriter(path+"PageLayoutAssignment.csv");
			fw.write( result );
			fw.write( "\n" );
			fw.flush();

			System.out.println("Done!");
		}


}

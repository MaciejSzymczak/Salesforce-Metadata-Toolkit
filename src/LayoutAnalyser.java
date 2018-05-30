import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
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

public class LayoutAnalyser {
	
	//Treemap is an implementation of the SortedMap interface. It presents its keys in sorted order.
	static Map fieldsAndLayouts = new TreeMap();
	static Map fields = new TreeMap();
	static Map layouts = new TreeMap();
	static Map AllObjectfields = new TreeMap();
		
    public static void 	writeToCsv(String folderName) throws IOException {
		  FileWriter fw = new FileWriter(folderName+"\\result.csv");
		  
		  System.out.println("Creating file: result.csv");
		  
		  //file header
	      fw.write( "Field Label, Field API Name,Count," );
		  for(Object layout: layouts.keySet()) {
		    	fw.write( layout.toString()+"," );
		  }
		  fw.write( "\n" );
		  
		  //file content
		  for(Object fieldLabel: AllObjectfields.keySet()) {
			  String fieldApiName = (String)AllObjectfields.get(fieldLabel);
			  fw.write( String.format("%s,%s,", fieldLabel, fieldApiName) );
			  //counter
			  Integer count = 0;
			  for(Object layout: layouts.keySet()) {
				    if (fieldsAndLayouts.containsKey(layout.toString()+"#"+fieldApiName)) {
				    	count++;
					} 
			  }		  
			  fw.write( count +"," );
              //YES flag
			  for(Object layout: layouts.keySet()) {
			    	fw.write( ((fieldsAndLayouts.containsKey(layout.toString()+"#"+fieldApiName))?"YES,":",") );
			  }	
			  fw.write( "\n" );
		  }	
		  fw.close();
    }
	
	/**
	* analyseXMLLayout
	 * @throws XPathExpressionException 
	*/
	public static void analyseXMLLayout (String path, String fileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		layouts.put(fileName, "");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);

		Document doc = factory.newDocumentBuilder().parse(new File(path+fileName));	

		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( "/Layout[*]/layoutSections[*]/layoutColumns[*]/layoutItems[*]/field" );
		
		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String fieldApiName = ((Element)currentNode).getTextContent();
		    Integer c = 0;
			if (fieldsAndLayouts.containsKey(fileName+"#"+fieldApiName))
			  c = (Integer)fieldsAndLayouts.get(fileName+"#"+fieldApiName);
			fieldsAndLayouts.put(fileName+"#"+fieldApiName, c);
			fields.put(fieldApiName, "");
			System.out.println( "    " + fieldApiName );
		}	
	}
	
	/**
	* analyseXMLObject
	 * @throws XPathExpressionException 
	*/
	public static void analyseXMLObject (String path, String fileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		layouts.put(fileName, "");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);

		Document doc = factory.newDocumentBuilder().parse(new File(path+fileName));	

		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		
		XPathExpression expression = xpath.compile( "/CustomObject[*]/fields[*]" );
		
		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String fullName = ((Element)currentNode).getElementsByTagName("fullName").item(0).getTextContent();
			String label;
			
			if (((Element)currentNode).getElementsByTagName("label").item(0)!=null)
				label = ((Element)currentNode).getElementsByTagName("label").item(0).getTextContent();
			else 
			    label = fullName;
			System.out.println( "    " + fullName );			
			AllObjectfields.put(fileName.split("\\.")[0] + ": " + label ,fullName);
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
	            if (fileEntry.getName().endsWith(".layout")  ) {
	            	System.out.println("analyseXMLLayout:" + fileEntry.getName());
	            	analyseXMLLayout(folder.getPath()+"\\", fileEntry.getName());
	            }	
	            if (fileEntry.getName().endsWith(".object")  ) {
	            	System.out.println("analyseXMLObject:" + fileEntry.getName());
	            	analyseXMLObject(folder.getPath()+"\\", fileEntry.getName());
	            }	
	        }
	    }
	}		
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		//"C:\\CommonProgs\\TrickSQL\\ConfigFiles\\Jnj\\tasks\\SFDC\\Customs\\metadata\\layouts"

	    System.out.println("* This java program produces a matrix that shows what fields are used on what page layouts (the matrix: fields x page layouts).");
		System.out.println("* It reads the content of page layouts and the definition of the object (you can download the .layout and .object files using migration tool) and conglomerates data in a form of a readable matrix."); 
		System.out.println("* It Reads given folder on the disk (entry parameter of java program) and saves the result in the file result.csv.");
		System.out.println("* It analyzes page layouts, not visual force pages (so far).");
		System.out.println("* It can be used for identification what region uses what fields and for identification what fields are not used at all.");
		System.out.println("* Column C says the number of page layouts on which field is used. 0 mean the field is suspicious and should be prosecuted  rather than instant death sentence."); 
		System.out.println("Version 2016.11.23     Author: Maciej Szymczak");
		
		if (args.length!=1) {
			System.out.println("");
			System.out.println("**** usage: java -Dfile.encoding=UTF8 -jar jarname.jar <source folder>");
			System.out.println("**** <source folder> is folder name with files: <pageLayuouts>.layout and <objectdefinition>.object");
			System.exit(-1);
		}
		String folderName = args[0];
		readFilesFromFolder(new File(folderName));
		writeToCsv(folderName);
	}

}

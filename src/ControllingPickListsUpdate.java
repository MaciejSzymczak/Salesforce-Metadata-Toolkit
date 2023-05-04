import java.io.File;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class ControllingPickListsUpdate {


	//variables used by findNode
	static String inNodePath;
	static String inSearchText;
	static int inLevelsUp;
	static Node outResultNode;	
	static boolean valueFound; 
	static boolean debugMode = false;

	static Node fieldNode;
	static Node picklistValueNode;


	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, MyException {

		String ControllingFieldValue;
		List<String> pickListValues= new ArrayList<String>();
		String fileName = "C:\\Users\\mszymcz1\\Documents\\MigrationTool_35\\medev\\ActivityUpload\\objects\\Activity.object";
		
		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("benefits of DD");
		pickListValues.add("comfort & Dual Technology");
		pickListValues.add("ASD: vision clear & stable");
		pickListValues.add("quick & easy to fit");
		pickListValues.add("the most extensive range");
		pickListValues.add("astig.correction from >0,75DC");
		pickListValues.add("competitive msg vs DACP Toric");
		pickListValues.add("competitive msg vs Clariti Tor");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("ASD: vision clear & stable");
		pickListValues.add("ASD: beyond chair experience");
		pickListValues.add("range expansion");
		pickListValues.add("no lens feeling-even at PC");
		pickListValues.add("shorter is better");
		pickListValues.add("quick & easy to fit");
		pickListValues.add("competitive msg vs AO Toric");
		pickListValues.add("competitive msg vs Biof Toric");
		pickListValues.add("competitive msg other");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("Best for Intense wear, no lens");
		pickListValues.add("comparable to natural eye");
		pickListValues.add("DAILIES TOTAL 1 argumentation");
		pickListValues.add("handling importance");
		pickListValues.add("DD: healthiest way of CL wear");
		pickListValues.add("competitive comparison MyDay");
		pickListValues.add("competitive comparison BioTrue");
		pickListValues.add("competitive comparison Clariti");
		pickListValues.add("competitive comparison Total1");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("Lysozyme & sensitive eyes");
		pickListValues.add("end-day comfort - LACREON");
		pickListValues.add("competitive comparison MyDay");
		pickListValues.add("competitive comparison BioTrue");
		pickListValues.add("competitive comparison Clariti");
		pickListValues.add("competitive comparison DACP");
		pickListValues.add("DAILIES TOTAL 1 argumentation");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST® Multifocal";
		pickListValues.clear();
		pickListValues.add("unique optical design");
		pickListValues.add("quick & easy fit");
		pickListValues.add("fitting guide");
		pickListValues.add("competitive comparison DACP");
		pickListValues.add("competitive comparison MF Reus");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("retention with ACUVUE");
		pickListValues.add("ultra-smooth for no-tired eyes");
		pickListValues.add("no lens feeling-even at PC");
		pickListValues.add("competitive comparison");
		pickListValues.add("upgrade from other REUs");
		pickListValues.add("shorter is better");
		pickListValues.add("digital eyestrain & tear film");
		pickListValues.add("compet.comparison Air Optix");
		pickListValues.add("compet.comparison Biofinity");
		pickListValues.add("compet.comparison other");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- wearer needs");
		pickListValues.add("fit more astigmats");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									

		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("CL is option for astigmats too");
		pickListValues.add("lasting comfort");
		pickListValues.add("clear and stable vision");
		pickListValues.add("freedom & convenience ");
		pickListValues.add("benefit vs sph correction");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("CL is option for astigmats too");
		pickListValues.add("lasting comfort");
		pickListValues.add("benefit vs sph correction");
		pickListValues.add("clear and stable vision");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("Best for Intense wear, no lens");
		pickListValues.add("DD: best way to start wearing");
		pickListValues.add("choice for holiday");
		pickListValues.add("freedom & convenience");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("sensitive eye & allday comfort");
		pickListValues.add("DD: best way to start wearing");
		pickListValues.add("freedom & convenience");
		pickListValues.add("choice for holiday");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST® Multifocal";
		pickListValues.clear();
		pickListValues.add("crisp vision at every distance");
		pickListValues.add("sensitive eye & allday comfort");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("unbeaten in comfort");
		pickListValues.add("no lens feeling-even at PC");
		pickListValues.add("prevent tired eyes");
		pickListValues.add("benefits vs other RUs");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- wide choice");
		pickListValues.add("CLs also for astigmats");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									

		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("DD Toric untapped opportunity");
		pickListValues.add("the most extensive range ");
		pickListValues.add("unmasking pts for satisfaction");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("astigmats more loyal to you");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("toric seg growth &profitabilty");
		pickListValues.add("unmasking pts for satisfaction");
		pickListValues.add("astigmats more loyal to you");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("commercial discussion");
		pickListValues.add("entry point to CL & teens");
		pickListValues.add("DD: segm growth &profitability");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("dual purchase with specs / DR");
		pickListValues.add("patient'll pay more for UV CLs");
		pickListValues.add("holiday upgrade to DD");
		pickListValues.add("handling importance");
		pickListValues.add("big pack sizes - grow business");
		pickListValues.add("Retention with ACUVUE");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("commercial discussion");
		pickListValues.add("DD: entry point to CL & teens");
		pickListValues.add("DD: segm growth &profitability");
		pickListValues.add("dual purchase with specs / DR");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("holiday upgrade to DD");
		pickListValues.add("big pack sizes - grow business");
		pickListValues.add("Retention with ACUVUE");
		pickListValues.add("Full 1DM family to help growth");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST® Multifocal";
		pickListValues.clear();
		pickListValues.add("new & continuity wearers 40+");
		pickListValues.add("MOIST family for all Pt needs");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("retention with ACUVUE");
		pickListValues.add("price diff to monthly CL comm.");
		pickListValues.add("upgrade from other RUs");
		pickListValues.add("challenge overwear objection");
		pickListValues.add("big pack sizes - grow business");
		pickListValues.add("commercial discussion");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- grow business");
		pickListValues.add("practice grow with more toric");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									

		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("benefits of DD ");
		pickListValues.add("comfort & Dual Technology");
		pickListValues.add("ASD: vision clear & stable");
		pickListValues.add("quick & easy to fit");
		pickListValues.add("the most extensive range");
		pickListValues.add("astig.correction from >0,75DC");
		pickListValues.add("competitive msg vs DACP Toric");
		pickListValues.add("competitive msg vs Clariti Tor");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("ASD: vision clear & stable");
		pickListValues.add("ASD: beyond chair experience");
		pickListValues.add("range expansion");
		pickListValues.add("no lens feeling-even at PC");
		pickListValues.add("shorter is better");
		pickListValues.add("quick & easy to fit");
		pickListValues.add("competitive msg vs AO Toric");
		pickListValues.add("competitive msg vs Biof Toric");
		pickListValues.add("competitive msg other");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("Best for Intense wear, no lens");
		pickListValues.add("comparable to natural eye");
		pickListValues.add("DAILIES TOTAL 1 argumentation");
		pickListValues.add("handling importance");
		pickListValues.add("DD: healthiest way of CL wear");
		pickListValues.add("competitive comparison MyDay");
		pickListValues.add("competitive comparison BioTrue");
		pickListValues.add("competitive comparison Clariti");
		pickListValues.add("competitive comparison Total1");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("Lysozyme & sensitive eyes");
		pickListValues.add("end-day comfort - LACREON");
		pickListValues.add("handling importance");
		pickListValues.add("DD: healthiest way of CL wear");
		pickListValues.add("competitive comparison MyDay");
		pickListValues.add("competitive comparison BioTrue");
		pickListValues.add("competitive comparison Clariti");
		pickListValues.add("competitive comparison DACP");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("retention with ACUVUE");
		pickListValues.add("ultra-smooth for no-tired eyes");
		pickListValues.add("no lens feeling-even at PC");
		pickListValues.add("competitive comparison");
		pickListValues.add("upgrade from other RUs");
		pickListValues.add("shorter is better");
		pickListValues.add("digital eyestrain & tear film");
		pickListValues.add("compet.comparison Air Optix");
		pickListValues.add("compet.comparison Biofinity");
		pickListValues.add("compet.comparison other");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- wearer needs");
		pickListValues.add("fit more astigmats");
		updateXML(fileName, "Professional", ControllingFieldValue, pickListValues, false);									

		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("CL is option for astigmats too");
		pickListValues.add("lasting comfort");
		pickListValues.add("clear and stable vision");
		pickListValues.add("freedom & convenience ");
		pickListValues.add("benefit vs sph correction");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("CL is option for astigmats too");
		pickListValues.add("lasting comfort");
		pickListValues.add("benefit vs sph correction");
		pickListValues.add("clear and stable vision");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("Best for Intense wear, no lens");
		pickListValues.add("DD: best way to start wearing");
		pickListValues.add("choice for holiday");
		pickListValues.add("freedom & convenience");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("sensitive eye & allday comfort");
		pickListValues.add("DD: best way to start wearing");
		pickListValues.add("freedom & convenience");
		pickListValues.add("choice for holiday");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("unbeaten in comfort-demanding");
		pickListValues.add("no lens feeling- am to pm");
		pickListValues.add("prevent tired eyes");
		pickListValues.add("benefits vs other RUs");
		pickListValues.add("UV. Protect skin.Protect eyes.");
		pickListValues.add("no lens feeling-even at PC");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- wide choice");
		pickListValues.add("CLs also for astigmats");
		updateXML(fileName, "Patient", ControllingFieldValue, pickListValues, false);									

		ControllingFieldValue= "(EU) 1-DAY ACUVUE MOIST Astigm";
		pickListValues.clear();
		pickListValues.add("DD Toric untapped opportunity");
		pickListValues.add("the most extensive range ");
		pickListValues.add("unmasking pts for satisfaction");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("astigmats more loyal to you");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® for ASTIGM";
		pickListValues.clear();
		pickListValues.add("toric seg growth &profitabilty");
		pickListValues.add("unmasking pts for satisfaction");
		pickListValues.add("astigmats more loyal to you");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® TruEye™";
		pickListValues.clear();
		pickListValues.add("commercial discussion");
		pickListValues.add("entry point to CL & teens");
		pickListValues.add("DD: segm growth &profitability");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("dual purchase with specs / DR");
		pickListValues.add("patient'll pay more for UV CLs");
		pickListValues.add("holiday upgrade to DD");
		pickListValues.add("teach handling importance");
		pickListValues.add("big pack sizes - grow business");
		pickListValues.add("Retention with ACUVUE");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) 1-DAY ACUVUE® MOIST®";
		pickListValues.clear();
		pickListValues.add("commercial discussion");
		pickListValues.add("DD: entry point to CL & teens");
		pickListValues.add("DD: segm growth &profitability");
		pickListValues.add("dual purchase with specs / DR");
		pickListValues.add("DD: easiest way- entry&retain");
		pickListValues.add("choice for holiday");
		pickListValues.add("big pack sizes - grow business");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) ACUVUE® OASYS® HYDRACLEAR";
		pickListValues.clear();
		pickListValues.add("retention with ACUVUE");
		pickListValues.add("price diff to monthly CL comm.");
		pickListValues.add("upgrade from other RUs");
		pickListValues.add("teach handling importance");
		pickListValues.add("big pack sizes - grow business");
		pickListValues.add("challenge overwear objection");
		pickListValues.add("commercial discussion");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									
		ControllingFieldValue= "(EU) Portfolio";
		pickListValues.clear();
		pickListValues.add("full ABP intro - wearer needs");
		pickListValues.add("pack sizes- grow business");
		pickListValues.add("practice grow with more toric");
		updateXML(fileName, "Practice", ControllingFieldValue, pickListValues, false);									

	}
	
	private static void updateXML(String fileName, String fieldAPIName, String ControllingFieldValue, List<String> pickListValues, Boolean deletePreviousAssigments) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, MyException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setExpandEntityReferences(false);

		Document doc = factory.newDocumentBuilder().parse(new File(fileName));			 

		boolean missingValues = false;
		for(int i=0;i < pickListValues.size(); i++) {			
			if (!pickListValueExists(doc, fieldAPIName, pickListValues.get(i).trim() )) {
				if (missingValues==false) {
					System.out.println("****" + fieldAPIName  +" (" +ControllingFieldValue+ ")- Update ERROR: Missing values for field " + fieldAPIName +". Add values manually and try again.");
					missingValues=true;
				}
				System.out.println( pickListValues.get(i) );
			}	
			  
		}
		if (missingValues) return;

		if (deletePreviousAssigments) 
		    deleteControllingFieldValue(doc, fieldAPIName, ControllingFieldValue);
		for(int i=0;i < pickListValues.size(); i++) {			
			addControllingFieldValue(doc, fieldAPIName, pickListValues.get(i).trim(), ControllingFieldValue );
		}
		
		saveChanges(doc, fileName);				
		System.out.println("****" + fieldAPIName +" (" +ControllingFieldValue+ ")- Update OK");

		//not usefull. just limit the lists via record type
		//if (!deletePreviousAssigments)
		//	listControllingFieldValue(doc,fieldAPIName,ControllingFieldValue,pickListValues);
	
	}

	private static boolean pickListValueExists(Document doc, String fieldAPIName, String pickListValue ) throws XPathExpressionException
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( String.format("/CustomObject/fields[label=\"%s\"]/picklist/picklistValues[translate(fullName, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')=\"%s\"]", fieldAPIName,pickListValue.toLowerCase() ) );

		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		return (nodes.getLength()!=0);
	}

	private static boolean pickListValueAndControllingFieldValueExists(Document doc, String fieldAPIName, String pickListValue, String controllingFieldValue ) throws XPathExpressionException
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( String.format("/CustomObject/fields[label=\"%s\"]/picklist/picklistValues[translate(fullName, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')=\"%s\"]/controllingFieldValues[text()=\"%s\"]", fieldAPIName,pickListValue.toLowerCase(), controllingFieldValue ) );

		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		return (nodes.getLength()!=0);
	}

	
	private static void deleteControllingFieldValue(Document doc, String fieldAPIName, String controllingFieldValue ) throws XPathExpressionException
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( String.format("/CustomObject/fields[label=\"%s\"]/picklist/picklistValues[*]/controllingFieldValues[text()=\"%s\"]", fieldAPIName,controllingFieldValue ) );

		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			currentNode.getParentNode().removeChild(currentNode);
		}
	}

	private static boolean StringListContains(List<String> pickListValues, String value) {
		for(int i=0;i < pickListValues.size(); i++) {			
			if (pickListValues.get(i).toLowerCase().trim().equals(value.toLowerCase().trim()))
			  return true;
			}	
		return false;	  		
	}
	
	//almost the same as delete, but it lists the values (do not deletes)
	private static void listControllingFieldValue(Document doc, String fieldAPIName, String controllingFieldValue,List<String> pickListValues ) throws XPathExpressionException
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		XPathExpression expression = xpath.compile( String.format("/CustomObject/fields[label=\"%s\"]/picklist/picklistValues[*]/controllingFieldValues[text()=\"%s\"]", fieldAPIName,controllingFieldValue ) );
		
		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String pickListValue = ((Element)currentNode.getParentNode()).getElementsByTagName("fullName").item(0).getTextContent();
			if (!StringListContains(pickListValues,pickListValue)) 
			{  System.out.println("BE AWARE OF EXTRA VALUE- DISABLE VIA RECORD TYPE:" + pickListValue); }
			//else      
			     //System.out.println("VALUE OK:" + pickListValue);
		}
	}
	
	
	private static void addControllingFieldValue(Document doc, String fieldAPIName, String pickListValue, String controllingFieldValue ) throws MyException, XPathExpressionException
	{
		if (pickListValueAndControllingFieldValueExists(doc,fieldAPIName,pickListValue,controllingFieldValue))
		    return;
		//use below statement to obtain the list of paths and values
		//debugMode = true;
		//findNode(doc, (Element)fieldNode, "???", "???", 2);		
		findNode(doc, doc.getDocumentElement(), ">fields>label", fieldAPIName, 2);		
		if (valueFound) {
			fieldNode = outResultNode;
			//debugMode = true;
			findNode(doc, (Element)fieldNode, ">picklist>picklistValues>fullName", pickListValue, 2);			
		}
		else {
		 throw new MyException("No fieldAPIName found:" + fieldAPIName);
		} 
		
		if (valueFound) {
			picklistValueNode = outResultNode;
			//<>Default Non POA</controllingFieldValues>
			Element newElement = doc.createElement("controllingFieldValues");
			newElement.appendChild(doc.createTextNode(controllingFieldValue));
			picklistValueNode.appendChild(newElement);
		 }
		else {
			 throw new MyException("No pickListValue found:" + pickListValue);
		} 

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

	//actually this approach is obsolete. should be replaced by xpath
	private static void findNode(final Document doc, final Element e, String pinNodePath, String pinSearchText, int pinLevelsUp) {
		inNodePath = pinNodePath; 
		inSearchText = pinSearchText;
		inLevelsUp = pinLevelsUp;
		valueFound = false;
		findNodeInternal(doc, e, "");
	}

	private static void findNodeInternal(final Document doc, final Element e, String nodePath) {
		final NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			final Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				//System.out.println(currentNodeName+n.getNodeName());
				findNodeInternal(doc, (Element) n, nodePath + ">" + n.getNodeName());
				if (valueFound) return; 
			}		
			
			if (debugMode && !(n.getNodeValue()+"").equals(""))
				 System.out.println("Examined:'" + nodePath +"' value:'" + n.getNodeValue() + "'  searchedText:'" + inSearchText + "'");
			if ((n.getNodeValue()+"").equalsIgnoreCase(inSearchText) && nodePath.equals(inNodePath)) {
				if (debugMode) 
					System.out.println("found value:'" + inSearchText + "' in path:'" + nodePath + "'");
				if (inLevelsUp==0)  outResultNode = n;
				if (inLevelsUp==1)  outResultNode = n.getParentNode();
				if (inLevelsUp==2)  outResultNode = n.getParentNode().getParentNode();
				if (inLevelsUp==3)  outResultNode = n.getParentNode().getParentNode().getParentNode();
				valueFound = true;
				return;
			} 
		}
	}	
	
	
}

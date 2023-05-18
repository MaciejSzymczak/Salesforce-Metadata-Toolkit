	import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;

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

public class UpdateProfiles {

	/*
	 * This class updates a Salesforce profiles: removes or grants access to specified fields.
	 * The class updates all xml files in the folder. It sets false/true for the list of provided fields
	 * 
	 * @author Maciej Szymczak
	 * @version 2023.05.18

	<Profile xmlns="http://soap.sforce.com/2006/04/metadata">
	    <custom>true</custom>
	    <fieldPermissions>
	        <editable>false</editable>              <---- This element is updated
	        <field>Account.ARR_Tier__c</field>
	        <readable>false</readable>              <---- This element is updated
	    </fieldPermissions>
	    <fieldPermissions>
	        <editable>false</editable>              <---- This element is updated
	        <field>Account.AUDDIS_Ref__c</field>
	        <readable>false</readable>              <---- This element is updated
	    </fieldPermissions>
	    <fieldPermissions>
	        <editable>false</editable>              <---- This element is updated
	        <field>Account.AccountID18__c</field>
	        <readable>false</readable>              <---- This element is updated
	    </fieldPermissions>


	 * How to use it 
	 *  1. Download profiles. Here is an example package.xml
	 *     remember to add CustomObject section so that fields are included in the profile files. Yes, standard objects also can be added to CustomObjects.
	 *  
			<?xml version="1.0" encoding="UTF-8"?>
			<Package xmlns="http://soap.sforce.com/2006/04/metadata">
			    <types>
			        <members>Account</members>
			        <members>Contact</members>
			        <members>YourCustomObject__c</members>
			        <name>CustomObject</name>
			    </types>
			    <types>
			        <members>*</members>
			        <name>Profile</name>
			    </types>
			    <version>56.0</version>
			</Package>

	    2. Set the parameters: path, valueForEditable, valueForReadable, fieldsforUpdate
	    3. Run this class 
        4. Upload the updated profiles. Skip CustomObject section in package.xml.
	  
			<?xml version="1.0" encoding="UTF-8"?>
			<Package xmlns="http://soap.sforce.com/2006/04/metadata">
			    <types>
			        <members>FI Contract Manager.profile</members>
			        <members>FI Marketing User.profile</members>
			        <members>FI Standard User -Lightning.profile</members>
			        <members>FI Standard User.profile</members>
			        <members>FI System Administrator.profile</members>
			        <name>Profile</name>
			    </types>
			    <version>56.0</version>
			</Package>
	 
	 * 
	 * */	
		
		public static String path = "C:\\salesforce_ant_44.0\\work\\adHocDownload\\profiles\\";
		public static String valueForEditable = "false";
		public static String valueForReadable = "true";
		public static String fieldsforUpdate =  
				",Account.SW_New_Biz_Propensity_Score__c"       +
				",Account.SW_G2_Intent_Last_30_Days__c"         +
				",Account.SW_Bombora_Intent_Last_30_Days__c"    +
				",Account.Email_Click_Count_Last_30_Days__c"    +
				",Account.SW_Email_Click_Count_Last_30_Days__c" +
				",Account.Form_Submit_Count_Last_30_Days__c"    +
				",Account.SW_Form_Submit_Count_Last_30_Days__c" +
				",Account.WRK_PRN_Competitor_Used__c"           +
				",Contact.SW_New_Biz_Propensity_Score__c"       +
				",Contact.SW_G2_Intent_Last_30_Days__c"         +
				",Contact.SW_Bombora_Intent_Last_30_Days__c"    +
				",Contact.Email_Click_Count_Last_30_Days__c"    +
				",Contact.SW_Email_Click_Count_Last_30_Days__c" +
				",Contact.Form_Submit_Count_Last_30_Days__c"    +
				",Contact.SW_Form_Submit_Count_Last_30_Days__c" +
				",Contact.AIQ_Update_Date_Trigger__c"           +
				",Contact.WRK_PRN_Competitor_Used__c"           +
				",Lead.WRK_PRN_Competitor_Used__c"           +
				",Lead.SW_New_Biz_Propensity_Score__c"          +
				",Lead.SW_G2_Intent_Last_30_Days__c"            +
				",Lead.SW_Bombora_Intent_Last_30_Days__c"       +
				",Lead.Email_Click_Count_Last_30_Days__c"       +
				",Lead.SW_Email_Click_Count_Last_30_Days__c"    +
				",Lead.Form_Submit_Count_Last_30_Days__c"       +
				",Lead.SW_Form_Submit_Count_Last_30_Days__c"    +
				",";
		
		
		public static void readFilesFromFolder(String path) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
			File folder = new File(path);
			for (final File fileEntry : folder.listFiles()) {
		    	System.out.println((fileEntry.getName()));
		    	updateXMLFile( path, fileEntry.getName()) ;
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
		
		public static void updateXMLFile(String path, String fileName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setExpandEntityReferences(false);

			Document doc = factory.newDocumentBuilder().parse(new File(path+fileName));	

			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpath = xpf.newXPath();
			
			XPathExpression expression = xpath.compile( "//fieldPermissions[*]/field" ); 
			
			NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currentNode = nodes.item(i);
				String fullName = ((Element)currentNode).getTextContent();
				if (fieldsforUpdate.contains(","+fullName+",")) {
					System.out.println( fullName );			
		
					NodeList childNodes = currentNode.getParentNode().getChildNodes();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node currentChild = childNodes.item(j);
						
						if (currentChild.getNodeName()=="editable") {
							currentChild.setTextContent(  valueForEditable );
							//System.out.println( "    " + currentChild.getNodeName() + "    " + currentChild.getTextContent() );
						}	
						if (currentChild.getNodeName()=="readable") {
							currentChild.setTextContent(  valueForReadable );
							//System.out.println( "    " + currentChild.getNodeName() + "    " + currentChild.getTextContent() );
						}	
					}
				}  
				
			}
			saveChanges(doc, path+fileName);			
		}
		
		public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
			readFilesFromFolder(path);
			System.out.println("Done!");
		}

}

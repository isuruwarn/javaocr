package org.warn.ocr.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;




public class XMLUtils {
	
	
	private static final String ENCODING = "UTF-8";
	private static final String INDENT = "yes";
	private static final String XPATH_EVAL_ID = "//*[@id='%s']";
	
	
	
	public static synchronized Document parse( String sourceXML ) {
		Document doc = null;
		try {
			File xmlFile = new File(sourceXML);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			//docBuilderFactory.setValidating(true);
			//docBuilderFactory.setNamespaceAware(true);
			//docBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlFile);
		}
		catch( ParserConfigurationException | SAXException | IOException e ) {
			e.printStackTrace();
		}
		return doc;
	}
	
	
	
	
	public static synchronized boolean update( String sourceXML, Document doc ) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty( OutputKeys.ENCODING, ENCODING );
			tf.setOutputProperty( OutputKeys.INDENT, INDENT );
			Writer out = new FileWriter( new File(sourceXML) );
			tf.transform( new DOMSource(doc), new StreamResult(out) );
			return true;
		} catch (TransformerException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	public static synchronized boolean deleteNode( String charCode, Node root ) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node node = (Node) xpath.evaluate( String.format( XPATH_EVAL_ID, charCode ), root, XPathConstants.NODE);
			if( node != null ) { // remove existing node
				Node parent = node.getParentNode();
				parent.removeChild(node);
				return true;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

}

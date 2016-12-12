package inz.lwe_v103;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class myDOMParser {

    public void MyDomParser(){
    }
    private String directory = "";
    private String fileName = "";
    private String tagName = "";
    private String valueAttributeToFind = "";
    private String nameAttributeToFind ="";
    private String nameAttributeToRead="";

    public void set_directory(String dir){
        this.directory = dir;
    }

    public void set_File(String file){
        this.fileName = file;
    }

    public void set_tagName(String tag) {
        this.tagName=tag;
    }

    public void set_nameAttributeToFind(String att){
        this.nameAttributeToFind = att;
    }

    public void set_valueAttributeToFind(String att){
        this.valueAttributeToFind = att;
    }

    public void set_nameAttributeToRead(String att){
        this.nameAttributeToRead = att;
    }

    public String compareAndRead_xmlValue(){
        String value = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)){
                    return element.getAttribute(nameAttributeToRead);
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            value = "error: "+e.toString();
        }
        return value;
    }


    public String[] compareAndRead_xmlValues(){
        String[] value= new String[10];
        int count = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)){
                    value[count]= element.getAttribute(nameAttributeToRead);
                    count++;
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            value[0] = "error: "+e.toString();
        }
        return value;
    }


    public String get_PerformerId(){
        String value = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)) {
                    NodeList nodes = el.getChildNodes();
                    for (int j = 0; j < nodes.getLength(); j++) {
                        Node node = nodes.item(j);
                        if (node.getNodeName().equals("xpdl:Performers")) {
                            NodeList nodeList = node.getChildNodes();
                            for (int k = 0; k < nodeList.getLength(); k++) {
                                Node lastNode = nodeList.item(k);
                                if (lastNode.getNodeName().equals("xpdl:Performer"))
                                    return lastNode.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            value = "error: "+e.toString();
        }
        return value;
    }

    public String get_ParticipantEmail(String id){
        String error="";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++) {
                Node el = ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)) {
                NodeList participantNodes = el.getChildNodes();
                    for(int j=0; j<participantNodes.getLength();j++){
                        Node node = participantNodes.item(j);
                        if(node.getNodeName().equals("xpdl:ExtendedAttributes")){
                            NodeList nodeList1=node.getChildNodes();
                            for(int k=0; k<nodeList1.getLength();k++){
                                Node node1=nodeList1.item(k);
                                if(node1.getNodeName().equals("xpdl:ExtendedAttribute")){
                                    Element element1 = (Element) node1;
                                    return element1.getAttribute("Value");
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            error = "error: "+e.toString();
        }
        return error;
    }


    public String read_xmlValue(){
        String value ="";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                return element.getAttribute(nameAttributeToRead);
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            value = "error: "+e.toString();
        }
        return value;
    }

    public String get_email(){
        String email = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)){
                    NodeList node1 = el.getChildNodes();
                    Node el2 = node1.item(3);
                    NodeList l2 = el2.getChildNodes();
                    Node el3 = l2.item(1);
                    Element el4 = (Element) el3;
                    return el4.getAttribute("Value");
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            email = "error: "+e.toString();
        }
        return email;
    }

    public Boolean update_xpdlValue(String value){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(directory +"/"+fileName));
            NodeList ExtAtt = doc.getElementsByTagName(tagName);
            for (int i=0; i<ExtAtt.getLength(); i++){
                Node el =  ExtAtt.item(i);
                Element element = (Element) el;
                if (element.getAttribute(nameAttributeToFind).equals(valueAttributeToFind)){
                    NamedNodeMap attr = el.getAttributes();
                    Node nodeAttr = attr.getNamedItem("Value");   ///ToDo
                    nodeAttr.setTextContent(value);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(directory +"/"+fileName));
            transformer.transform(source, result);
        }
        catch (ParserConfigurationException | SAXException | IOException |
                TransformerException e) {
            return false;
        }
        return true;
    }
}

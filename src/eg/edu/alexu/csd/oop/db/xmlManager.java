/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Mustafa
 */
class xmlManager {

    private static xmlManager manger;

    xmlManager() {

    }

    public static synchronized xmlManager managergetter(){

        if (manger == null) {

            manger = new xmlManager();
        }

        return manger;

    }

    public List<Map<String, String>> read(String filceName) throws FileNotFoundException, XMLStreamException {

        String fileName = "Data\\" + filceName + ".xml";
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        Map<String, String> map = new LinkedHashMap<String, String>();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();// creaating a new object of this class which provides methods to read from xml file
        try {// just to catch errors msh aktr

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            //the way stax work is by handling events this is an event reader
            //by using it u can look at the next element and return its data
            int anElementHasStarted = 0;
            while (xmlEventReader.hasNext()) {// while i can read events
                XMLEvent xmlEvent = xmlEventReader.nextEvent();// get the next event
                if (xmlEvent.isStartElement()) {// start element means <anyString>
                    StartElement startElement = xmlEvent.asStartElement();/// reads String content only
                    if (startElement.getName().getLocalPart().equals("Element")) {

                        map = new LinkedHashMap<String, String>();
                        anElementHasStarted = 1;
                    } else if (anElementHasStarted == 1) {

                        xmlEvent = xmlEventReader.nextEvent();
                        map.put(startElement.getName().getLocalPart(), xmlEvent.asCharacters().getData());
                        //     System.out.println(xmlEvent.asCharacters().getData());

                        // get the next event which is the text after the start
                    }
                }
                if (xmlEvent.isEndElement()) {// if this event is an end event </anyString>

                    EndElement endElement = xmlEvent.asEndElement();/// end element is an object which has 3 components
                    ///one of them is localName which is the text part only between the 2 <>
                    if (endElement.getName().getLocalPart().equals("Element")) {/// gets the string part of the end and compares
                        data.add(map);
                    }
                }
            }

            xmlEventReader.close();
        } catch (XMLStreamException | FileNotFoundException e) {/// if file name is wrong or xml format is not well
            System.out.println("No Such File\n");
            e.printStackTrace();
        }
      //  System.out.print(data);
        return data;

    }

    public void create(List<Map<String, String>> list, String filceName) {

        String fileName = "Data\\" + filceName + ".xml";

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        try {

            XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new FileOutputStream(fileName), "UTF-8");

            //For Debugging - below code to print XML to Console
            //  xmlEventWriter = xmlOutputFactory.createXMLEventWriter(System.out);
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent dend = eventFactory.createDTD("\t");

            StartDocument startDocument = eventFactory.createStartDocument();
            xmlEventWriter.add(startDocument);
            xmlEventWriter.add(end);
            StartElement configStartElement = eventFactory.createStartElement("", "", filceName);
            xmlEventWriter.add(configStartElement);
            xmlEventWriter.add(end);
            xmlEventWriter.add(dend);

            // Write the element nodes
            int i = 0;
            for (Map<String, String> map : list) {
                i++;
                StartElement configStartElement2 = eventFactory.createStartElement("", "", "Element");
                xmlEventWriter.add(configStartElement2);
                xmlEventWriter.add(end);
                xmlEventWriter.add(dend);

                Set<String> elementNodes = map.keySet();
                for (String key : elementNodes) {

                    createNode(xmlEventWriter, key, map.get(key));

                }

                xmlEventWriter.add(end);
                xmlEventWriter.add(eventFactory.createEndElement("", "", "Elemnt"));
                xmlEventWriter.add(end);

            }

            xmlEventWriter.add(eventFactory.createEndElement("", "", filceName));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndDocument());
            xmlEventWriter.add(end);
            //*/
            xmlEventWriter.close();

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void createNode(XMLEventWriter eventWriter, String element, String value) throws XMLStreamException {
        XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();

        //Create Start node
        StartElement sElement = xmlEventFactory.createStartElement("", "", element);

        eventWriter.add(sElement);
        //Create Content
        Characters characters = xmlEventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = xmlEventFactory.createEndElement("", "", element);
        eventWriter.add(eElement);

    }

    public List<String> getXsdElements(String fileName) throws FileNotFoundException, XMLStreamException {

        List<String> columnsName = new ArrayList<String>();;
        String x, y;
        try {

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            InputStream input = new FileInputStream(new File("Data\\" + fileName + ".xsd"));
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(input);

            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    x = xmlStreamReader.getAttributeValue(1);
                    y = xmlStreamReader.getAttributeValue(0);
                    //da kamyt if condition 3shn bamn3 hodos ay error 3'er matwk3 y3knan 3alena ? 3shn attribute zero feh table name w element w dol msh 3yznohom
                    if (x == null || x.equalsIgnoreCase("qualified") || y.equalsIgnoreCase(fileName) || y.equalsIgnoreCase("unqualified") || y.equalsIgnoreCase("Element")) {

                    } else {
                        //akid ana m3mltsh kda mn far3' bs 3shn life is not that easy f howa bytb3li datatype b xs: el abliha f lazm ashel xs:
                        y = y.substring(3);
                        //odd iterator for columnName and even one for datatype
                        columnsName.add(x);
                        columnsName.add(y);
                    }
                }

            }

        } catch (FileNotFoundException e) {/// if file name is wrong or xml format is not well
            e.printStackTrace();
        }
        return columnsName;
    }

    public void CreateXSD(String tableName, List<String> element, List<String> dataType) throws FileNotFoundException, UnsupportedEncodingException {
        //creating xsd file
        PrintWriter writer = new PrintWriter("Data\\" + tableName + ".xsd", "UTF-8"); // bn3ml object tp write into the file
        // da w ana b7wl alsm el code 3shn mafish auto generation
        String x = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" \n xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">";
        //bytb3 w b3den yaznl line gedid
        writer.println(x);
        //tany satr bktb table name
        x = "<xs:element name=\"" + tableName + "\">";
        writer.println(x);
        //baktb ma benhom \n 3shn ynzl satr gedid
        x = "<xs:complexType> \n <xs:sequence> \n <xs:element name=\"Element\" maxOccurs=\"unbounded\"> \n <xs:complexType> \n <xs:sequence> ";
        writer.println(x);
        //      //to write columns
        for (int i = 0; i < element.size(); i++) {
            if (dataType.get(i).equals("varchar")) {
                dataType.set(i, "string");
            }
            x = "<xs:element type=\"xs:" + dataType.get(i) + "\" name=\"" + element.get(i) + "\"/>";
            writer.println(x);
        }
        x = "</xs:sequence> \n </xs:complexType> \n </xs:element> \n </xs:sequence> \n </xs:complexType> \n </xs:element> \n </xs:schema> ";
        writer.println(x);
        writer.close(); //to close the file
    }
    
   public boolean validation (String filename) throws FileNotFoundException, XMLStreamException, SAXException, IOException{
       
       try{
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream("Data\\"+filename+".xml")); 
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File("Data\\"+filename+".xsd"));

        javax.xml.validation.Validator validator =  schema.newValidator();
        validator.validate(new StAXSource(reader));

        //no exception thrown, so valid
        System.out.println("Document is valid");
            //*/
        return true;
       }
       catch (Exception ex){
             System.out.println("Document is invalid");
      
           return false;
           
       }
       
   } 
    

}

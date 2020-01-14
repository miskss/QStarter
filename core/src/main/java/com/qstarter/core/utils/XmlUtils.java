package com.qstarter.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author peter
 * date: 2019-12-04 15:33
 **/
@Slf4j
public class XmlUtils {

    public static Map<String, String> convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap<String, String> map = new HashMap<>();
        //API to obtain DOM Document instance
        DocumentBuilder builder;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

            map.put(doc.getFirstChild().getNodeName(), doc.getFirstChild().getTextContent());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return map;
    }
}

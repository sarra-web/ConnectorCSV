package com.keyrus.proxemconnector.connector.csv.configuration.service.xml;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectorXMLService {


    public static List<List<String>> XML(String filename,String tagName) throws ParserConfigurationException, IOException, SAXException {
        List<String> mm=tagNames(filename);
        List<List<String>> liste=new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Lecture du fichier XML
        Document document = builder.parse(filename);

        NodeList nodeList = document.getElementsByTagName(tagName);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                List<String> product = new ArrayList<>();
                for(int j = 0; j < mm.size(); j++){
                    // int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String name = element.getElementsByTagName(mm.get(j)).item(0).getTextContent();
                    // double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());
                    product.add(name);
                }
                liste.add(product);
            }
        }

        return liste;
    }

    static List<String> tagNames(String fileName){
        List<String> tagsList = new ArrayList<>();
        try {
            // Création du parseur DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Lecture du fichier XML
            Document document = builder.parse(fileName);

            // Récupération de la racine du document
            Element root = document.getDocumentElement();

            // Récupération de tous les éléments du document
            NodeList nodeList = document.getElementsByTagName("*");

            // Liste pour stocker les noms uniques des balises
            // Parcours des éléments et récupération des balises
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String tagName = element.getTagName();

                // Vérification si la balise est déjà présente dans la liste
                if (!tagsList.contains(tagName)) {
                    tagsList.add(tagName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> ll=tagsList.subList(2, tagsList.size());
        return ll;
    }
}

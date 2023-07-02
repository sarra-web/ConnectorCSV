package com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector;

import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
   static List<String> tagNames(){
        List<String> m=new ArrayList<>();
       try {
           File file = new File("file.xml"); // Chemin vers votre fichier XML

           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder = factory.newDocumentBuilder();
           Document document = builder.parse(file);

           // Obtenez la racine du document
           Element root = document.getDocumentElement();

           // Obtenez les nœuds enfants de l'élément racine
           NodeList childNodes = root.getChildNodes();

           // Parcourez les nœuds enfants et affichez les noms des balises
           for (int i = 0; i < childNodes.getLength(); i++) {
               Node childNode = childNodes.item(i);
               if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                   String tagName = childNode.getNodeName();
                   m.add(tagName);
                   System.out.println("Tag Name: " + tagName);
               }
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
       return m;

    }

    public static void main(String[] args){
        List<String> ll=tagNames();
        System.out.println(ll);

    }
}
@Data
class Product {

    private int id;

    private String name;

    private double price;

    // Getters and setters
}
@RestController
class ProductController {
    @GetMapping("/products")
    public List<Product> getProducts() throws ParserConfigurationException, IOException, SAXException {
        List<Product> products = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ClassPathResource resource = new ClassPathResource("file.xml"); // Chemin vers votre fichier XML
        Document document = builder.parse(resource.getInputStream());
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("product");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setPrice(price);

                products.add(product);
            }
        }

        return products;
    }
}



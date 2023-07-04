package com.keyrus.proxemconnector.connector.csv.configuration.service.xml;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Test {

    public static List<List<String>> XML(String filename) throws ParserConfigurationException, IOException, SAXException{
        List<String> mm=tagNames(filename);
        List<List<String>> liste=new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ClassPathResource resource = new ClassPathResource(filename); // Chemin vers votre fichier XML
        Document document = builder.parse(resource.getInputStream());
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("product");

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
        /*
        List<String> m=new ArrayList<>();
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

           // Ensemble (Set) pour stocker les noms uniques des balises


           // Parcours des éléments et récupération des balises
           for (int i = 0; i < nodeList.getLength(); i++) {
               System.out.println(i);
               Element element = (Element) nodeList.item(i);
               String tagName = element.getTagName();
               m.add(tagName);
           }

           // Affichage de la liste des balises


       } catch (Exception e) {
           e.printStackTrace();
       }

       return m;*/
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
   public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        System.out.println(tagNames("file.xml"));
        System.out.println(XML("file.xml"));

    }

 /*   @GetMapping("/products")
    public ResponseEntity<List<List<String>>> f(@RequestBody String name) throws ParserConfigurationException, IOException, SAXException {

        return  new ResponseEntity<>(XML("file.xml"), OK);

    }*/
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



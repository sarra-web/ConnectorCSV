package com.keyrus.proxemconnector.connector.csv.configuration.service.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dao.ConnectorXMLDAO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorXML;
import com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;
import com.keyrus.proxemconnector.connector.csv.configuration.service.log.Logging;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.keyrus.proxemconnector.connector.csv.configuration.rest.router.ConnectorJDBCRestRouter.countOccurrences;

@Slf4j
public final class ConnectorXMLService {
    private static final String BASE_POST_URL = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
    private static final String USER_URL= "http://localhost:8082/api/auth";
    private static final String PROJECT_URL= "http://localhost:8080/project";



    private static ConnectorXMLService instance = null;

    public static ConnectorXMLService instance(
            final com.keyrus.proxemconnector.connector.csv.configuration.repository.xmlConnector.XMLConnectorRepository XMLConnectorRepository
    ) {
        if (Objects.isNull(instance))
            instance =
                    new ConnectorXMLService(
                            XMLConnectorRepository
                    );
        return instance;
    }

    private final XMLConnectorRepository XMLConnectorRepository;

    private ConnectorXMLService(
            final XMLConnectorRepository XMLConnectorRepository
    ) {
        this.XMLConnectorRepository = XMLConnectorRepository;
    }

    public Either<Error, ConnectorXML> create(
            final ConnectorXML connectorXML
    ) {
        return
                this.XMLConnectorRepository
                        .create(
                                connectorXML
                        )
                        .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);
    }


    public Either<Error, ConnectorXML> update(
            final ConnectorXML connectorXML
    ) {
        return
                this.XMLConnectorRepository
                        .update(
                                connectorXML
                        )
                        .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);
    }

    public Either<Error, ConnectorXML> delete(
            final String id
    ) {
        return
                this.XMLConnectorRepository
                        .delete(
                                id
                        )
                        .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorXML>> findAll() {
        return this.XMLConnectorRepository
                .findAll()
                .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);

    }
    public Either<Error, ConnectorXML> findOneByName(String name) {
        return this.XMLConnectorRepository
                .findOneByName(
                        name
                )
                .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);
    }
    public Either<Error, ConnectorXML> findOneById(String id) {
        return this.XMLConnectorRepository
                .findOneById(
                        id
                )
                .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);
    }

    public Either<Error, Collection<ConnectorXML>> findManyByNameContainsIgnoreCase(String name) {
        return this.XMLConnectorRepository
                .findManyByNameContainsIgnoreCase(name)
                .mapLeft(ConnectorXMLService::repositoryErrorToServiceError);

    }
    public Page<ConnectorXMLDAO> findAll(int page, int size){
        log.info("Fetching for page {} of size {}",page,size);
        return XMLConnectorRepository.findAll(PageRequest.of(page,size));
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
        List<String> ll=tagsList.subList(1, tagsList.size());
        return ll;
    }

    public static List<List<String>> ReadXML(ConnectorXMLDTO conn) throws ParserConfigurationException, IOException, SAXException {
        List<String> aa=tagNames("uploads/"+conn.path());
        System.out.println("aaaaaaaaa"+aa);
        String tagName= aa.get(0);
        System.out.println("bbbbb"+tagName);
        List<String> mm=aa.subList(1, aa.size());
        System.out.println("cccc"+mm);
        List<List<String>> liste=new ArrayList<>();
        liste.add(mm);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Lecture du fichier XML
        Document document = builder.parse("uploads/"+conn.path());

        NodeList nodeList = document.getElementsByTagName(tagName);

        for (int i = 0; i < 5/*nodeList.getLength()*/; i++) {
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



    public static String generateRecordID(int position, String fileName) {
        return String.format("%s_%s_%d",  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), fileName, position);
    }

    public static   List<ProxemDto> XMLDataToJSON(final ConnectorXMLDTO config) throws ParserConfigurationException, IOException, SAXException {
        List<ProxemDto> dataList = new ArrayList<>();
     /******************************************************************************/
        List<List<String>> xmlLignes = ReadXML(config);
        List<List<String>> documents=xmlLignes.subList(1, xmlLignes.size());
        System.out.println("ddddd"+documents);
        int position = 0;
        for (List<String> values:xmlLignes) {
            ProxemDto data = new ProxemDto();
            position++;//pp les lignes
            System.out.println("projName"+config.projectName());
            data.setCorpusId(ConnectorCSVService.getProjectByName(config.projectName()).proxemToken()/*"a0e04a5f-ab7c-4b0e-97be-af263a61ba49"*//*config.getProject().getProjectName() ou project id nom doit etre unique*/);

            List<FieldDTO> l =  config.fields().stream().filter(field1 -> field1.fieldType().toString()=="Identifier").collect(Collectors.toList());
            if ((l.isEmpty() )) {
                String recordId = generateRecordID(position, config.path());
                data.setExternalId(recordId);
            } else {
                data.setExternalId(position+"_"+ values.get(l.get(0).position() - 1));//pour garantir l'unicité car les donne provenant des fichier j'ai pas controle sur eux
            }
            List<FieldDTO> l2 = config.fields().stream().filter(field1 -> field1.fieldType().toString()=="Date").collect(Collectors.toList());
            if (l2.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                data.setDocUtcDate(LocalDateTime.now().toString());
            } else {

                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                // data.setDocUtcDate(format2.parse(values[l2.get(0).position()-1]).toString());
                data.setDocUtcDate(values.get(l2.get(0).position() - 1));
            }
            Collection<Meta> metasList = new ArrayList<>();


            List<FieldDTO> l22= config.fields().stream().filter(field1 -> field1.included()==true
            ).filter(field1 -> field1.fieldType().toString()=="Meta").toList();
            if(!l22.isEmpty()) {
                l22.forEach(x -> {
                    Meta meta = new Meta();
                    meta.setName(x.meta());
                    meta.setValue(values.get(x.position() - 1));
                    metasList.add(meta);
                });
            }
            data.setMetas(metasList);
            List<TextPart> textPartsList = new ArrayList<>();

            TextPart titlePart = new TextPart();
            titlePart.setName("title");
            List<FieldDTO> l3=config.fields().stream().filter(field1 -> field1.fieldType().toString()=="Title").collect(Collectors.toList());

            if (!l3.isEmpty()){

                String value = values.get(l3.get(0).position() - 1);
                System.out.println("l333333333"+value);
                titlePart.setContent(value);
                textPartsList.add(titlePart);}
            TextPart bodyPart = new TextPart();
            config.fields().stream().filter(field1 -> field1.fieldType().toString()=="Text").collect(Collectors.toList()).forEach(x -> {

                bodyPart.setName("body");
                if(bodyPart.getContent()!=null){
                    bodyPart.setContent(bodyPart.getContent().toString()+ " ;" + values.get(x.position() - 1));}
                else{
                    bodyPart.setContent(values.get(x.position() - 1));}
                //concatenation of text fields otherwise each one will be considered as a ProxemDto
            });
            textPartsList.add(bodyPart);
            data.setTextParts(textPartsList);
            dataList.add(data);
        }

     return dataList;
    }

    public static ResponseEntity<String> pushToProxem(ConnectorXMLDTO connectorCSVDAO) throws ParserConfigurationException, IOException, SAXException {

        List<ProxemDto> proxemDtos = XMLDataToJSON(connectorCSVDAO);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/"+new ConnectorXMLDAO(connectorCSVDAO.toConfiguration().get()).project().getProxemToken()+"/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String mail= UserServiceConnector.getUserByName(connectorCSVDAO.userName()).get().getEmail() /*"mehdi.khayati@keyrus.com"*/;
        String userToken= UserServiceConnector.getUserByName(connectorCSVDAO.userName()).get().getUserToken()  /*"63cdd92e-adb4-42fe-a655-8e54aeb0653f"*/;

        headers.add("Authorization", "ApiKey "+mail+":"+userToken);
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response =   restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        if(response.getStatusCode().toString().startsWith("200")){
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),countOccurrences(response.getBody().toString(), "\"UpsertSuccessful\":true")+" docs pushed",connectorCSVDAO.userName());
            System.out.println("response body"+response.getBody().toString());//count appearence of "UpsertSuccessful":true
        }
        else{
            Logging.putInCSV(LocalDateTime.now().toString(),"/pushToProxem","PUT",response.getStatusCode().toString(),"no docs pushed",connectorCSVDAO.userName());
        }
        return response;

    }
    private static Error repositoryErrorToServiceError(
            final XMLConnectorRepository.Error repositoryError
    ) {
        if (repositoryError instanceof XMLConnectorRepository.Error.IO io)
            return new Error.IO(io.message());
        if (repositoryError instanceof XMLConnectorRepository.Error.AlreadyExist)
            return new Error.AlreadyExist();
        if (repositoryError instanceof XMLConnectorRepository.Error.NotFound)
            return new Error.NotFound();
        throw new IllegalStateException("repository error not mapped to service error");
    }
//////////////////////////getProjectByName///////////////////////////////////

    public static ProjectDTO getProjectByName(String id ){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<ProjectDTO> result= restTemplate.getForEntity(PROJECT_URL+"/"+id,ProjectDTO.class);
        return  result.getBody();
    }

/////////////////////////////getProjectByName///////////////////////////////////
    public sealed interface Error {

        default String message() {
            return this.getClass().getCanonicalName();
        }

        record IO(
                String message
        ) implements Error {
        }

        record AlreadyExist() implements Error {
        }

        record NotFound() implements Error {
        }
    }
}

package com.keyrus.proxemconnector.connector.csv.configuration.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.Meta;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.TextPart;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.Type;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Connector;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.keyrus.proxemconnector.connector.csv.configuration.enumerations.Type.*;


@Service
public class ConnecteurCSVService  {
    public static int numRow(String csvFilePath, String stringSeparator) {
        int numRow = 0;
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(csvFilePath);
        } catch (FileNotFoundException e){
            //System.out.println("File doesn't exist put a valid path");
            System.out.println(e.getMessage());

        }
        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    numRow++;}
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        return numRow;

    }

    public static int numCol(String csvFilePath, String stringSeparator) {
        int numCol = 0;
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(csvFilePath);
        } catch (FileNotFoundException e){
            //System.out.println("File doesn't exist put a valid path");
            System.out.println(e.getMessage());

        }
        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            line = br.readLine();
            String[] valuesEntete = line.split(stringSeparator);

            numCol = valuesEntete.length;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return numCol;

    }

    public static String generateRecordID(int position, String fileName) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int recordIndex = position;
        return String.format("%s_%s_%d", date, fileName, recordIndex);
    }


    public static List<String> extractHeader(String stringSeparator, Boolean hasHeader, String csvFilePath) {
        if (hasHeader == false) {
            System.out.println("there is no header");
            return null;
        } else {
            List<String> header = new ArrayList<>();
            /// I want to put first in a list the header if there is a header according to my variable hasHeader
            if (hasHeader == true) {
                System.out.println("The file contain header");
                try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
                    String line;
                    line = br.readLine();
                    String[] valuesEntete = line.split(stringSeparator);
                    for (int i = 0; i < numCol(csvFilePath, stringSeparator); i++) {
                        header.add(valuesEntete[i]);
                    }
                    System.out.println("The header is: " + header);
                } catch (Exception e) {
                }
            }
            return header;
        }
    }
public static void main(String[] args) throws JSONException {
    UUID uuid= UUID.randomUUID();
    List<Type> list = List.of(titre,identifiant,texte,meta);
    //new Connector(4548,"",";","\\","\"",true,"csv","email.csv");
    final var id = UUID.randomUUID().toString();
    final var connnectorCSV =
            Connector.Builder
                    .builder()
                    .withId(id)
                    .withName(UUID.randomUUID().toString())
                    .withSeparator(";")
                    .withEncoding(StandardCharsets.UTF_8.name())
                    .withFolderToScan("email.csv")
                    .withArchiveFolder(UUID.randomUUID().toString())
                    .withFailedRecordsFolder(UUID.randomUUID().toString())
                    .withContainsHeaders(new Random().nextBoolean())
                    .withHeaders(
                            IntStream.iterate(1, it -> it + 1)
                                    .limit(4)
                                    .mapToObj(it ->
                                            Field.of(
                                                            UUID.randomUUID().toString(),
                                                            id,
                                                            UUID.randomUUID().toString(),
                                                            it,
                                                            UUID.randomUUID().toString(),
                                                            true,
                                                            false,list.get(it-1)
                                                    )
                                                    .get()
                                    )
                                    .collect(Collectors.toUnmodifiableSet())
                    )
                    .build()
                    .get();


    List<ProxemDto> proxemDtos = CSVDataToJSON(connnectorCSV);
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayNode metas1 = objectMapper.createArrayNode();
    ObjectNode meta1 = objectMapper.createObjectNode();

    ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);

// Créez un objet JSON final pour encapsuler le tableau de données
    ObjectNode finalJson = objectMapper.createObjectNode();
    //finalJson.set("data", jsonArray);

// Convertissez l'objet JSON final en chaîne de caractères
  //  String jsonString = finalJson.toString();

// Utilisez la chaîne de caractères JSON selon vos besoins
    System.out.println( jsonArray);

}
  public static   List<ProxemDto> CSVDataToJSON(Connector config)  {
      List<ProxemDto> dataList = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(config.folderToScan()))) {

          int position = 0;
          String line;
          if (config.containsHeaders()){
              br.readLine();
          }
          while ((line = br.readLine()) != null) {

              String[] values = parseLine(line,config.separator().charAt(0),"\"".charAt(0),"\\".charAt(0));
              ProxemDto data = new ProxemDto();
              position++;
              data.setCorpusId("a0e04a5f-ab7c-4b0e-97be-af263a61ba49"/*config.getProject().getProjectName()*/);
              List<Field> l =  config.fields().stream().filter(field1 -> field1.type()==identifiant).collect(Collectors.toList());
              if ((l.isEmpty() )) {
                  String recordId = generateRecordID(position, config.folderToScan());
                  data.setExternalId(recordId);
              } else {
                  data.setExternalId(values[l.get(0).position()]);
              }
              List<Field> l2 = config.fields().stream().filter(field1 -> field1.type()==date).collect(Collectors.toList());
              if (l2.isEmpty()) {
                  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                  data.setDocUtcDate("2023-01-04T15:00:00Z");
              } else {

                  SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                  System.out.println(values[l2.get(0).position()]);
                  data.setDocUtcDate(format2.parse(values[l2.get(0).position()]).toString());
              }
              Collection<Meta> metasList = new ArrayList<>();
              config.fields().stream().filter(field1 -> field1.type()==date).forEach(x -> {
                  Meta meta = new Meta();
                  meta.setName(x.name());
                  meta.setValue(values[x.position()]);
                  metasList.add(meta);
              });
              data.setMetas(metasList);
              List<TextPart> textPartsList = new ArrayList<>();

              TextPart titlePart = new TextPart();
              titlePart.setName("title");
              titlePart.setContent(values[config.fields().stream().filter(field1 -> field1.type()==titre).collect(Collectors.toList()).get(0).position()]);
              textPartsList.add(titlePart);
              TextPart bodyPart = new TextPart();
              config.fields().stream().filter(field1 -> field1.type()==texte).collect(Collectors.toList()).forEach(x -> {

                  bodyPart.setName("body");
                  bodyPart.setContent(bodyPart.getContent() + " ;" + values[x.position()]);
                  //concatenation of text fields otherwise each one will be considered as a ProxemDto
              });
              textPartsList.add(bodyPart);
              data.setTextParts(textPartsList);
              dataList.add(data);
          }
      }
      catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          throw new RuntimeException(e);
      }
      return dataList;
    }
    public static String[] parseLine(String line, char separator, char quote, char escape) {
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        String[] tokens = new String[0];
        if (line == null || line.isEmpty()) {
            return tokens;
        }
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == escape && i + 1 < line.length()) {
                sb.append(line.charAt(++i));
            } else if (c == quote) {
                inQuotes = !inQuotes;
            } else if (c == separator && !inQuotes) {
                tokens = addToken(tokens, sb);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        tokens = addToken(tokens, sb);
        return tokens;
    }

    private static String[] addToken(String[] tokens, StringBuilder sb) {
        String[] newTokens = new String[tokens.length + 1];
        System.arraycopy(tokens, 0, newTokens, 0, tokens.length);
        newTokens[tokens.length] = sb.toString();
        return newTokens;
    }

}

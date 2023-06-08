package com.keyrus.proxemconnector.connector.csv.configuration.service;


import com.keyrus.proxemconnector.connector.csv.configuration.dto.*;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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
        return String.format("%s_%s_%d",  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), fileName, position);
    }


    public static Either <String,List<String>> extractHeader(String stringSeparator, Boolean hasHeader, String csvFilePath) throws FileNotFoundException {
        return hasHeader ? Either.right(getHeader(csvFilePath,true, stringSeparator))
                : Either.left("There is no header");
    }

    public static List<String> getHeader(String stringSeparator, Boolean hasHeader, String csvFilePath) throws FileNotFoundException {
        List<String> header = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine();
            if (line != null) {
                String[] valuesEntete = line.split(stringSeparator);
                for (int i = 0; i < valuesEntete.length; i++) {
                    header.add(valuesEntete[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return header;

    }



  public static   List<ProxemDto> CSVDataToJSON(final ConnectorDTO config)  {
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
              data.setCorpusId("a0e04a5f-ab7c-4b0e-97be-af263a61ba49"/*config.getProject().getProjectName() ou project id nom doit etre unique*/);

              List<FieldDTO> l =  config.headers().stream().filter(field1 -> field1.field_type().startsWith("id")).collect(Collectors.toList());
              if ((l.isEmpty() )) {
                  String recordId = generateRecordID(position, config.folderToScan());
                  data.setExternalId(recordId);
              } else {
                  data.setExternalId(values[l.get(0).position()-1]);
              }
              List<FieldDTO> l2 = config.headers().stream().filter(field1 -> field1.field_type().startsWith("da")).collect(Collectors.toList());
              if (l2.isEmpty()) {
                  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                  data.setDocUtcDate("2023-01-04T15:00:00Z");
              } else {

                  SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                  data.setDocUtcDate(format2.parse(values[l2.get(0).position()-1]).toString());
              }
              Collection<Meta> metasList = new ArrayList<>();
              config.headers().stream().filter(field1 -> field1.field_type().startsWith("m")).forEach(x -> {
                  Meta meta = new Meta();
                  meta.setName(x.name());
                  meta.setValue(values[x.position()-1]);
                  metasList.add(meta);
              });
              data.setMetas(metasList);
              List<TextPart> textPartsList = new ArrayList<>();

              TextPart titlePart = new TextPart();
              titlePart.setName("title");
              String value =values[config.headers().stream().filter(field1 -> field1.field_type().startsWith("t")).collect(Collectors.toList()).get(0).position()-1];
              titlePart.setContent(value);
              textPartsList.add(titlePart);
              TextPart bodyPart = new TextPart();
              config.headers().stream().filter(field1 -> field1.field_type().startsWith("ti")).collect(Collectors.toList()).forEach(x -> {

                  bodyPart.setName("body");
                  if(bodyPart.getContent()!=null){
                  bodyPart.setContent(bodyPart.getContent().toString()+ " ;" + values[x.position()-1]);}
                  else{
                      bodyPart.setContent(values[x.position()-1]);}
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

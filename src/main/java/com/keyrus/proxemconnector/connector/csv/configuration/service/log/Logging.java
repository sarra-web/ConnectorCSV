package com.keyrus.proxemconnector.connector.csv.configuration.service.log;


/* @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

 MyCustomHttpRequestWrapper requestWrapper = new MyCustomHttpRequestWrapper((HttpServletRequest) request);
       List<String> list=new ArrayList<>();
        String uri = requestWrapper.getRequestURI();
        log.info("Requeust URI: {}", uri);
        list.add(LocalDateTime.now().toString());
        list.add(uri);
//log.info //log.trace  .debug
        log.info("Requeust Method: {}", requestWrapper.getMethod());
        list.add(requestWrapper.getMethod());
        String requestData = new String(requestWrapper.getByteArray()).replaceAll("\n", " ");
        log.info("Requeust Body: {}", requestData);
        list.add(requestData);
        MyCustomHttpResponseWrapper responseWrapper = new MyCustomHttpResponseWrapper((HttpServletResponse) response);

       chain.doFilter(requestWrapper, responseWrapper);
        log.info("Response status -{}", responseWrapper.getStatus());
        list.add(String.valueOf(responseWrapper.getStatus()));
        log.info("Response body -{}",new String (responseWrapper.getBaos().toByteArray()));
        list.add(new String (responseWrapper.getBaos().toByteArray()));


        listLogs.add(list);

    }
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
@Data
@AllArgsConstructor
@Component
@Slf4j
public  final class Logging {
   /* static List<String> l= List.of("Date","Requeust URI","Requeust Method"
            ,"Requeust Body","Response status","Response Body ");
    public static  List<List<String>> listLogs =List.of(l);*/

    public static void putInCSV(String madate, String uri, String methode,String status,String info){
     /*   try (FileWriter writer = new FileWriter("myLog.csv")) {
            // Écrire les en-têtes des champs dans le fichier CSV
            // Écrire les données dans le fichier CSV

                writer.append(madate);
                writer.append(",");
                writer.append(myClass);
                writer.append(",");
                writer.append(uri);
                writer.append(",");
                writer.append(methode);


            writer.append("\n");

            writer.flush();

        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier CSV : " + e.getMessage());
        }*/
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("myLog.csv", true))) {
            StringBuilder sb = new StringBuilder();

            // Construire la ligne CSV à ajouter
            sb.append(madate);
            sb.append(",");
            sb.append(uri);
            sb.append(",");
            sb.append(methode);
            sb.append(",");
            sb.append(status);
            sb.append(",");
            sb.append(info);



            sb.append("\n");

            // Écrire la ligne dans le fichier
            writer.write(sb.toString());
            writer.flush();


        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier CSV : " + e.getMessage());
        }
    }
}

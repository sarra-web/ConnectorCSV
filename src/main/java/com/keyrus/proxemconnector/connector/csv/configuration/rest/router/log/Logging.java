package com.keyrus.proxemconnector.connector.csv.configuration.rest.router.log;


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

import java.util.List;
public  class Logging {
    static List<String> l= List.of("Date","Requeust URI","Requeust Method"
            ,"Requeust Body","Response status","Response Body ");
    public static  List<List<String>> listLogs =List.of(l);
    public static void main(String[] args){
        System.out.println(listLogs);
        System.out.println("hello");
    }
}

package com.keyrus.proxemconnector.connector.csv.configuration.rest.router.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
//@Order(1)
public class RequestResponseLoggingFilter implements Filter {

    //password
    //ssn, pan, aadhar
    //personal  => sensitive information so w need  to mask that
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);



    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        List<List<String>> listLogs=new ArrayList<>();
 MyCustomHttpRequestWrapper requestWrapper = new MyCustomHttpRequestWrapper((HttpServletRequest) request);
      String uri = requestWrapper.getRequestURI();
        log.info("Request URI: {}", uri);
//log.info //log.trace  .debug
        log.info("Request Method: {}", requestWrapper.getMethod());
        String requestData = new String(requestWrapper.getByteArray()).replaceAll("\n", " ");
        log.info("Request Body: {}", requestData);
        MyCustomHttpResponseWrapper responseWrapper = new MyCustomHttpResponseWrapper((HttpServletResponse) response);
        chain.doFilter(requestWrapper, responseWrapper);
        log.info("Response status -{}", responseWrapper.getStatus());
        log.info("Response body -{}",new String (responseWrapper.getBaos().toByteArray()));
        log.info(listLogs.toString());

        String a="";
        if(responseWrapper.getStatus()==200){
            a="status:_200_OK";
        }
        if(responseWrapper.getStatus()==500){
            a="status:500_Internal Server Error";
        }
        else{

          a=  "status_:"+String.valueOf(responseWrapper.getStatus());
        }
        Logging.putInCSV(LocalDateTime.now().toString(), uri,requestWrapper.getMethod(),a);
    }


    private class MyCustomHttpRequestWrapper extends HttpServletRequestWrapper {

        private byte[] byteArray;

        public MyCustomHttpRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                byteArray = IOUtils.toByteArray(request.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("Issue while reading request stream");
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {

            return new MyDelegatingServletInputStream(new ByteArrayInputStream(byteArray));

        }

        public byte[] getByteArray() {
            return byteArray;
        }
    }

    private class MyCustomHttpResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        private PrintStream printStream = new PrintStream(baos);

        public ByteArrayOutputStream getBaos() {
            return baos;
        }

        public MyCustomHttpResponseWrapper(HttpServletResponse response) {
            super(response);
        }
          @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new MyDelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), printStream));
        }

       @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new TeeOutputStream(super.getOutputStream(), printStream));
        }
    }

}
/*public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        MyCustomHttpRequestWrapper requestWrapper = new MyCustomHttpRequestWrapper((HttpServletRequest) request);

        String uri = requestWrapper.getRequestURI();
        log.info("Requeust URI: {}", uri);

        log.info("Requeust Method: {}", requestWrapper.getMethod());
        String requestData = new String(requestWrapper.getByteArray()).replaceAll("\n", " ");
        log.info("Requeust Body: {}", requestData);
       if("/v1/addProduct".equalsIgnoreCase(uri)){
            ConnectorCSV connector = objectMapper.readValue(requestData, ConnectorCSV.class);

            product.setCurrency("****");

            requestData = objectMapper.writeValueAsString(product);
        }

        log.info("Requeust Body: {}", requestData);

        MyCustomHttpResponseWrapper responseWrapper = new MyCustomHttpResponseWrapper((HttpServletResponse) response);

        chain.doFilter(requestWrapper, responseWrapper);
        log.info("Response status -{}", responseWrapper.getStatus());
        log.info("Response body -{}",new String (responseWrapper.getBaos().toByteArray()));
        String responseResult = new String(responseWrapper.getBaos().toByteArray());
        if("/v1/addProduct".equalsIgnoreCase(uri)){
            ProductResponse productResponse = objectMapper.readValue(responseResult, ProductResponse.class);

//            product.setCurrency("****");

            responseResult = objectMapper.writeValueAsString(productResponse);
        }
        log.info("Response status - {}", responseWrapper.getStatus());
        log.info("Response Body - {}",new String (responseWrapper.getBaos().toByteArray()) );
    }*/
///////////////////
  /*  private static final ThreadLocal<ServletOutputStream> loggedData = new ThreadLocal<ServletOutputStream>();
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.info("Request URI:{}",httpRequest.getRequestURL());
        log.info("Request Method:{}",httpRequest.getMethod());
        log.info("Request body:{}",new String (IOUtils.toByteArray(httpRequest.getInputStream())));
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log the request
        logRequest(httpRequest);
        // Wrap the response to capture the response body
        AddDefaultCharsetFilter.ResponseWrapper responseWrapper = new AddDefaultCharsetFilter.ResponseWrapper(httpResponse," UTF-8");


        chain.doFilter(request, responseWrapper);

        // Log the response
      logResponse(responseWrapper);
        loggedData.set(responseWrapper.getOutputStream());

    }

    private void logRequest(HttpServletRequest httpRequest) {
        // Log request details (URL, method, headers, etc.)
        logger.info("Request - URL: {}, Method: {}, Headers: {}", httpRequest.getRequestURL(),
                httpRequest.getMethod(), httpRequest.getHeaderNames());
        // Optionally, log the request body
        logRequestBody(httpRequest);
    }

    private void logRequestBody(HttpServletRequest httpRequest) {
        try {
            String requestBody = StreamUtils.copyToString(httpRequest.getInputStream(), Charset.defaultCharset());
            logger.info("Request Body: {}", requestBody);
        } catch (IOException e) {
            logger.error("Error reading request body", e);
        }
    }

    private void logResponse(AddDefaultCharsetFilter.ResponseWrapper responseWrapper) {
        // Log response details (status code, headers, etc.)
        logger.info("Response - Status: {}, Headers: {}", responseWrapper.getStatus(),
                responseWrapper.getHeaderNames());
        // Optionally, log the response body
        logResponseBody(responseWrapper);
    }

    private void logResponseBody(AddDefaultCharsetFilter.ResponseWrapper responseWrapper) {
        String responseBody = String.valueOf(responseWrapper.getResponse());
        logger.info("Response Body: {}", responseBody);
    }
    public static ServletOutputStream getLoggedData() {
        return loggedData.get();
    }
}

class RequestWrapper extends HttpServletRequestWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    public RequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            byteArray=IOUtils.toByteArray(request.getInputStream());

        }catch (IOException e){
            throw new RuntimeException("Issue reading request stream");

        }
        byteArrayOutputStream = new ByteArrayOutputStream();
    }
}*/
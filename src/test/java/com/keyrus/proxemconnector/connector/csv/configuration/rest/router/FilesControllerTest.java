package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.message.ResponseMessage;
import com.keyrus.proxemconnector.connector.csv.configuration.service.FilesStorageService;
import com.keyrus.proxemconnector.initializer.PostgreSQLInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgreSQLInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilesControllerTest {
    private final FilesStorageService filesStorageService;
    private final int port;
    private final TestRestTemplate restTemplate;
    private final String baseUrl;
    private final String errorHeader;

    @Autowired
    FilesControllerTest(

            FilesStorageService filesStorageService, @Value(value = "${local.server.port}") final int port,
            final TestRestTemplate restTemplate,
            @Value("${connectors.rest.error-header:error}") final String errorHeader
    ) {
        this.filesStorageService = filesStorageService;

        this.port = port;
        this.restTemplate = restTemplate;
        baseUrl = "http://localhost:" + this.port + "/configuration";
        this.errorHeader = errorHeader;
    }


    @BeforeAll
    void beforeAll() {
        this.filesStorageService.deleteAll();
    }

    @BeforeEach
    void beforeEach() {
        this.filesStorageService.deleteAll();
    }

    @AfterEach
    void afterEach() {
        this.filesStorageService.deleteAll();
    }

    @AfterAll
    void afterAll() {
        this.filesStorageService.deleteAll();
    }

    @Test
    @DisplayName("uploadFile_test ")
    void uploadFile_test() {
        this.filesStorageService.init();

        MultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "header1,header2\nvalue1,value2".getBytes()
        );

        this.filesStorageService.save(file);


        final var result =
                this.restTemplate.postForEntity(
                        this.baseUrl+"/upload",
                        file,
                        ResponseMessage.class
                );

         Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());

    }

    @Test
    void getListFiles_test() {
    }

    @Test
    void getFile_test() {
    }

    @Test
    void deleteFile_test() {
    }

    @Test
    void readCsv_test() {
    }
}
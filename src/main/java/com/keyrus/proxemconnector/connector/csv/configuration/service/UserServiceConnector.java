package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class UserServiceConnector {
       private static  RestTemplate restTemplate = null;
       private static UserServiceConnector instance = null;

    public static UserServiceConnector instance(

    ) {
        if (Objects.isNull(instance))
            instance =
                    new UserServiceConnector(

                    );
        return instance;
    }

    public UserServiceConnector() {
        //this.restTemplate = restTemplate;
    }

 //   @HystrixCommand(fallbackMethod = "getDefaultProductById")
        public static Optional<UserDto> getUserById(Long userId) {
        if (restTemplate==null){restTemplate=new RestTemplate();}
            ResponseEntity<UserDto> userResponse = restTemplate
                    .getForEntity("http://localhost:8082/api/auth/{id}",
                            UserDto.class,
                            userId);
            if (userResponse.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(userResponse.getBody());
            } else {
                log.error("Unable to get user with ID: " + userId
                        + ", StatusCode: " + userResponse.getStatusCode());
                return Optional.empty();
            }
        }
    public static Optional<UserDto> getUserByName(String name) {
        if (restTemplate==null){restTemplate=new RestTemplate();}
        ResponseEntity<UserDto> userResponse = restTemplate
                .getForEntity("http://localhost:8082/api/auth/findByName/{name}",
                        UserDto.class,
                        name);
        if (userResponse.getStatusCode() == HttpStatus.OK) {
            return Optional.ofNullable(userResponse.getBody());
        } else {
            log.error("Unable to get user with ID: " + name
                    + ", StatusCode: " + userResponse.getStatusCode());
            return Optional.empty();
        }
    }

        Optional<UserDto> getDefaultProductById(Long userId) {
                log.info("Returning default UserById for product Id: " + userId);
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            userDto.setUsername("UNKNOWN");
            userDto.setEmail("UNKNOWN");
            userDto.setPassword("NONE");
            return Optional.ofNullable(userDto);
        }
    }


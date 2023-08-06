package com.keyrus.proxemconnector.connector.csv.configuration.rest.router;

import com.keyrus.proxemconnector.connector.csv.configuration.service.UserServiceConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserRestRouter {
    private final UserServiceConnector userServiceConnector;

    public UserRestRouter(UserServiceConnector userServiceConnector) {
        this.userServiceConnector = userServiceConnector;
    }

    @GetMapping("GetUserByUserId/{id}")
    public ResponseEntity<?> GetUserById(@PathVariable("id") final Long id){
        return ResponseEntity.ok(userServiceConnector.getUserById(id));
    }
    @GetMapping("GetUserByUserName/{name}")
    public ResponseEntity<?> GetUserByName(@PathVariable("name") final String name){
        return ResponseEntity.ok(userServiceConnector.getUserByName(name));
    }
}

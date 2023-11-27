package com.example.project;


import com.example.project.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.UserRegistration;

import java.util.ArrayList;

@NoArgsConstructor
@RestController
@RequestMapping(value = "/user", consumes = "application/json", produces = "application/json")
public class UserRegistrationController {

    UserRepository repo = new UserRepository("src/resources/users.csv");
    ObjectMapper ob = new ObjectMapper();



    public UserRegistrationController(UserRepository repo){
        this.repo = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createUser(@RequestBody UserRegistration userRequest) {
        String returnString = "";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ArrayList<String> error = userRequest.runTest();
        boolean validAddress = userRequest.validateAddress();
        if(error.size() == 0 && validAddress) {
            if(repo.findExistsByEmailAndUsername(userRequest.getEmail(), userRequest.getUsername())){
                returnString = "{\n\"error\" : \"Invalid request. Username and Email already taken.\"\n}";
            } else {
                System.out.println(userRequest);
                repo.create(userRequest);
                System.out.println(userRequest);
                try {
                    returnString = ob.writeValueAsString(userRequest);
                } catch (Exception e) {
                    System.out.println(e);
                }
                status = HttpStatus.CREATED;
            }
        } else {
            returnString = "{\n\"error\" : \"Invalid request.";
            if(error.size() > 0) {
                returnString += " Invalid required field:";
                for (String code : error) {
                    returnString += " \'" + code + "\'";
                }
                returnString += ".";
            } if(!validAddress){
                returnString += " Address not valid.";
            }
            returnString += "\"\n}";
        }
        // Implement user creation logic here
        return ResponseEntity.status(status)
                .body(returnString);
    }

    @GetMapping("/{user-id}")
    @ResponseStatus
    @ResponseBody
    public ResponseEntity<String> getUserById(@PathVariable("user-id") String userId) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "";
        try {
            long id = Long.parseLong(userId);
            UserRegistration user = repo.findById(id);
            if(user != null){
                message = ob.writeValueAsString(user);
                status = HttpStatus.OK;
            } else {
                message = "{\n\"error\":\"User not found.\"\n}";
            }
        } catch (Exception e){
            message = "{\n\"error\":\"Invalid ID format.\"\n}";
        }
        return ResponseEntity.status(status).body(message);
    }
}
package com.example.project.model;




import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;


import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistration {
    private Long id;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String email;
    private String username;
    // Add remaining fields here

    public UserRegistration(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = null;
    }

    public UserRegistration(String firstName, String lastName, String street, String city, String state, String zip, String country, String phone, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.street = street;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.id = null;
    }
    public UserRegistration(long id, String firstName, String lastName, String street, String city, String state, String zip, String country, String phone, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.street = street;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.id = id;
    }

    public UserRegistration(String street, String city, String state, String country, String zip){
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    public ArrayList<String> runTest(){
        ArrayList<String> error = new ArrayList<>();
        if(!validateFirstName(firstName)){
            error.add("firstName");
        }
        if(!validateLastName(lastName)){
            error.add("lastName");
        }
        if(!validateStreet()){
            error.add("street");
        }
        if(!validateCity()){
            error.add("city");
        }
        if(!validateState()){
            error.add("state");
        }
        if(!validateZip()){
            error.add("zip");
        }
        if(!validateCountry()){
            error.add("country");
        }
        if(!validatePhoneNumber(phone)){
            error.add("phone");
        }
        if(!validateEmailAddress(email)){
            error.add("firstName");
        }
        return error;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean validateFirstName(String firstName) {
        // Implement validation logic for first name
        // Should not be empty, and should have a max length of 256 characters
        // Allow alphabetic characters, spaces, hyphens, and apostrophes
        // Return true if valid, false otherwise
        return firstName != null && Pattern.matches("[-a-zA-Z' ]{1,256}", firstName);
    }

    public boolean validateLastName(String lastName) {
        // Implement validation logic for last name
        // Should not be empty, and should have a max length of 256 characters
        // Allow alphabetic characters, spaces, hyphens, and apostrophes
        // Return true if valid, false otherwise
        return lastName != null && Pattern.matches("[-a-zA-Z' ]{1,256}", lastName);
    }

    public boolean validateEmailAddress(String email) {
        // Implement validation logic for email address
        // Should match the username@domain.com format
        // Allow letters, numbers, dots ".", hyphens "-", hash "#," and underscores "_"
        // Return true if valid, false otherwise
        return email != null && Pattern.matches("[a-zA-Z.#_-]+@[a-zA-Z.#_-]+\\.com", email);
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        // Implement validation logic for phone number
        // Should consist of ten digits
        // Return true if valid, false otherwise
        return phoneNumber != null && Pattern.matches("\\d{3}-\\d{3}-\\d{4}", phoneNumber);
    }

    // Add  methods for address validation

    public boolean validateStreet(){
        return this.street != null && Pattern.matches("[a-zA-Z0-9 ]{1,256}", this.street);
    }
    public boolean validateCity(){
        return this.city != null && Pattern.matches("[a-zA-Z0-9 ]{1,256}", this.city);
    }
    public boolean validateState(){
        return this.state != null && Pattern.matches("[a-zA-Z0-9 ]{1,256}", this.state);
    }
    public boolean validateCountry(){
        return this.country != null && Pattern.matches("[a-zA-Z0-9]{1,256}", this.country);
    }
    public boolean validateZip(){
        return this.zip != null && Pattern.matches("[a-zA-Z0-9]{1,256}", this.zip);
    }

    public String toString(){
        return id + "," + firstName + "," + lastName + "," + street + "," + city + "," + state + "," + zip + "," + country + "," + phone + "," + email + "," + username;
    }

    public boolean validateAddress(){
        HttpResponse<String> response = null;
        boolean valid = false;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://addressvalidation.googleapis.com/v1:validateAddress?key="))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"address\" : {\"regionCode\" : \"" + country + "\", \"addressLines\" : [\"" + street + "\", \"" + city + ", " + state + ", " + zip + "\"]}}"))
                    .build();

            response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper om = new ObjectMapper();

            Object result = om.readValue(response.body(), Map.class).get("result");

            Map rMap = null;
            if(result instanceof Map) rMap = (Map) result;

            Object verdict = rMap.get("verdict");
            Map vMap = null;
            if(verdict instanceof Map) vMap = (Map) verdict;

            valid = !((String)vMap.get("validationGranularity")).equals("OTHER");



        } catch (Exception e){

        }

        return response != null && response.statusCode() == 200 && valid;
    }
}
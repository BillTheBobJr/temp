package com.example.project.Repository;

import com.example.project.model.UserRegistration;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;

@NoArgsConstructor
public class UserRepository {

    File file;
    HashMap<Long, UserRegistration> database;
    long nextKey = 1;

    public UserRepository(String databaseFile){
        database = new HashMap<>();
        file = new File(databaseFile);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            while(reader.ready()){
                nextKey += 1;
                String line = reader.readLine();
                String elements[] = line.split(",");
                database.put(Long.parseLong(elements[0]), new UserRegistration(Long.parseLong(elements[0]),elements[1],elements[2],elements[3],elements[4],elements[5],elements[6],elements[7],elements[8],elements[9],elements[10]));
            }

            reader.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public synchronized UserRegistration findByEmailAndUsername(String email, String username){
        for(Long key: database.keySet()){
            UserRegistration user= database.get(key);
            if(user.getEmail().equals(email) && user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public synchronized boolean findExistsByEmailAndUsername(String email, String username){
        return findByEmailAndUsername(email, username) != null;
    }


    public synchronized UserRegistration findById(Long id){
        return database.get(id);
    }

    public synchronized boolean findExistsById(Long id){
        return findById(id) != null;
    }

    public UserRegistration create(UserRegistration user){
        user.setId(nextKey++);
        database.put(user.getId(), user);
        this.save();
        return user;
    }

    public boolean save(){
        if(file == null) return false;
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.flush();
            fileWriter.write("id,firstName,lastName,street,city,state,zip,country,phone,email,username\n");
            for(Long key: database.keySet()){
                fileWriter.write(database.get(key) + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }


}

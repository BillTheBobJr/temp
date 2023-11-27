package com.example.project;

import com.example.project.model.UserRegistration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserRegistration {

   UserRegistration testUser = new UserRegistration();

   @Test
   public void testCorrectFirstName()
   {
      assertTrue(testUser.validateFirstName("Alex"));
   }

   @Test
   public void testIncorrectFirstName(){
      assertFalse(testUser.validateFirstName("XI12"));
   }

   @Test
   public void testCorrectLastName()
   {
      assertTrue(testUser.validateLastName("Chambers"));
   }

   @Test
   public void testIncorrectLastName(){
      assertFalse(testUser.validateLastName("Last Name Ya!"));
   }

   @Test
   public void testCorrectEmailAddress()
   {
      assertTrue(testUser.validateEmailAddress("valide_email@domain.com"));
   }

   @Test
   public void testIncorrectEmailAddress(){
      assertFalse(testUser.validateEmailAddress("thIS;is%wrong@iastate.com"));
   }

   @Test
   public void testCorrectPhoneNumber()
   {
      assertTrue(testUser.validatePhoneNumber("123-456-7890"));
   }

   @Test
   public void testIncorrectPhoneNumber(){
      assertFalse(testUser.validatePhoneNumber("123982123"));
   }
}

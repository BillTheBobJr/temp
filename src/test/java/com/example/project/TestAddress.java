package com.example.project;

import com.example.project.model.UserRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestAddress {

    UserRegistration testAddr = new UserRegistration();

    @BeforeEach
    public void initCorrectAddress(){
        testAddr.setStreet("Street");
        testAddr.setCity("Ames");
        testAddr.setState("Iowa");
        testAddr.setCountry("USA");
        testAddr.setZip("50010");
    }
    // Add tests for address validation
    @Test
    public void testCorrectStreet(){
        assertTrue(testAddr.validateStreet());
    }

    @Test
    public void testIncorrectStreet(){
        testAddr.setStreet("*".repeat(257));
        assertFalse(testAddr.validateStreet());
    }

    @Test
    public void testCorrectCity(){
        assertTrue(testAddr.validateCity());
    }

    @Test
    public void testIncorrectCity(){
        testAddr.setCity("*".repeat(257));
        assertFalse(testAddr.validateCity());
    }

    @Test
    public void testCorrectState(){
        assertTrue(testAddr.validateState());
    }

    @Test
    public void testIncorrectState(){
        testAddr.setState("*".repeat(257));
        assertFalse(testAddr.validateState());
    }

    @Test
    public void testCorrectCountry(){
        assertTrue(testAddr.validateCountry());
    }

    @Test
    public void testIncorrectCountry(){
        testAddr.setCountry("*".repeat(257));
        assertFalse(testAddr.validateCountry());
    }

    @Test
    public void testCorrectZip(){
        assertTrue(testAddr.validateZip());
    }

    @Test
    public void testIncorrectZip(){
        testAddr.setZip("*".repeat(257));
        assertFalse(testAddr.validateZip());
    }

    static Stream<Arguments> dataProvider(){
        return Stream.of(
                arguments(true, new UserRegistration("212 Beyer Ct", "Ames", "IA", "US", "50021")),
                arguments(false, new UserRegistration("ERR0R", "City", "ST@TE", "County", "Zip")),
                arguments(false, new UserRegistration("Street Address", "Ames", "Iowa", "US", "50010"))
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testDifferentAddresses(boolean result, UserRegistration addr){
        assertEquals(result, addr.validateAddress());
    }
}

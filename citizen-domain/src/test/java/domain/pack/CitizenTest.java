package domain.pack;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CitizenTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCitizen() {
        Citizen citizen = new Citizen(
            "12345678",
            "ΓΙΩΡΓΟΣ",
            "ΠΑΠΑΔΟΠΟΥΛΟΣ",
            "ΑΝΔΡΑΣ",
            "01-01-1980",
            "123456789",
            "ΑΘΗΝΑ"
        );

        Set<ConstraintViolation<Citizen>> violations = validator.validate(citizen);
        assertTrue(violations.isEmpty(), "Δεν πρέπει να υπάρχουν παραβιάσεις επικύρωσης");
    }

    @Test
    void testInvalidAt() {
        Citizen citizen = new Citizen();
        citizen.setAt("123");

        Set<ConstraintViolation<Citizen>> violations = validator.validateProperty(citizen, "at");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidMandatoryFields() {
        Citizen citizen = new Citizen();

        Set<ConstraintViolation<Citizen>> violations = validator.validate(citizen);
        assertFalse(violations.isEmpty());

        boolean atViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("at"));
        boolean firstNameViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
        boolean lastNameViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
        boolean genderViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gender"));
        boolean dobViolation = violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth"));

        assertTrue(atViolation, "ΑΤ πρέπει να είναι υποχρεωτικό");
        assertTrue(firstNameViolation, "Όνομα πρέπει να είναι υποχρεωτικό");
        assertTrue(lastNameViolation, "Επίθετο πρέπει να είναι υποχρεωτικό");
        assertTrue(genderViolation, "Φύλο πρέπει να είναι υποχρεωτικό");
        assertTrue(dobViolation, "Ημερομηνία γέννησης πρέπει να είναι υποχρεωτική");
    }

    @Test
    void testInvalidAfm() {
        Citizen citizen = new Citizen();
        citizen.setAfm("12345"); 
        Set<ConstraintViolation<Citizen>> violations = validator.validateProperty(citizen, "afm");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        Citizen citizen = new Citizen();
        citizen.setFirstName("ΜΑΡΙΑ");
        assertEquals("ΜΑΡΙΑ", citizen.getFirstName());

        citizen.setLastName("ΝΙΚΟΛΑΟΥ");
        assertEquals("ΝΙΚΟΛΑΟΥ", citizen.getLastName());

        citizen.setGender("ΓΥΝΑΙΚΑ");
        assertEquals("ΓΥΝΑΙΚΑ", citizen.getGender());

        citizen.setDateOfBirth("15-08-1990");
        assertEquals("15-08-1990", citizen.getDateOfBirth());

        citizen.setAt("87654321");
        assertEquals("87654321", citizen.getAt());

        citizen.setAfm("987654321");
        assertEquals("987654321", citizen.getAfm());

        citizen.setAddress("ΘΕΣΣΑΛΟΝΙΚΗ");
        assertEquals("ΘΕΣΣΑΛΟΝΙΚΗ", citizen.getAddress());
    }

    
}

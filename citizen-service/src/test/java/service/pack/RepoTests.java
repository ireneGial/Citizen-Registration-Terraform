package service.pack;

import domain.pack.Citizen;
import jpa.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepoTests {

    @Autowired
    private JPACitizenRepository citizenRepository;

    @Test
    void testSaveAndFindById() {
        Citizen citizen = new Citizen("87654321", "ΑΝΝΑ", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ", "ΓΥΝΑΙΚΑ",
                "15-05-1990", "987654321", "ΘΕΣΣΑΛΟΝΙΚΗ");

        citizenRepository.save(citizen);

        Optional<Citizen> found = citizenRepository.findById("87654321");
        assertTrue(found.isPresent());
        assertEquals("ΑΝΝΑ", found.get().getFirstName());
    }

    @Test
    void testUpdateCitizen() {
        Citizen citizen = new Citizen("12345678", "ΓΙΩΡΓΟΣ", "ΠΑΠΑΔΟΠΟΥΛΟΣ", "ΑΝΔΡΑΣ",
                "01-01-1980", "123456789", "ΑΘΗΝΑ");
        citizenRepository.save(citizen);

        Citizen toUpdate = citizenRepository.findById("12345678").orElseThrow();
        toUpdate.setAfm("111222333");
        toUpdate.setAddress("ΠΑΤΡΑ");

        citizenRepository.save(toUpdate);

        Citizen updated = citizenRepository.findById("12345678").orElseThrow();
        assertEquals("111222333", updated.getAfm());
        assertEquals("ΠΑΤΡΑ", updated.getAddress());
    }

    @Test
    void testDeleteCitizen() {
        Citizen citizen = new Citizen("99999999", "ΝΙΚΟΣ", "ΚΑΡΑΜΑΝΛΗΣ", "ΑΝΔΡΑΣ",
                "10-10-1970", null, null);
        citizenRepository.save(citizen);

        citizenRepository.deleteById("99999999");

        assertFalse(citizenRepository.findById("99999999").isPresent());
    }

    @Test
    void testSaveInvalidCitizen() {
        Citizen invalidCitizen = new Citizen();
        invalidCitizen.setAt("123"); // invalid length

        assertThrows(ConstraintViolationException.class, () -> {
            citizenRepository.save(invalidCitizen);
            citizenRepository.flush(); // to trigger validation immediately
        });
    }

    @Test
    void testFindAll() {
        citizenRepository.save(new Citizen("11111111", "Α", "Β", "ΑΝΔΡΑΣ", "01-01-2000", null, null));
        citizenRepository.save(new Citizen("22222222", "Γ", "Δ", "ΓΥΝΑΙΚΑ", "02-02-2000", null, null));

        List<Citizen> all = citizenRepository.findAll();
        assertTrue(all.size() >= 2);
    }
}

package service.pack;

import domain.pack.Citizen;
import jpa.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JPAIntergrationTest {

    @Autowired
    private JPACitizenRepository citizenRepository;

    @Test
    @DisplayName("Αποθήκευση και ανάκτηση πολίτη")
    public void testSaveAndFindById() {
        Citizen citizen = new Citizen(
            "AB123456",
            "ΙΩΑΝΝΗΣ",
            "ΠΑΠΑΔΟΠΟΥΛΟΣ",
            "ΑΡΡΕΝ",
            "01-01-1990",
            "123456789",
            "ΚΑΤΕΧΑΚΗ 12"
        );

        
        Citizen savedCitizen = citizenRepository.save(citizen);
        assertThat(savedCitizen).isNotNull();

        Optional<Citizen> foundCitizenOpt = citizenRepository.findById("AB123456");
        assertThat(foundCitizenOpt).isPresent();

        Citizen foundCitizen = foundCitizenOpt.get();
        assertThat(foundCitizen.getFirstName()).isEqualTo("ΙΩΑΝΝΗΣ");
        assertThat(foundCitizen.getAfm()).isEqualTo("123456789");
    }

    @Test
    @DisplayName("Έλεγχος αν υπάρχει πολίτης με ΑΤ")
    public void testExistsById() {
        Citizen citizen = new Citizen(
            "CD987654",
            "ΜΑΡΙΑ",
            "ΝΙΚΟΛΑΟΥ",
            "ΘΗΛΥ",
            "15-05-1985",
            null,
            null
        );
        citizenRepository.save(citizen);

        boolean exists = citizenRepository.existsById("CD987654");
        assertThat(exists).isTrue();
    }

}

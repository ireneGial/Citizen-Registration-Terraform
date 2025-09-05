package controller.pack;

import domain.pack.Citizen;
import jpa.repository.JPACitizenRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CitizenController {

    private final JPACitizenRepository citizenRepository;

    public CitizenController(JPACitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    @PostMapping("/citizens")
    public ResponseEntity<?> createCitizen(@Valid @RequestBody Citizen citizen, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMsg);
        }

        if (citizenRepository.existsById(citizen.getAt())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Υπάρχει ήδη πολίτης με τον ίδιο ΑΤ.");
        }

        Citizen savedCitizen = citizenRepository.save(citizen);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCitizen);
    }

    @GetMapping("/citizens/{at}")
    public ResponseEntity<?> getCitizenByAt(@PathVariable("at") String at) {
        Optional<Citizen> citizenOpt = citizenRepository.findById(at);

        if (citizenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Δεν βρέθηκε πολίτης με ΑΤ: " + at);
        }

        return ResponseEntity.ok(citizenOpt.get());
    }

    @DeleteMapping("/citizens/{at}")
    public ResponseEntity<?> deleteCitizen(@PathVariable("at") String at) {
        if (at == null || at.trim().isEmpty() || at.length() != 8) {
            return ResponseEntity.badRequest().body("Μη έγκυρος αριθμός ταυτότητας (ΑΤ).");
        }

        if (!citizenRepository.existsById(at)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Δεν βρέθηκε πολίτης με τον ΑΤ: " + at);
        }

        citizenRepository.deleteById(at);
        return ResponseEntity.ok("Ο πολίτης διαγράφηκε επιτυχώς.");
    }

    @PutMapping("/citizens/{at}")
    public ResponseEntity<?> updateCitizen(@PathVariable("at") String at, @RequestBody Map<String, String> updates) {
        if (at == null || at.trim().isEmpty() || at.length() != 8) {
            return ResponseEntity.badRequest().body("Μη έγκυρος αριθμός ταυτότητας (ΑΤ).");
        }

        Optional<Citizen> citizenOpt = citizenRepository.findById(at);

        if (citizenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Δεν βρέθηκε πολίτης με ΑΤ: " + at);
        }

        Citizen citizen = citizenOpt.get();

        if (updates.containsKey("afm")) {
            String afm = updates.get("afm");
            if (!afm.isEmpty() && !afm.matches("\\d{9}")) {
                return ResponseEntity.badRequest().body("Μη έγκυρο ΑΦΜ!. Πρέπει να έχει 9 ψηφία.");
            }
            citizen.setAfm(afm);
        }

        if (updates.containsKey("address")) {
            citizen.setAddress(updates.get("address"));
        }

        citizenRepository.save(citizen);

        return ResponseEntity.ok("Ο πολίτης ενημερώθηκε επιτυχώς.");
    }

    @GetMapping("/citizens")
    public ResponseEntity<?> searchCitizens(
            @RequestParam(name = "at", required = false) String at,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "gender", required = false) String gender,
            @RequestParam(name = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(name = "afm", required = false) String afm,
            @RequestParam(name = "address", required = false) String address
    ) {
        
        List<Citizen> allCitizens = citizenRepository.findAll();

        List<Citizen> results = allCitizens.stream()
                .filter(c -> at == null || c.getAt().equals(at))
                .filter(c -> firstName == null || c.getFirstName().equalsIgnoreCase(firstName))
                .filter(c -> lastName == null || c.getLastName().equalsIgnoreCase(lastName))
                .filter(c -> gender == null || c.getGender().equalsIgnoreCase(gender))
                .filter(c -> dateOfBirth == null || c.getDateOfBirth().equals(dateOfBirth))
                .filter(c -> afm == null || (c.getAfm() != null && c.getAfm().equals(afm)))
                .filter(c -> address == null || (c.getAddress() != null && c.getAddress().equalsIgnoreCase(address)))
                .toList();

        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Δεν βρέθηκαν εγγραφές με τα δοθέντα κριτήρια.");
        }

        return ResponseEntity.ok(results);
    }

}

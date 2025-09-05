package service.pack;

import domain.pack.Citizen;
import jpa.repository.JPACitizenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitizenService {

    private final JPACitizenRepository citizenRepository;

    public CitizenService(JPACitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    public Citizen saveCitizen(Citizen citizen) {
        return citizenRepository.save(citizen);
    }

    public Optional<Citizen> findByAt(String at) {
        return citizenRepository.findById(at);
    }

    public boolean existsByAt(String at) {
        return citizenRepository.existsById(at);
    }

    public void deleteByAt(String at) {
        citizenRepository.deleteById(at);
    }

    public List<Citizen> findAll() {
        return citizenRepository.findAll();
    }

}
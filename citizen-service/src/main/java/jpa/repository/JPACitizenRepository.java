package jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.pack.Citizen;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPACitizenRepository extends JpaRepository<Citizen,String> {
	Optional<Citizen> findByAt(String at);

    List<Citizen> findByLastName(String lastName);

    List<Citizen> findByFirstNameAndLastName(String firstName, String lastName);

}

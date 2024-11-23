package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucan.edu.academic.entities.InitializationFlag;

@Repository
public interface InitializationFlagRepository extends JpaRepository<InitializationFlag, Long> {
}

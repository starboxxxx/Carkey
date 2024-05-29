package carkey.project.error.repository;

import carkey.project.error.domain.Error;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<Error, Long> {
}

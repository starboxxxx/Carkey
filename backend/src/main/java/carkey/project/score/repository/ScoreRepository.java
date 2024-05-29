package carkey.project.score.repository;

import carkey.project.score.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Score findByRnnVersion(String rnnVersion);
}

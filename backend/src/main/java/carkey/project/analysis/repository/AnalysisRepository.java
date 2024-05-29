package carkey.project.analysis.repository;

import carkey.project.analysis.domain.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByUserIdAndAnalyzeDate(Long userId, String analyzeDate);

    @Query("SELECT DISTINCT a.analyzeDate FROM Analysis a where a.user.id = :userId")
    List<String> findDistinctAnalyzeDateByUserId(Long userId);

    @Query("SELECT n FROM Analysis n ORDER BY n.analyzeDatetime DESC")
    List<Analysis> findAllOrderByAnalyzeDatetimeDesc();
}

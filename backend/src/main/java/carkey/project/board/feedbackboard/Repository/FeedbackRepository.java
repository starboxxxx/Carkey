package carkey.project.board.feedbackboard.Repository;

import carkey.project.board.feedbackboard.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}

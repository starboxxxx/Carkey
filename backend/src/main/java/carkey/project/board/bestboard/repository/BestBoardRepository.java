package carkey.project.board.bestboard.repository;

import carkey.project.board.bestboard.domain.BestBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BestBoardRepository extends JpaRepository<BestBoard, Long> {
}

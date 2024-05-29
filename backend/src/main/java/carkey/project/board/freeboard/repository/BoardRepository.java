package carkey.project.board.freeboard.repository;

import carkey.project.board.freeboard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUserId(Long userId);

    List<Board> findTop10ByOrderByRecommendCountDesc();

}

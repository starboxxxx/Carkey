package carkey.project.recommend.repository;

import carkey.project.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    Optional<Recommend> findByBoardIdAndUserId(Long boardId, Long userId);

    //추천 눌렀을때
    @Modifying
    @Query(value = "INSERT INTO recommend(board_id, user_id) VALUES(:board_id, :user_id)", nativeQuery = true)
    void recommend(Long board_id, Long user_id);

    //추천 한번 더 눌러서 취소
    @Modifying
    @Query(value = "DELETE FROM recommend WHERE board_id = :board_id AND user_id = :user_id", nativeQuery = true)
    void cancelRecommend(Long board_id, Long user_id);

    //추천 수 +1
    @Modifying
    @Query(value = "update Board board set board.recommendCount = board.recommendCount + 1 where board.id = :board_id")
    void plusRecommend(@Param("board_id") Long board_id);

    //추천 수 -1
    @Modifying
    @Query(value = "update Board board set board.recommendCount = board.recommendCount - 1 where board.id = :board_id")
    void minusRecommend(@Param("board_id") Long board_id);
}

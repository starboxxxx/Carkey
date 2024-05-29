package carkey.project.reply.repository;

import carkey.project.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByBoardId(Long boardId);
}

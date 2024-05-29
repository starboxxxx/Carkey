package carkey.project.notice.repository;

import carkey.project.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findFirstByOrderByCreatedDateDesc();


}

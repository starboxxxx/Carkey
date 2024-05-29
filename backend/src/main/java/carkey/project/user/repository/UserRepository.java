package carkey.project.user.repository;

import carkey.project.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);


    boolean existsByLoginId(String loginId);

    boolean existsByNickName(String nickName);

}

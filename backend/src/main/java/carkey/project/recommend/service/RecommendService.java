package carkey.project.recommend.service;

import carkey.project.recommend.repository.RecommendRepository;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean recommend(Long boardId) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        if (recommendRepository.findByBoardIdAndUserId(boardId, user.getId()).isEmpty()) {
            recommendRepository.recommend(boardId, user.getId());
            recommendRepository.plusRecommend(boardId);
            return true;
        }
        else {
            recommendRepository.cancelRecommend(boardId, user.getId());
            recommendRepository.minusRecommend(boardId);
            return false;
        }
    }
}

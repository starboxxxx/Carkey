package carkey.project.score.service;

import carkey.project.score.domain.Score;
import carkey.project.score.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;

    @Transactional
    public Score save(String rnnVersion, String r2Score) {
        Score score = new Score(rnnVersion, r2Score);
        scoreRepository.save(score);
        return score;
    }

    @Transactional
    public String findByVersion(String version) {
        Score score = scoreRepository.findByRnnVersion(version);
        return score.getR2Score();
    }

    @Transactional
    public void delete(String version) {
        String rnnVersion = "v"+ version;
        Score score = scoreRepository.findByRnnVersion(rnnVersion);
        scoreRepository.deleteById(score.getId());
    }
}

package carkey.project.board.feedbackboard.service;

import carkey.project.board.feedbackboard.Repository.FeedbackRepository;
import carkey.project.board.feedbackboard.domain.Feedback;
import carkey.project.board.feedbackboard.dto.FeedbackDto;
import carkey.project.board.feedbackboard.dto.FeedbackListDto;
import carkey.project.board.feedbackboard.dto.FeedbackSaveRequestDto;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    String projectPath = System.getProperty("user.home") + "/image/feedbackImages";

    @Transactional
    public void write(FeedbackSaveRequestDto feedbackSaveRequestDto, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Feedback feedback = new Feedback(user, feedbackSaveRequestDto.getTitle(),
                                        fileName, feedbackSaveRequestDto.getComment());

        feedbackRepository.save(feedback);
    }

    @Transactional
    public FeedbackDto getFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> {
            return new IllegalArgumentException("피드백 내용을 찾을 수 없습니다.");
        });
        return FeedbackDto.toDto(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackListDto> getFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        Collections.reverse(feedbacks);
        List<FeedbackListDto> feedbackListDtos = new ArrayList<>();

        feedbacks.forEach(s -> feedbackListDtos.add(FeedbackListDto.toDto(s)));
        return feedbackListDtos;
    }

    @Transactional
    public void delete(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }
}

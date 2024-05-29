package carkey.project.board.feedbackboard.controller;


import carkey.project.board.feedbackboard.dto.FeedbackSaveRequestDto;
import carkey.project.board.feedbackboard.service.FeedbackService;
import carkey.project.setting.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/mypage/feedback/save")
    public Response<?> save(@RequestPart(value = "feedbackSaveRequestDto") String feedbackSaveRequestDtoJson,
                            @RequestPart(value = "image") MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        FeedbackSaveRequestDto feedbackSaveRequestDto = objectMapper.readValue(feedbackSaveRequestDtoJson, FeedbackSaveRequestDto.class);
        service.write(feedbackSaveRequestDto, image);
        return new Response<>("true", "피드백 저장 완료", null);
    }
}

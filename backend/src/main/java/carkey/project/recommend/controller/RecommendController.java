package carkey.project.recommend.controller;

import carkey.project.recommend.service.RecommendService;
import carkey.project.setting.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/board/{boardId}/recommend")
    public Response<?> recommend(@PathVariable Long boardId) {
        if (recommendService.recommend(boardId)) {
            return new Response<>("true", "사용자 게시물 추천 완료", null);
        }
        else {
            return new Response<>("true", "사용자 게시물 추천 취소 완료", null);
        }
    }
}

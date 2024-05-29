package carkey.project.board.bestboard.controller;

import carkey.project.board.bestboard.service.BestBoardService;
import carkey.project.setting.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BestBoardController {
    private final BestBoardService bestBoardService;

    @GetMapping("/board/bestboard/{bestboardId}")
    public Response<?> getBestBoard(@PathVariable Long bestboardId) {
        return new Response<>("True", "베스트 게시물 상세조회 성공", bestBoardService.getBestBoard(bestboardId));
    }

    @GetMapping("/board/bestboard/list")
    public Response<?> getBestBoards() {
        return new Response<>("True", "베스트 게시물 목록 조회 성공", bestBoardService.getBestBoards());
    }
}

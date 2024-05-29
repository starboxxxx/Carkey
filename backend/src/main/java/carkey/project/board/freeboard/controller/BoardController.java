package carkey.project.board.freeboard.controller;

import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.dto.BoardDto;
import carkey.project.board.freeboard.dto.BoardSaveRequestDto;
import carkey.project.board.freeboard.service.BoardService;
import carkey.project.setting.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    // 게시글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/board/save")
    public Response<?> save(@RequestPart(value = "boardSaveRequestDto") String boardSaveRequestDtoJson,
                            @RequestPart(value = "image") List<MultipartFile> image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BoardSaveRequestDto boardSaveRequestDto = objectMapper.readValue(boardSaveRequestDtoJson, BoardSaveRequestDto.class);
        return new Response<>("true", "게시글 작성 성공", service.write(boardSaveRequestDto, image));
    }

    @GetMapping("/board/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> getBoard(@PathVariable Long boardId) {
        return new Response<>("true", "게시글 조회 완료", service.getBoard(boardId));
    }

    @GetMapping("/board/list")
    @ResponseStatus(HttpStatus.OK)
    public Response<?> findAll(Board board) {
        return new Response<>("true", "전체 게시글 조회 완료", service.getBoards());
    }

    @PutMapping("/board/{boardId}/edit")
    public Response<?> boardEdit(@RequestPart(value = "boardDto") String boardDtoJson,
                                 @RequestPart(value = "image") List<MultipartFile> image, @PathVariable Long boardId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BoardDto boardDto = objectMapper.readValue(boardDtoJson, BoardDto.class);
        return new Response<>("True", "게시글 수정 완료", service.edit(boardDto, boardId, image));
    }

    @GetMapping("/board/{boardId}/check")
    public Response<?> boardCheck(@PathVariable Long boardId) {
        if (service.findBoardUserId(boardId)) {
            return new Response<>("True", "게시글 본인 확인 완료", null);
        }
        else {
            return new Response<>("False", "게시글 본인 확인 중 오류 발생", null);
        }
    }

    @DeleteMapping("/board/delete/{boardId}")
    public Response<?> boardDelete(@PathVariable Long boardId) {
        if (service.findBoardUserId(boardId)) {
            return new Response<>("True", "게시글 삭제 완료", service.delete(boardId));
        }
        else {
            return new Response<>("False", "게시글 삭제 중 오류 발생", null);
        }
    }
}

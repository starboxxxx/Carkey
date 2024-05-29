package carkey.project.reply.controller;

import carkey.project.reply.domain.ReplyDto;
import carkey.project.reply.service.ReplyService;
import carkey.project.setting.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService service;

    // 게시물 저장(일단 임의로 boarId, userId는 파라미터로 받아옴
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/replies/{boardId}")
    public Response<?> save(@PathVariable Long boardId, @RequestBody ReplyDto replyDto) {
        return new Response<>("true", "댓글 작성을 완료했습니다.", service.writeReply(boardId, replyDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/replies/{boardId}")
    public Response<?> findAll(@PathVariable Long boardId) {
        return new Response<>("true", "모든 댓글 불러오기 완료.", service.getReplies(boardId));
    }

    @GetMapping("/replies/{replyId}/check")
    public Response<?> replyUserCheck(@PathVariable Long replyId) {
        if (service.findReplyUserId(replyId)) {
            return new Response<>("True", "댓글 본인 확인 완료", null);
        }
        else {
            return new Response<>("False", "댓글 본인 확인 실패", null);
        }
    }

    @PostMapping("/replies/{replyId}/edit")
    public Response<?> edit(@PathVariable Long replyId, @RequestBody ReplyDto replyDto) {
        return new Response<>("True", "댓글 수정 완료", service.edit(replyId, replyDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/replies/{replyId}")
    public Response<?> delete(@PathVariable Long replyId) {
        service.deleteReply(replyId);
        return new Response<>("True", "댓글 삭제 완료", null);
    }
}

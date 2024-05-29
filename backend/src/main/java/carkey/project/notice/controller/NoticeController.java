package carkey.project.notice.controller;

import carkey.project.notice.service.NoticeService;
import carkey.project.setting.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/user/notice/{noticeId}")
    public Response<?> findNotice(@PathVariable Long noticeId) {
        return new Response<>("True", "공지사항 상세 조회 성공", noticeService.findNotice(noticeId));
    }

    @GetMapping("/user/notice/first")
    public Response<?> findFirstNotice() {
        return new Response<>("True", "첫 번째 공지사항 조회 성공", noticeService.findRecentNotice());
    }

    @GetMapping("/user/notice/list")
    public Response<?> findAll() {
        return new Response<>("True", "공지사항 목록 조회 성공", noticeService.findAll());
    }
}

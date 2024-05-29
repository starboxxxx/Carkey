package carkey.project.admin.controller;

import carkey.project.analysis.dto.VersionRequestDto;
import carkey.project.analysis.service.AnalysisService;
import carkey.project.board.bestboard.service.BestBoardService;
import carkey.project.board.feedbackboard.service.FeedbackService;
import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.service.BoardService;
import carkey.project.error.dto.ErrorListDto;
import carkey.project.error.service.ErrorService;
import carkey.project.notice.dto.NoticeSaveDto;
import carkey.project.notice.service.NoticeService;
import carkey.project.reply.service.ReplyService;
import carkey.project.score.service.ScoreService;
import carkey.project.security.service.AuthService;
import carkey.project.setting.Response;
import carkey.project.user.dto.UserRequestDto;
import carkey.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthService authService;
    private final BoardService boardService;
    private final FeedbackService feedbackService;
    private final ReplyService replyService;
    private final BestBoardService bestBoardService;
    private final AnalysisService analysisService;
    private final ErrorService errorService;
    private final NoticeService noticeService;

    @PostMapping("/admin/signup")
    public Response<?> signup(@RequestBody UserRequestDto requestDto) {
        return new Response<>("True", "회원가입 성공", authService.adminSignup(requestDto));
    }

    @PostMapping("/admin/login")
    public Response<?> login(@RequestBody UserRequestDto requestDto) {
        return new Response<>("True", "로그인 성공", authService.login(requestDto));
    }

    @GetMapping("/admin/users/list")
    public Response<?> findAll() {
        return new Response<>("True", "조회 성공", userService.findAll());
    }

    @GetMapping("/admin/notice/{noticeId}")
    public Response<?> findNotice(@PathVariable Long noticeId) {
        return new Response<>("True", "공지사항 상세 조회 성공", noticeService.findAdminNotice(noticeId));
    }

    @GetMapping("/admin/notice/list")
    public Response<?> findNotices() {
        return new Response<>("True", "공지사항 목록 조회 성공", noticeService.findAll());
    }

    @GetMapping("/admin/board/{boardId}")
    public Response<?> getBoard(@PathVariable Long boardId) {
        return new Response<>("True", "게시글 조회 완료", boardService.getBoard(boardId));
    }

    @GetMapping("/admin/board/list")
    public Response<?> getBoardList(Board board) {
        return new Response<>("True", "전체 게시글 조회 완료", boardService.getBoards());
    }

    @GetMapping("/admin/bestBoard/update")
    public Response<?> updateBestBoard() throws IOException, InterruptedException {
        return new Response<>("True", "베스트 게시물 업데이트 성공", bestBoardService.updateBestBoard());
    }

    @GetMapping("/admin/bestBoard/{bestBoardId}")
    public Response<?> getBestBoard(@PathVariable Long bestBoardId) {
        return new Response<>("True", "베스트 게시물 상세 조회 성공", bestBoardService.getBestBoard(bestBoardId));
    }

    @GetMapping("/admin/bestBoard/list")
    public Response<?> getBestBoards() {
        return new Response<>("True", "베스트 게시물 목록 조회 성공", bestBoardService.getBestBoards());
    }

    @GetMapping("/admin/feedback/{feedbackId}")
    public Response<?> getFeedback(@PathVariable Long feedbackId) {
        return new Response<>("true", "피드백 조회 성공", feedbackService.getFeedback(feedbackId));
    }

    @GetMapping("/admin/feedback/list")
    public Response<?> getFeedbackList() {
        return new Response<>("true", "전체 피드백 조회", feedbackService.getFeedbacks());
    }

    @GetMapping("/admin/model/current")
    public Response<?> getCurrentVersion() {
        return new Response<>("True", "현재 모델 버전 조회 완료", analysisService.getCurrentVersion());
    }

    @GetMapping("/admin/analysis/list")
    public Response<?> getAnalysisList() {
        return new Response<>("true", "분석 내역 리스트 조회 완료", analysisService.findAnalyzeList());
    }

    @GetMapping("/admin/model/version")
    public Response<?> findModelVersion() throws IOException {
        VersionRequestDto versionRequestDto = analysisService.getModelVersions();
        return new Response<>("true", "모델 버전 리스트 조회 완료", versionRequestDto);
    }

    @GetMapping("/admin/error/list")
    public Response<?> findErrorList() {
        List<ErrorListDto> errorListDto = errorService.findAll();
        return new Response<>("true", "에러 리스트 조회 완료", errorListDto);
    }

    @PostMapping("/admin/model/select/{selectVersion}")
    public Response<?> selectModel(@PathVariable String selectVersion) {
        return new Response<>("true", "rnn_model_v" + selectVersion + " 선택 완료", analysisService.setVersion(selectVersion));
    }

    @PostMapping("/admin/model/train/{epoch}")
    public Response<?> trainModel(@PathVariable String epoch) throws IOException, InterruptedException {
        return new Response<>("true", "모델 추가 학습 완료", analysisService.trainModel(epoch));
    }

    @PostMapping("/admin/notice/save")
    public Response<?> save(@RequestBody NoticeSaveDto noticeSaveDto) {
        return new Response<>("true", "공지사항 저장 성공", noticeService.save(noticeSaveDto));
    }

    @PutMapping("/admin/notice/{noticeId}/edit")
    public Response<?> edit(@RequestBody NoticeSaveDto noticeSaveDto, @PathVariable Long noticeId) {
        return new Response<>("true", "공지사항 수정 완료", noticeService.edit(noticeId, noticeSaveDto));
    }

    @DeleteMapping("/admin/model/delete/{version}")
    public Response<?> deleteModel(@PathVariable String version) throws IOException {
        analysisService.deleteModel(version);
        return new Response<>("true",  "모델 버전 " + version + " 삭제 완료", null);
    }

    @DeleteMapping("/admin/users/{userId}")
    public Response<?> userDelete(@PathVariable Long userId) {
        userService.delete(userId);
        return new Response<>("true", "사용자 삭제 완료", null);
    }

    @DeleteMapping("/admin/board/{boardId}")
    public Response<?> boardDelete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return new Response<>("true", "게시글 삭제 완료", null);
    }

    @DeleteMapping("/admin/board/reply/{replyId}")
    public Response<?> replyDelete(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);
        return new Response<>("true", "댓글 삭제 완료", null);
    }

    @DeleteMapping("/admin/feedback/{feedbackId}")
    public Response<?> feedbackDelete(@PathVariable Long feedbackId) {
        feedbackService.delete(feedbackId);
        return new Response<>("true", "피드백 삭제 완료", null);
    }

    @DeleteMapping("/admin/analysis/delete/{analysisId}")
    public Response<?> analysisDelete(@PathVariable Long analysisId) {
        analysisService.delete(analysisId);
        return new Response<>("true", "분석 내역 삭제 완료", null);
    }

    @DeleteMapping("/admin/error/delete/{errorId}")
    public Response<?> errorDelete(@PathVariable Long errorId) {
        errorService.delete(errorId);
        return new Response<>("True", "에러 삭제 완료", null);
    }

    @DeleteMapping("/admin/notice/delete/{noticeId}")
    public Response<?> noticeDelete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return new Response<>("True", "공지사항 삭제 완료", null);
    }
}

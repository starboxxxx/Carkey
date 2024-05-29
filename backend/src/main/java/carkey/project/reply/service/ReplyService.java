package carkey.project.reply.service;

import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.repository.BoardRepository;
import carkey.project.reply.domain.Reply;
import carkey.project.reply.domain.ReplyDto;
import carkey.project.reply.repository.ReplyRepository;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public List<ReplyDto> writeReply(Long boardId, ReplyDto replyDto) {

        //게시판 번호로 게시글 찾기
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시판을 찾을 수 없습니다.");
        });

        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Reply reply = new Reply();
        reply.setComment(replyDto.getComment());

        reply.setUser(user);
        reply.setBoard(board);
        replyRepository.save(reply);

        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));

        return replyDtos;
    }

    // 글에 해당하는 전체 댓글 불러오기
    @Transactional(readOnly = true)
    public List<ReplyDto> getReplies(Long boardId) {
        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));
        return replyDtos;
    }

    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> {
            return new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        });
        replyRepository.deleteById(replyId);
    }
    @Transactional
    public ReplyDto edit(Long replyId, ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyId).get();
        reply.setComment(replyDto.getComment());
        return ReplyDto.toDto(reply);
    }

    @Transactional
    public Boolean findReplyUserId(Long replyId) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Reply reply = replyRepository.findById(replyId).get();
        if (reply.getUser().getId().equals(user.getId())) {
            return true;
        }
        else {
            return false;
        }
    }
}

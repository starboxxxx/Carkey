package carkey.project.board.freeboard.service;

import carkey.project.board.bestboard.domain.BestBoard;
import carkey.project.board.bestboard.repository.BestBoardRepository;
import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.dto.BoardDto;
import carkey.project.board.freeboard.dto.BoardListDto;
import carkey.project.board.freeboard.dto.BoardSaveRequestDto;
import carkey.project.board.freeboard.dto.ResponseBoardDto;
import carkey.project.board.freeboard.repository.BoardRepository;
import carkey.project.reply.domain.Reply;
import carkey.project.reply.domain.ReplyDto;
import carkey.project.reply.repository.ReplyRepository;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    String projectPath = System.getProperty("user.home") + "/image/boardImages";

    @Transactional
    public BoardDto write(BoardSaveRequestDto boardSaveRequestDto, List<MultipartFile> file) throws IOException {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Board board = new Board(user, boardSaveRequestDto.getTitle(),
                boardSaveRequestDto.getCost(), boardSaveRequestDto.getComment());
        List<String> imageNames = new ArrayList<>();
        for (MultipartFile multipartFile : file) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + multipartFile.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            multipartFile.transferTo(saveFile);

            imageNames.add(fileName);
        }
        board.setCloserImageName(imageNames.get(0));
        board.setEntireImageName(imageNames.get(1));

        boardRepository.save(board);
        return BoardDto.toDto(board);
    }

    // 개별 게시물 조회
    @Transactional(readOnly = true)
    public ResponseBoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        });
        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));


        board.setCost(NumberFormat.getInstance().format(Integer.parseInt(board.getCost())));
        return ResponseBoardDto.toDto(board, replyDtos);
    }

    // 전체 게시물 조회
    @Transactional(readOnly = true)
    public List<BoardListDto> getBoards() {
        List<Board> boards = boardRepository.findAll();
        Collections.reverse(boards);
        List<BoardListDto> boardListDtos = new ArrayList<>();
        boards.forEach(s -> boardListDtos.add(BoardListDto.toDto(s)));
        return boardListDtos;
    }

    @Transactional
    public ResponseBoardDto edit(BoardDto boardDto, Long boardId, List<MultipartFile> file) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        });
        board.setTitle(boardDto.getTitle());
        board.setComment(boardDto.getComment());
        board.setCost(boardDto.getCost());

        List<String> imageNames = new ArrayList<>();

        for (MultipartFile multipartFile : file) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + multipartFile.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            multipartFile.transferTo(saveFile);
            imageNames.add(fileName);
        }
        board.setCloserImageName(imageNames.get(0));
        board.setEntireImageName(imageNames.get(1));
        boardRepository.save(board);


        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));

        ResponseBoardDto dto = ResponseBoardDto.toDto(board, replyDtos);
        dto.setCost(NumberFormat.getInstance().format(Integer.parseInt(board.getCost())));
        return dto;
    }

    @Transactional
    public ResponseBoardDto delete(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        });
        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));

        boardRepository.deleteById(boardId);
        return ResponseBoardDto.toDto(board, replyDtos);
    }

    @Transactional
    public Boolean findBoardUserId(Long boardId) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        });
        if (board.getUser().getId().equals(user.getId())) {
            return true;
        } else {
            return false;
        }
    }
}

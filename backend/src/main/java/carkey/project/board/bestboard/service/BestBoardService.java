package carkey.project.board.bestboard.service;

import carkey.project.board.bestboard.domain.BestBoard;
import carkey.project.board.bestboard.repository.BestBoardRepository;
import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.dto.BoardListDto;
import carkey.project.board.freeboard.dto.ResponseBoardDto;
import carkey.project.board.freeboard.repository.BoardRepository;
import carkey.project.reply.domain.Reply;
import carkey.project.reply.domain.ReplyDto;
import carkey.project.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BestBoardService {

    private final BestBoardRepository bestBoardRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;


    @Transactional(readOnly = true)
    public ResponseBoardDto getBestBoard(Long bestBoardId) {
        BestBoard bestBoard = bestBoardRepository.findById(bestBoardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        });
        Long boardId = bestBoard.getBoard().getId();
        Board board = boardRepository.findById(boardId).get();
        List<Reply> replies = replyRepository.findByBoardId(boardId);
        List<ReplyDto> replyDtos = new ArrayList<>();

        replies.forEach(s -> replyDtos.add(ReplyDto.toDto(s)));

        board.setCost(NumberFormat.getInstance().format(Integer.parseInt(board.getCost())));
        return ResponseBoardDto.toDto(board, replyDtos);
    }
    @Transactional
    public List<BoardListDto> getBestBoards() {
        List<Board> boards = new ArrayList<>();
        List<BestBoard> bestBoards = bestBoardRepository.findAll();
        for (BestBoard bestBoard : bestBoards) {
            Board board = boardRepository.findById(bestBoard.getBoard().getId()).get();
            boards.add(board);
        }
        List<BoardListDto> boardListDtos = new ArrayList<>();
        boards.forEach(s -> boardListDtos.add(BoardListDto.toDto(s)));
        return boardListDtos;
    }

    @Transactional
    public List<BoardListDto> updateBestBoard() throws IOException {
        bestBoardRepository.deleteAll();
        List<Board> boards = boardRepository.findTop10ByOrderByRecommendCountDesc();
        for (Board board : boards) {
            BestBoard bestBoard = new BestBoard(board);
            bestBoardRepository.save(bestBoard);
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/ai/carkey_project_module_image_analyze.py", board.getCloserImageName(), board.getCost());
            processBuilder.redirectErrorStream(true);
            processBuilder.start();
        }
        List<BoardListDto> boardListDtos = new ArrayList<>();
        boards.forEach(s -> boardListDtos.add(BoardListDto.toDto(s)));
        return boardListDtos;
    }
}

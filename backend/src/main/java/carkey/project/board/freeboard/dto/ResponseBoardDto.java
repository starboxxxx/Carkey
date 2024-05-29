package carkey.project.board.freeboard.dto;


import carkey.project.board.freeboard.domain.Board;
import carkey.project.reply.domain.ReplyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBoardDto {


    private String nickName;

    private String title;

    private String cost;

    private String closerImageName;

    private String entireImageName;

    private String comment;

    private int recommendCount;

    private String postDate;

    private List<ReplyDto> replies;

    public static ResponseBoardDto toDto(Board board, List<ReplyDto> replies) {
        return new ResponseBoardDto(
                board.getUser().getNickName(),
                board.getTitle(),
                board.getCost(),
                board.getCloserImageName(),
                board.getEntireImageName(),
                board.getComment(),
                board.getRecommendCount(),
                board.getPostDate(),
                replies);
    };
}
package carkey.project.board.freeboard.dto;

import carkey.project.board.freeboard.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private String nickName;

    private String title;

    private String cost;

    private String closerImageName;

    private String entireImageName;

    private String comment;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getUser().getNickName(),
                board.getTitle(),
                board.getCost(),
                board.getCloserImageName(),
                board.getEntireImageName(),
                board.getComment());
    }
}

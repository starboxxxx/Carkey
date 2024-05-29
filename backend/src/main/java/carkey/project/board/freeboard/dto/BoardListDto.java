package carkey.project.board.freeboard.dto;

import carkey.project.board.freeboard.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDto {

    private Long boardId;

    private String title;

    private String nickName;

    private String postDate;

    private int recommendCount;

    public static BoardListDto toDto(Board board) {
        return new BoardListDto(
                board.getId(),
                board.getTitle(),
                board.getUser().getNickName(),
                board.getPostDate(),
                board.getRecommendCount()
        );
    }
}

package carkey.project.board.freeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequestDto {

    private String title;

    private String cost;

    private String comment;
}

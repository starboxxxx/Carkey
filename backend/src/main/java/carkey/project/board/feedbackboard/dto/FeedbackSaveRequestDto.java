package carkey.project.board.feedbackboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackSaveRequestDto {
    private String title;
    private String comment;
}

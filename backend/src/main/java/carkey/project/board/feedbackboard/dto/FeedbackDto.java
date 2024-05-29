package carkey.project.board.feedbackboard.dto;

import carkey.project.board.feedbackboard.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long feedbackId;
    private String nickName;
    private String title;
    private String imgPath;
    private String comment;
    private String postDate;

    public static FeedbackDto toDto(Feedback feedback) {
        return new FeedbackDto(
                feedback.getId(),
                feedback.getUser().getNickName(),
                feedback.getTitle(),
                feedback.getImgName(),
                feedback.getComment(),
                feedback.getPostDate()
        );
    }
}

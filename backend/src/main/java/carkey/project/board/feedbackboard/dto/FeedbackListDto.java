package carkey.project.board.feedbackboard.dto;

import carkey.project.board.feedbackboard.domain.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackListDto {
    private Long feedbackId;
    private String nickName;
    private String title;
    private String imgPath;
    private String comment;
    private String postDate;

    public static FeedbackListDto toDto(Feedback feedback) {
        return new FeedbackListDto(
                feedback.getId(),
                feedback.getUser().getNickName(),
                feedback.getTitle(),
                feedback.getImgName(),
                feedback.getComment(),
                feedback.getPostDate()
        );
    }
}

package carkey.project.reply.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private Long replyId;
    private String nickName;
    private String comment;

    public static ReplyDto toDto(Reply reply) {
        return new ReplyDto(
                reply.getReplyId(),
                reply.getUser().getNickName(),
                reply.getComment()
        );
    }
}

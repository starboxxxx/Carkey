package carkey.project.notice.dto;

import carkey.project.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

    private String title;

    private String nickName;

    private String comment;

    private String createdDate;

    public static NoticeDto toDto(Notice notice) {
        return new NoticeDto(
                notice.getTitle(),
                "관리자",
                notice.getComment(),
                notice.getCreatedDate()
        );
    }
}

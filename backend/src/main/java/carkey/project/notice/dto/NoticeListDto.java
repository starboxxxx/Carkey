package carkey.project.notice.dto;

import carkey.project.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListDto {

    private Long noticeId;

    private String nickName;

    private String title;

    private String comment;

    private String createdDate;

    public static NoticeListDto toDto(Notice notice) {
        return new NoticeListDto(
                notice.getId(),
                "관리자",
                notice.getTitle(),
                notice.getComment(),
                notice.getCreatedDate()
        );
    }
}

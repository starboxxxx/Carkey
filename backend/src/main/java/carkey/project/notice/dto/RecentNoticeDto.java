package carkey.project.notice.dto;

import carkey.project.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentNoticeDto {

    private String title;

    private String comment;

    private String createdDate;



    public static RecentNoticeDto toDto(Notice notice) {
        return new RecentNoticeDto(
                notice.getTitle(),
                notice.getComment(),
                notice.getCreatedDate());
    }
}

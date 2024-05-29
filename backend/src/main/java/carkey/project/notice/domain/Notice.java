package carkey.project.notice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "notice")
public class Notice {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    private String comment;

    private String createdDate;

    public Notice(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    @PrePersist
    public void createdDate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createdDate = customLocalDateTimeFormat;
    }
}

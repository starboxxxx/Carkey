package carkey.project.board.feedbackboard.domain;

import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@Table(name="feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String imgName;

    private String comment;

    private String postDate;
    public Feedback(User user, String title, String imgName, String comment) {
        this.user = user;
        this.title = title;
        this.imgName = imgName;
        this.comment = comment;
    }

    @PrePersist
    public void postDate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.postDate = customLocalDateTimeFormat;
    }
}

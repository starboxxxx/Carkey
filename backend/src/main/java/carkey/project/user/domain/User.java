package carkey.project.user.domain;

import carkey.project.analysis.domain.Analysis;
import carkey.project.board.feedbackboard.domain.Feedback;
import carkey.project.board.freeboard.domain.Board;
import carkey.project.error.domain.Error;
import carkey.project.recommend.domain.Recommend;
import carkey.project.reply.domain.Reply;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;

    @Column(name = "login_id")
    private String loginId;

    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analysis> analysis = new ArrayList<>();

    @Column(name = "created_date")
    private String createdDate;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(Long id, String loginId, String password, String nickName, Authority authority) {
        this.Id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.authority = authority;
    }

    @PrePersist
    public void createdDate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createdDate = customLocalDateTimeFormat;
    }

}

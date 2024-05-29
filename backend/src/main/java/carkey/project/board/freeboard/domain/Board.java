package carkey.project.board.freeboard.domain;

import carkey.project.board.bestboard.domain.BestBoard;
import carkey.project.recommend.domain.Recommend;
import carkey.project.reply.domain.Reply;
import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private long Id;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BestBoard> bestBoards = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String cost;

    private String comment;

    private String closerImageName;

    private String entireImageName;

    private int recommendCount;

    private String postDate;


    public Board(User user, String title, String cost, String comment) {
        this.user = user;
        this.title = title;
        this.cost = cost;
        this.comment = comment;
    }


    @PrePersist
    public void postDate() {
        this.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}

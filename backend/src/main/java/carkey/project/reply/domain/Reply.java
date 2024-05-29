package carkey.project.reply.domain;

import carkey.project.board.freeboard.domain.Board;
import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPLY")
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String comment;
}

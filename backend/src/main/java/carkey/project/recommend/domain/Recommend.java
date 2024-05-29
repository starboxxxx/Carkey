package carkey.project.recommend.domain;

import carkey.project.board.freeboard.domain.Board;
import carkey.project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="RECOMMEND")
@NoArgsConstructor
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}


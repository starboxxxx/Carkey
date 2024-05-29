package carkey.project.board.bestboard.domain;

import carkey.project.board.freeboard.domain.Board;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name="bestboard")
@NoArgsConstructor
public class BestBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bestboard_id")
    private Long id;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String updateDate;

    public BestBoard(Board board) {
        this.board = board;
    }


    @PrePersist
    public void postDate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.updateDate = customLocalDateTimeFormat;
    }
}

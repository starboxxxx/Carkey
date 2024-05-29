package carkey.project.score.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private long id;

    private String rnnVersion;

    private String r2Score;

    public Score(String rnnVersion, String r2Score) {
        this.rnnVersion = rnnVersion;
        this.r2Score = r2Score;
    }
}

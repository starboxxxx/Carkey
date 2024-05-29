package carkey.project.score.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {
    private String rnnVersion;

    private String r2Score;
}

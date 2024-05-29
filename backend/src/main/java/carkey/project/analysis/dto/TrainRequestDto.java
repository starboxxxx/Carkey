package carkey.project.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRequestDto {

    private String lossImageName;
    private String modelPerformanceImageName;
    private String r2Score;
    private String rnnVersion;
}

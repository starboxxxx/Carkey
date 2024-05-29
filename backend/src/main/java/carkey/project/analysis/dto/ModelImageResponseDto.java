package carkey.project.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelImageResponseDto {
    private String lossImageName;
    private String modelPerformanceImageName;

    public static ModelImageResponseDto toDto(TrainRequestDto trainRequestDto) {
        return new ModelImageResponseDto(
                trainRequestDto.getLossImageName(),
                trainRequestDto.getLossImageName());
    }
}

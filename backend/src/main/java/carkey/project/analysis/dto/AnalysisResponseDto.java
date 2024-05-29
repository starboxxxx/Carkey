package carkey.project.analysis.dto;

import carkey.project.analysis.domain.Analysis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponseDto {

    private String totalPrice;

    private String naturalImg;

    private String analyzeDatetime;

    public static AnalysisResponseDto toDto(Analysis analysis) {
        return new AnalysisResponseDto(
                analysis.getTotalPrice(),
                analysis.getNaturalImg(),
                analysis.getAnalyzeDatetime());
    }

}

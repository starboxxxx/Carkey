package carkey.project.analysis.dto;

import carkey.project.analysis.domain.Analysis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisSaveDto {

    private Long analysisId;

    private String totalPrice;

    private String naturalImg;

    private String originalImg;

    private String scratchImg;

    private String crushedImg;

    public static AnalysisSaveDto toDto(Analysis analysis) {
        return new AnalysisSaveDto(
                analysis.getId(),
                analysis.getTotalPrice(),
                analysis.getNaturalImg(),
                analysis.getOriginalImg(),
                analysis.getScratchImg(),
                analysis.getCrushedImg());
    }
}

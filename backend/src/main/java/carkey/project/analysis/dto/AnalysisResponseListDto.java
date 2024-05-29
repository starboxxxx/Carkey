package carkey.project.analysis.dto;

import carkey.project.analysis.domain.Analysis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResponseListDto {

    private Long analysisId;

    private String totalPrice;

    private String nickName;

    private String originalImg;

    private String naturalImg;

    private String scratchImg;

    private String crushedImg;

    private String analyzeDate;

    public static AnalysisResponseListDto toDto(Analysis analysis) {
        return new AnalysisResponseListDto(
                analysis.getId(),
                analysis.getTotalPrice(),
                analysis.getUser().getNickName(),
                analysis.getOriginalImg(),
                analysis.getNaturalImg(),
                analysis.getScratchImg(),
                analysis.getCrushedImg(),
                analysis.getAnalyzeDate());
    }
}

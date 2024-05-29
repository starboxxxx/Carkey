package carkey.project.analysis.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResultDto {

    private String totalPrice;

    private String naturalImg;

    private String originalImg;

    private String scratchImg;

    private String crushedImg;

    private String r2Image;

    private String pixelPerformanceImage;

    private String priceImage;
}

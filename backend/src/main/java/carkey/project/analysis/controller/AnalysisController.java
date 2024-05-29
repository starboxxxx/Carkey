package carkey.project.analysis.controller;

import carkey.project.analysis.dto.AnalysisResultDto;
import carkey.project.analysis.dto.AnalysisSaveDto;
import carkey.project.analysis.dto.AnalysisRequestDto;
import carkey.project.analysis.service.AnalysisService;
import carkey.project.error.service.ErrorService;
import carkey.project.setting.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AnalysisController {

    private final AnalysisService service;
    private final ErrorService errorService;

    @GetMapping("/user/mypage/analyze")
    public Response<?> getDistinctAnalyzeDates() {
        return new Response<>("True", "분석 날짜 조회 완료", service.getDistinctAnalyzeDates());
    }

    @PostMapping("/user/analyze/save")
    public Response<?> save(@RequestBody AnalysisSaveDto analysisSaveDto) throws IOException {
        return new Response<>("True", "분석 내역 저장 완료", service.write(analysisSaveDto));
    }

    @PostMapping("/user/analyze/cost")
    public Response<?> analyze(@RequestPart(value = "image") MultipartFile image) throws IOException{

        String imageName = service.saveImage(image);
        String output = service.getAnalyzeCost(imageName);

        if (output.contains("error")) {
            errorService.save(output);
            return new Response<>("False", "수리비 분석 중 오류 발생", null);
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            AnalysisResultDto analysisResultDto = mapper.readValue(output, AnalysisResultDto.class);
            analysisResultDto.setTotalPrice(NumberFormat.getInstance().format(Integer.parseInt(analysisResultDto.getTotalPrice())));
            return new Response<>("True", "수리비 분석 완료", analysisResultDto);
        }
    }

    @PostMapping("/user/mypage/analyze/list")
    public Response<?> findAnalyzeList(@RequestBody AnalysisRequestDto requestDto) {
        return new Response<>("True", "분석 리스트 조회 완료", service.findByAnalyzeDate(requestDto));
    }

}

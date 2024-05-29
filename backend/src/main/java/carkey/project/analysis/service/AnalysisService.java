package carkey.project.analysis.service;

import carkey.project.analysis.domain.Analysis;
import carkey.project.analysis.dto.*;
import carkey.project.analysis.repository.AnalysisRepository;
import carkey.project.score.service.ScoreService;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;
    private final ScoreService scoreService;
    String version = "v0.1";
    String projectPath = System.getProperty("user.home") + "/image/ai/image";

    @Transactional
    public AnalysisSaveDto write(AnalysisSaveDto analysisSaveDto) throws IOException {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        Analysis analysis = new Analysis(user, analysisSaveDto.getTotalPrice(), analysisSaveDto.getOriginalImg(),
                analysisSaveDto.getNaturalImg(), analysisSaveDto.getScratchImg(), analysisSaveDto.getCrushedImg());
        analysisRepository.save(analysis);
        return AnalysisSaveDto.toDto(analysis);
    }

    @Transactional
    public String saveImage(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        return fileName;
    }




    @Transactional
    public AnalysisDateResponseDto getDistinctAnalyzeDates() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        List<String> analyzeDates = analysisRepository.findDistinctAnalyzeDateByUserId(user.getId());
        return new AnalysisDateResponseDto(analyzeDates);
    }

    @Transactional
    public List<AnalysisResponseDto> findByAnalyzeDate(AnalysisRequestDto requestDto) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();

        List<Analysis> analysisList = analysisRepository.findByUserIdAndAnalyzeDate(user.getId(), requestDto.getAnalyzeDate());
        List<AnalysisResponseDto> analysisResponseDtos = new ArrayList<>();
        analysisList.forEach(s -> analysisResponseDtos.add(AnalysisResponseDto.toDto(s)));
        return analysisResponseDtos;
    }

    @Transactional
    public List<AnalysisResponseListDto> findAnalyzeList() {
        List<Analysis> analysisList = analysisRepository.findAllOrderByAnalyzeDatetimeDesc();
        List<AnalysisResponseListDto> analysisResponseListDtos = new ArrayList<>();
        analysisList.forEach(s -> analysisResponseListDtos.add(AnalysisResponseListDto.toDto(s)));
        return analysisResponseListDtos;
    }

    @Transactional
    public String setVersion(String selectVersion) {
         version = "v" + selectVersion;
         return version;
    }

    @Transactional
    public String getAnalyzeCost(String imageName) throws IOException {
        String score = scoreService.findByVersion(version);
        ProcessBuilder processBuilder = new ProcessBuilder("python", System.getProperty("user.home") + "/carkey_v1.0/backend/src/main/ai/carkey_project_module_price_predict.py", imageName, version, score);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        output.append(reader.readLine());
        return output.toString();
    }

    @Transactional
    public TrainRequestDto getCurrentVersion() {
        String currentVersion = "rnn_model_" + version;
        TrainRequestDto trainRequestDto = new TrainRequestDto();
        trainRequestDto.setRnnVersion(currentVersion);
        return trainRequestDto;
    }

    @Transactional
    public ModelImageResponseDto trainModel(String epoch) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", System.getProperty("user.home") +
                "/carkey_v1.0/backend/src/main/ai/carkey_project_module_addtional_train_rnn.py", epoch);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        output.append(reader.readLine());
        ObjectMapper mapper = new ObjectMapper();
        reader.close();
        TrainRequestDto trainRequestDto = mapper.readValue(output.toString(), TrainRequestDto.class);
        scoreService.save(trainRequestDto.getRnnVersion(), trainRequestDto.getR2Score());
        return ModelImageResponseDto.toDto(trainRequestDto);
    }

    @Transactional
    public void deleteModel(String version) throws IOException {
        scoreService.delete(version);
        ProcessBuilder processBuilder = new ProcessBuilder("python", System.getProperty("user.home") + "/carkey_v1.0/backend/src/main/ai/carkey_project_module_version_delete.py", version);
        processBuilder.redirectErrorStream(true);
        processBuilder.start();
    }

    @Transactional
    public VersionRequestDto getModelVersions() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", System.getProperty("user.home") + "/carkey_v1.0/backend/src/main/ai/carkey_project_module_all_version_search.py");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        output.append(reader.readLine());
        ObjectMapper mapper = new ObjectMapper();
        reader.close();
        return mapper.readValue(output.toString(), VersionRequestDto.class);
    }

    @Transactional
    public void delete(Long analysisId) {
        analysisRepository.deleteById(analysisId);
    }

}

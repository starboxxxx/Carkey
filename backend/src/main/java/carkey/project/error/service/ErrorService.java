package carkey.project.error.service;

import carkey.project.error.domain.Error;
import carkey.project.error.dto.ErrorDto;
import carkey.project.error.dto.ErrorListDto;
import carkey.project.error.repository.ErrorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ErrorService {

    private final ErrorRepository errorRepository;

    @Transactional
    public ErrorDto save(String output) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorDto errorDto = mapper.readValue(output, ErrorDto.class);
        Error error = new Error(errorDto.getError());
        errorRepository.save(error);
        return errorDto;
    }

    @Transactional
    public List<ErrorListDto> findAll() {
        List<Error> errors = errorRepository.findAll();

        List<ErrorListDto> errorListDtos = new ArrayList<>();

        errors.forEach(s -> errorListDtos.add(ErrorListDto.toDto(s)));
        return errorListDtos;
    }

    @Transactional
    public void delete(Long errorId) {
        errorRepository.deleteById(errorId);
    }
}

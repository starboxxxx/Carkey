package carkey.project.error.dto;

import carkey.project.error.domain.Error;
import carkey.project.user.domain.User;
import carkey.project.user.dto.UserListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorListDto {

    private Long errorId;

    private String error;

    private String createdDate;

    public static ErrorListDto toDto(Error error) {
        return new ErrorListDto(
                error.getId(),
                error.getError(),
                error.getCreatedDate());
    }
}

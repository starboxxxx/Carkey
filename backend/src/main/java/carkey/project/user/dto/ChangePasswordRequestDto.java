package carkey.project.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {
    private String loginId;
    private String exPassword;
    private String newPassword;
}

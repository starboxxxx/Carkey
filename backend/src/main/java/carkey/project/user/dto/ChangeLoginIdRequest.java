package carkey.project.user.dto;

import lombok.Getter;

@Getter
public class ChangeLoginIdRequest {
    private String loginId;
    private String newLoginId;
}

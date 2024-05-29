package carkey.project.user.dto;

import carkey.project.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {

    private Long userId;

    private String nickName;

    private String loginId;

    private String password;

    private String createdDate;

    public static UserListDto toDto(User user) {
        return new UserListDto(
                user.getId(),
                user.getNickName(),
                user.getLoginId(),
                user.getPassword(),
                user.getCreatedDate());
    }
}

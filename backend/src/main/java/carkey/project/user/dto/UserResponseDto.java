package carkey.project.user.dto;

import carkey.project.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private long userId;
    private String loginId;
    private String nickName;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .nickName(user.getNickName())
                .build();
    }

}

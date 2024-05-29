package carkey.project.user.dto;

import carkey.project.user.domain.Authority;
import carkey.project.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long userId;
    private String loginId;
    private String newLoginId;
    private String password;
    private String nickName;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .nickName(nickName)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public User toAdmin(PasswordEncoder passwordEncoder) {
        return User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .nickName(nickName)
                .authority(Authority.ROLE_ADMIN)
                .build();
    }
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}

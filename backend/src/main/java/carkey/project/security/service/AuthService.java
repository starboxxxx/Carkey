package carkey.project.security.service;

import carkey.project.security.TokenProvider;
import carkey.project.security.domain.TokenDto;
import carkey.project.user.domain.User;
import carkey.project.user.dto.UserRequestDto;
import carkey.project.user.dto.UserResponseDto;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDto adminSignup(UserRequestDto requestDto) {
        if (userRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        User user = requestDto.toAdmin(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    public UserResponseDto userSignUp(UserRequestDto requestDto) {
        if (userRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }
}

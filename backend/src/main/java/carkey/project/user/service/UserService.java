package carkey.project.user.service;

import carkey.project.user.dto.UserListDto;
import carkey.project.board.freeboard.domain.Board;
import carkey.project.board.freeboard.dto.BoardListDto;
import carkey.project.board.freeboard.repository.BoardRepository;
import carkey.project.security.config.SecurityUtil;
import carkey.project.user.domain.User;
import carkey.project.user.dto.UserResponseDto;
import carkey.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;


    @Transactional
    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public UserResponseDto findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
    }

    public List<UserListDto> findAll() {

        List<User> userList = userRepository.findAll();
        List<UserListDto> userListDtos = new ArrayList<>();

        userList.forEach(s -> userListDtos.add(UserListDto.toDto(s)));
        return userListDtos;
    }

    public List<BoardListDto> findBoardListByNickName() {

        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        List<Board> boardList = boardRepository.findByUserId(user.getId());
        Collections.reverse(boardList);
        List<BoardListDto> boardListDtos = new ArrayList<>();
        boardList.forEach(s -> boardListDtos.add(BoardListDto.toDto(s)));
        return boardListDtos;
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public void userDelete() {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        userRepository.deleteById(user.getId());
    }



    public UserResponseDto changeNickName(String nickName) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        user.setNickName(nickName);
        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    public UserResponseDto changeLoginId(String newLoginId) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).get();
        user.setLoginId(newLoginId);
        return UserResponseDto.of(userRepository.save(user));
    }

    public UserResponseDto changePassword(String loginId, String newPassword) {
        User user = userRepository.findByLoginId(loginId).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        return UserResponseDto.of(userRepository.save(user));
    }

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean checkNickNameDuplicate(String nickName) {
        return userRepository.existsByNickName(nickName);
    }
}

package carkey.project.user.controller;

import carkey.project.security.domain.TokenDto;
import carkey.project.security.service.AuthService;
import carkey.project.setting.Response;
import carkey.project.user.dto.*;
import carkey.project.user.service.UserService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/user/nonUsers")
    public Response<?> nonUsers() {
        NonUsersDto nonUsersDto = new NonUsersDto();
        return new Response<>("True", "비회원 토큰 값 전달 완료", nonUsersDto);
    }

    @PostMapping("/user/signup")
    public Response<?> signup(@RequestBody UserRequestDto requestDto) {
        return new Response<>("True", "사용자 회원가입 성공", authService.userSignUp(requestDto));
    }

    @PostMapping("/user/login")
    public Response<?> login(@RequestBody UserRequestDto requestDto) {
        TokenDto token = authService.login(requestDto);
        if (token.getAccessToken() == null) {
            return new Response<>("False", "사용자 로그인 실패", null);
        }
        else {
            return new Response<>("True", "사용자 로그인 성공", authService.login(requestDto));
        }
    }

    // 마이페이지에서 회원정보 변경 페이지 들어갔을때 필요한 회원정보
    @GetMapping("/mypage/user/info")
    public Response<?> findUser(){
        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        return new Response<>("True", "사용자 조회 성공", myInfoBySecurity);
    }

    @PostMapping("/user/findpwd")
    public Response<?> findByLoginId(@RequestBody UserRequestDto requestDto) {
        return new Response<>("True", "사용자 loginId로 조회 성공", userService.findByLoginId(requestDto.getLoginId()));
    }

    @PostMapping("/user/loginId/exists")
    public Response<?> checkLoginIdDuplicate(@RequestBody UserRequestDto requestDto) {
        if (userService.checkLoginIdDuplicate(requestDto.getLoginId())) {
            return new Response<>("false", "아이디 중복 발생", null);
        }
        else {
            return new Response<>("true", "아이디 중복 이상 없음", null);
        }
    }

    @PostMapping("/user/nickName/exists")
    public Response<?> checkNickNameDuplicate(@RequestBody UserRequestDto requestDto) {
        if (userService.checkNickNameDuplicate(requestDto.getNickName())) {
            return new Response<>("false", "닉네임 중복 발생", null);
        }
        else {
            return new Response<>("true", "닉네임 중복 이상 없음", null);
        }
    }

    //사용자가 쓴 글 조회
    @GetMapping("/user/mypage/boardList")
    public Response<?> findBoardListByNickName() {
        return new Response<>("true", "게시글 조회 성공", userService.findBoardListByNickName());
    }

    //사용자 닉네임 변경
    @PutMapping("/user/mypage/infoChange/nickName")
    public Response<?> changeNickName(@RequestBody UserRequestDto requestDto) {
        return new Response<>("True", "사용자 닉네임 변경 성공", userService.changeNickName(requestDto.getNickName()));
    }


    //사용자 아이디 변경
    @PutMapping("/user/mypage/infoChange/loginId")
    public Response<?> changeLoginId(@RequestBody ChangeLoginIdRequest requestDto) {
        return new Response<>("True", "사용자 로그인 아이디 변경 성공", userService.changeLoginId(requestDto.getNewLoginId()));
    }

    //사용자 비밀번호 변경
    @PutMapping("/user/mypage/infoChange/password")
    public Response<?> changePassword(@RequestBody ChangePasswordRequestDto requestDto) {
        return new Response<>("True", "사용자 비밀번호 변경 성공", userService.changePassword(requestDto.getLoginId(), requestDto.getNewPassword()));
    }

    @DeleteMapping("/user/delete")
    public Response<?> userDelete() {
        userService.userDelete();
        return new Response<>("true", "사용자 삭제 완료", null);
    }
}

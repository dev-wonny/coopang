package com.coopang.user.presentation.controller;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.application.service.UserService;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.SearchRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.UpdateRequestDto;
import com.coopang.user.presentation.request.UserSearchCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "UserController API", description = "UserController API")
@Slf4j
@RestController
@RequestMapping("/users/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 가입
    @PostMapping("/join")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        final UserResponseDto user = userService.join(signupRequestDto);
        return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);
    }

    // 회원 정보 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable("userId") UUID userId) {
        final UserResponseDto userInfo = userService.getUserInfoById(userId);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    // 회원 정보 수정
    @Secured(UserRoleEnum.Authority.MASTER)
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateUserInfo(@PathVariable("userId") UUID userId, @RequestBody UpdateRequestDto updateRequestDto) {
        userService.updateUser(userId, updateRequestDto);
        final UserResponseDto userInfo = userService.getUserInfoById(userId);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    // 회원 삭제
    @Secured(UserRoleEnum.Authority.MASTER)
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID deleteUserId) {
        userService.deleteUser(deleteUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // 회원 목록 조회 엔드포인트
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping("/user")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(SearchRequestDto searchRequestDto) {
        Page<UserResponseDto> users = userService.getAllUsers(searchRequestDto);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    // 회원 검색 엔드포인트
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping("/user/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(UserSearchCondition userSearchCondition, SearchRequestDto searchRequestDto) {
        Page<UserResponseDto> users = userService.searchUsers(userSearchCondition, searchRequestDto);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/user/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto) {
        userService.changePassword(changePasswordRequestDto);
        return new ResponseEntity<>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);
    }

    @Secured(UserRoleEnum.Authority.MASTER)
    @PatchMapping("/user/block/{userId}")
    public ResponseEntity<String> block(@PathVariable("userId") UUID userId) {
        userService.blockUser(userId);
        return new ResponseEntity<>(userId + " 블록 처리가 완료 되었습니다.", HttpStatus.OK);
    }
}
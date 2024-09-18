package com.coopang.user.presentation.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

        UserResponseDto userInfo = userService.getUserInfo(userId);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    // 회원 정보 수정
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateUserInfo(@PathVariable("userId") UUID userId, @RequestBody UpdateRequestDto updateRequestDto) {
        userService.updateUserInfo(userId, updateRequestDto);
        UserResponseDto userInfo = userService.getUserInfo(userId);

//        try {
//            authService.updateRedisUserRole(userId, userInfo.getRole().toString());
//        } catch (Exception e) {
//            log.warn("Failed to update Redis cache for user role: {}", e.getMessage());
//        }

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID deleteUserId, @RequestHeader("X-User-Id") UUID loginUserId) {

        userService.deleteUser(deleteUserId);
//        try {
//            authService.deleteRedisUserRole(deleteUserId);
//            // 해당 유저의 토큰을 가져와서 블랙리스트 추가
//
//        } catch (Exception e) {
//            log.warn("Failed to delete user role from Redis cache: {}", e.getMessage());
//        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // 회원 목록 조회 엔드포인트
    @GetMapping("/user")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestHeader("X-Role") String role,
            SearchRequestDto searchRequestDto) {


        Page<UserResponseDto> users = userService.getAllUsers(searchRequestDto.getValidatedPage(), searchRequestDto.getValidatedSize(), searchRequestDto.getValidatedSortBy());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    // 회원 검색 엔드포인트
    @GetMapping("/user/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(
            @RequestHeader("X-Role") String role,
            UserSearchCondition userSearchCondition) {

        Page<UserResponseDto> users = userService.searchUsers(userSearchCondition);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/user/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto,
            @RequestHeader("X-User-Id") UUID loginUserId) {

        userService.changePasswordWithCheckPastPassword(loginUserId, changePasswordRequestDto);
        return new ResponseEntity<>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);
    }
}

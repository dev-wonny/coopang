package com.coopang.user.presentation.controller;


import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.UserPermissionValidator;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.application.request.AddressDto;
import com.coopang.user.application.request.ChangePasswordDto;
import com.coopang.user.application.request.MyInfoUpdateDto;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.application.response.UserResponseDto;
import com.coopang.user.application.service.UserService;
import com.coopang.user.presentation.request.ChangePasswordRequestDto;
import com.coopang.user.presentation.request.MyInfoUpdateRequestDto;
import com.coopang.user.presentation.request.NearHubRequestDto;
import com.coopang.user.presentation.request.NewPasswordRequestDto;
import com.coopang.user.presentation.request.RoleRequestDto;
import com.coopang.user.presentation.request.SignupRequestDto;
import com.coopang.user.presentation.request.SlackRequestDto;
import com.coopang.user.presentation.request.UpdateAddressRequestDto;
import com.coopang.user.presentation.request.UserInfoUpdateRequestDto;
import com.coopang.user.presentation.request.UserSearchCondition;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "UserController API", description = "UserController API")
@Slf4j(topic = "UserController")
@RestController
@RequestMapping("/users/v1")
public class UserController {
    private final UserService userService;
    private final ModelMapperConfig mapperConfig;

    public UserController(UserService userService, ModelMapperConfig mapperConfig) {
        this.userService = userService;
        this.mapperConfig = mapperConfig;
    }

    /**
     * 회원 가입을 처리합니다.
     *
     * @param signupRequestDto 회원 가입 요청 데이터
     * @return 가입된 회원의 이메일을 반환합니다.
     */
    @PostMapping("/join")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        final UserDto userDto = mapperConfig.strictMapper().map(signupRequestDto, UserDto.class);
        final UserResponseDto user = userService.join(userDto);
        return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);

    }

    /**
     * 본인 정보를 조회합니다.
     *
     * @param userIdHeader 요청 헤더에서 전달된 사용자 ID
     * @return 본인의 사용자 정보
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@RequestHeader("X-User-Id") String userIdHeader) {
        final UUID userId = UUID.fromString(userIdHeader);
        final UserResponseDto myInfo = userService.getMyInfo(userId);
        return new ResponseEntity<>(myInfo, HttpStatus.OK);

    }

    /**
     * 본인 정보를 수정합니다.
     *
     * @param userIdHeader           요청 헤더에서 전달된 사용자 ID
     * @param myInfoUpdateRequestDto 본인 정보 수정 요청 데이터
     * @return 수정된 본인 정보
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyInfo(@RequestHeader("X-User-Id") String userIdHeader, @RequestBody MyInfoUpdateRequestDto myInfoUpdateRequestDto) {
        final UUID userId = UUID.fromString(userIdHeader);
        final MyInfoUpdateDto myInfoUpdateDto = mapperConfig.strictMapper().map(myInfoUpdateRequestDto, MyInfoUpdateDto.class);
        userService.updateMyInfo(userId, myInfoUpdateDto);
        final UserResponseDto myInfo = userService.getMyInfo(userId);
        return new ResponseEntity<>(myInfo, HttpStatus.OK);

    }

    /**
     * 본인 비밀번호를 변경합니다.
     *
     * @param userIdHeader             요청 헤더에서 전달된 사용자 ID
     * @param changePasswordRequestDto 이전 비밀번호와 새로운 비밀번호
     */
    @PatchMapping("/me/change-password")
    public ResponseEntity<Void> changeMyPassword(@RequestHeader("X-User-Id") String userIdHeader, @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto) {
        final ChangePasswordDto changePasswordDto = mapperConfig.strictMapper().map(changePasswordRequestDto, ChangePasswordDto.class);
        userService.updatePasswordAfterValidation(UUID.fromString(userIdHeader), changePasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 관리자가 특정 사용자의 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 조회된 사용자 정보
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable("userId") UUID userId) {
        final UserResponseDto userInfo = userService.getUserInfoById(userId);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);

    }

    /**
     * 관리자가 모든 사용자를 조회합니다.
     *
     * @param pageable 페이지네이션 정보
     * @return 조회된 사용자 목록
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping("/user")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    /**
     * 관리자가 조건에 따라 사용자를 검색합니다.
     *
     * @param userSearchCondition 검색 조건
     * @param pageable            페이지네이션 정보
     * @return 검색된 사용자 목록
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping("/user/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsers(UserSearchCondition userSearchCondition, Pageable pageable) {
        Page<UserResponseDto> users = userService.searchUsers(userSearchCondition, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    /**
     * 관리자가 특정 사용자의 정보를 수정합니다.
     *
     * @param userId                   수정할 사용자의 ID
     * @param userInfoUpdateRequestDto 수정 요청 데이터
     * @return 수정된 사용자 정보
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateUserInfo(@PathVariable("userId") UUID userId, @RequestBody UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        final UserDto userDto = mapperConfig.strictMapper().map(userInfoUpdateRequestDto, UserDto.class);
        userService.updateUserInfo(userId, userDto);
        final UserResponseDto updatedUser = userService.getUserInfoById(userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    /**
     * 관리자가 특정 사용자의 비밀번호를 강제로 새로 설정합니다.
     *
     * @param userId                수정할 사용자의 ID
     * @param newPasswordRequestDto 새로운 비밀번호
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PatchMapping("/user/{userId}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable("userId") UUID userId, @RequestBody NewPasswordRequestDto newPasswordRequestDto) {
        userService.resetPassword(userId, newPasswordRequestDto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 관리자가 특정 사용자의 역할을 변경합니다.
     *
     * @param userId         수정할 사용자의 ID
     * @param roleRequestDto 새로운 역할 정보
     * @return 변경된 사용자 ID
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PatchMapping("/user/change-role/{userId}")
    public ResponseEntity<String> updateUserRole(@PathVariable("userId") UUID userId, @RequestBody RoleRequestDto roleRequestDto) {
        userService.updateUserRole(userId, roleRequestDto.getRole());
        return new ResponseEntity<>(userId.toString(), HttpStatus.OK);
    }

    /**
     * 슬랙 ID를 변경합니다.
     *
     * @param userId          사용자 ID
     * @param userIdHeader    요청 헤더에서 전달된 사용자 ID
     * @param roleHeader      요청 헤더에서 전달된 사용자 역할
     * @param slackRequestDto 슬랙 ID 요청 데이터
     * @return 상태 코드 OK
     */
    @PatchMapping("/user/change-slack/{userId}")
    public ResponseEntity<Void> updateSlackId(@PathVariable("userId") UUID userId,
                                              @RequestHeader("X-User-Id") String userIdHeader,
                                              @RequestHeader("X-User-Role") String roleHeader,
                                              @RequestBody SlackRequestDto slackRequestDto) {
        // MASTER 또는 본인 여부 확인
        UserPermissionValidator.validateMasterOrSelf(userId, userIdHeader, roleHeader);

        userService.updateSlackId(userId, slackRequestDto.getSlackId());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * 주소를 변경합니다.
     *
     * @param userId                  사용자 ID
     * @param userIdHeader            요청 헤더에서 전달된 사용자 ID
     * @param roleHeader              요청 헤더에서 전달된 사용자 역할
     * @param updateAddressRequestDto 주소 변경 요청 데이터
     * @return 변경된 사용자 정보
     */
    @PatchMapping("/user/change-address/{userId}")
    public ResponseEntity<UserResponseDto> updateAddress(@PathVariable("userId") UUID userId,
                                                         @RequestHeader("X-User-Id") String userIdHeader,
                                                         @RequestHeader("X-User-Role") String roleHeader,
                                                         @RequestBody UpdateAddressRequestDto updateAddressRequestDto) {
        // MASTER 또는 본인 여부 확인
        UserPermissionValidator.validateMasterOrSelf(userId, userIdHeader, roleHeader);

        final AddressDto addressDto = mapperConfig.strictMapper().map(updateAddressRequestDto, AddressDto.class);
        userService.updateAddress(userId, addressDto);
        final UserResponseDto updatedUser = userService.getUserInfoById(userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * 근처 허브 변경합니다.
     *
     * @param userId            사용자 ID
     * @param userIdHeader      요청 헤더에서 전달된 사용자 ID
     * @param roleHeader        요청 헤더에서 전달된 사용자 역할
     * @param nearHubRequestDto 변경 허브 ID
     */
    @PatchMapping("/user/change-nearHub/{userId}")
    public ResponseEntity<Void> updateNearHub(@PathVariable("userId") UUID userId,
                                              @RequestHeader("X-User-Id") String userIdHeader,
                                              @RequestHeader("X-User-Role") String roleHeader,
                                              @RequestBody NearHubRequestDto nearHubRequestDto) {
        // MASTER 또는 본인 여부 확인
        UserPermissionValidator.validateMasterOrSelf(userId, userIdHeader, roleHeader);

        userService.updateNearHub(userId, nearHubRequestDto.getNearHubId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 관리자가 특정 사용자를 차단합니다.
     *
     * @param userId 사용자 ID
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PatchMapping("/user/block/{userId}")
    public ResponseEntity<Void> block(@PathVariable("userId") UUID userId) {
        userService.blockUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * 관리자가 특정 사용자를 차단 해지합니다.
     *
     * @param userId 사용자 ID
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PatchMapping("/user/unblock/{userId}")
    public ResponseEntity<Void> unblock(@PathVariable("userId") UUID userId) {
        userService.unblockUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * 관리자가 특정 사용자를 삭제합니다.
     *
     * @param userId 사용자 ID
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
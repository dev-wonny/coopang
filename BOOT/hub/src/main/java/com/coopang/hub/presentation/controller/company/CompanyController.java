package com.coopang.hub.presentation.controller.company;


import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.apiconfig.constants.HeaderConstants.HEADER_USER_ROLE;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.hub.application.request.company.CompanyDto;
import com.coopang.hub.application.request.company.CompanySearchCondition;
import com.coopang.hub.application.response.company.CompanyResponseDto;
import com.coopang.hub.application.service.company.CompanyService;
import com.coopang.hub.application.service.hub.HubService;
import com.coopang.hub.application.validator.HubPermissionValidator;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import com.coopang.hub.presentation.request.company.CompanyAddressRequestDto;
import com.coopang.hub.presentation.request.company.CompanyHubIdRequestDto;
import com.coopang.hub.presentation.request.company.CompanyMangerIdRequestDto;
import com.coopang.hub.presentation.request.company.CompanyNameRequestDto;
import com.coopang.hub.presentation.request.company.CompanySearchConditionRequestDto;
import com.coopang.hub.presentation.request.company.CreateCompanyRequestDto;
import com.coopang.hub.presentation.request.company.UpdateCompanyRequestDto;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "CompanyController API", description = "CompanyController API")
@Slf4j(topic = "CompanyController")
@RestController
@RequestMapping("/companies/v1/company")
public class CompanyController {
    private final CompanyService companyService;
    private final HubService hubService;
    private final ModelMapperConfig mapperConfig;

    public CompanyController(CompanyService companyService, HubService hubService, ModelMapperConfig mapperConfig) {
        this.companyService = companyService;
        this.hubService = hubService;
        this.mapperConfig = mapperConfig;
    }

    /**
     * 생성
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody CreateCompanyRequestDto req
    ) {
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, req.getHubId(), UUID.fromString(userIdHeader), hubService);
        final CompanyDto companyDto = mapperConfig.strictMapper().map(req, CompanyDto.class);
        final CompanyResponseDto company = companyService.createCompany(companyDto);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    /**
     * 조회
     * 모두: 제한 없음
     * 마스터: 삭제 여부 선택 가능
     *
     * @param companyId
     * @param roleHeader
     * @return
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        CompanyResponseDto company;
        // 마스터면 delete도 보임, 그외 권한 delete 안 보임
        if (UserRoleEnum.isMaster(roleHeader)) {
            company = companyService.getCompanyById(companyId);
        } else {
            company = companyService.getValidCompanyById(companyId);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @Secured(UserRoleEnum.Authority.SERVER)
    @PostMapping("/list")
    public ResponseEntity<List<CompanyResponseDto>> getCompanyList(@RequestBody CompanySearchConditionRequestDto req) {
        final CompanySearchCondition condition = CompanySearchCondition.from(
                req.getCompanyId()
                , req.getHubId()
                , req.getCompanyManagerId()
                , req.getCompanyName()
                , req.getHubName()
                , req.getIsDeleted()
        );
        List<CompanyResponseDto> companyList = companyService.getCompanyList(condition);
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }

    /**
     * 검색
     * 모두: 제한 없음
     * 마스터: 삭제 여부 선택 가능
     *
     * @param roleHeader
     * @param req
     * @param pageable
     * @return
     */
    @PostMapping("/search")
    public ResponseEntity<Page<CompanyResponseDto>> searchCompanies(
            @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestBody CompanySearchConditionRequestDto req
            , Pageable pageable
    ) {
        // 마스터 권한이면 클라이언트에서 받은 isDeleted 값을 사용하고, 그외 권한은 삭제되지 않은 항목만 조회하도록 false로 설정
        final boolean isDeleted = UserRoleEnum.isMaster(roleHeader) ? req.getIsDeleted() : false;
        final CompanySearchCondition condition = CompanySearchCondition.from(
                req.getCompanyId()
                , req.getHubId()
                , req.getCompanyManagerId()
                , req.getCompanyName()
                , req.getHubName()
                , isDeleted
        );

        Page<CompanyResponseDto> companies = companyService.searchCompanies(condition, pageable);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    /**
     * 전체 수정
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody UpdateCompanyRequestDto req
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        final CompanyDto companyDto = mapperConfig.strictMapper().map(req, CompanyDto.class);
        companyService.updateCompany(companyId, companyDto);
        CompanyResponseDto updatedCompany = companyService.getValidCompanyById(companyId);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }

    /**
     * 소속 hubId 변경
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PatchMapping("/{companyId}/change-hub")
    public ResponseEntity<Void> changeHub(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestBody CompanyHubIdRequestDto req
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        companyService.changeHub(companyId, req.getHubId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * companyManagerId 변경
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PatchMapping("/{companyId}/change-manager")
    public ResponseEntity<Void> changeCompanyManager(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestBody CompanyMangerIdRequestDto req
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        companyService.changeCompanyManager(companyId, req.getCompanyManagerId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * companyName 변경
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PatchMapping("/{companyId}/change-name")
    public ResponseEntity<Void> changeCompanyName(
            @PathVariable UUID companyId,
            @RequestHeader(HEADER_USER_ID) String userIdHeader,
            @RequestHeader(HEADER_USER_ROLE) String roleHeader,
            @RequestBody CompanyNameRequestDto req
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        companyService.changeCompanyName(companyId, req.getCompanyName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 주소 변경
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @PatchMapping("/{companyId}/change-address")
    public ResponseEntity<Void> changeCompanyAddress(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody CompanyAddressRequestDto req
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        companyService.changeCompanyAddress(companyId, req.getZipCode(), req.getAddress1(), req.getAddress2());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 삭제
     *
     * @param companyId
     * @param userIdHeader
     * @param roleHeader
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable UUID companyId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        validateRoleHubManaagerAndCompanyAccess(companyId, UUID.fromString(userIdHeader), roleHeader);
        companyService.deleteCompany(companyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // 마스터, 허브매니저(자기 소속 허브 회사만 가능), 회사(companyManagerId 동일 해야 가능)


    /**
     * companyId 로 회사를 조회해서 소속 허브, 소속 회사 여부를 확인합니다.
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     * 회사: companyManagerId 동일 해야 가능
     *
     * @param companyId
     * @param userId
     * @param userRole
     */
    private void validateRoleHubManaagerAndCompanyAccess(UUID companyId, UUID userId, String userRole) {
        // 수정하려는 company 이전 정보 조회
        CompanyEntity companyEntity = companyService.findValidCompanyById(companyId);

        if (UserRoleEnum.isHubManager(userRole)) {
            // 허브 매니저일 경우, 소속 허브의 회사만 수정 가능
            HubPermissionValidator.verifyHubOfHubManager(companyEntity.getHubId(), userId, hubService);
        } else if (UserRoleEnum.isCompany(userRole)) {
            // 회사 소유자일 경우, 본인의 회사만 수정 가능
            companyService.validateCompanyOwner(companyEntity.getCompanyManagerId(), userId);
        }
    }
}

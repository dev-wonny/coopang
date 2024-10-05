package com.coopang.user;

import static com.coopang.apidata.application.user.enums.UserRoleEnum.Authority.MASTER;
import static org.assertj.core.api.Assertions.assertThat;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.user.QUserEntity;
import com.coopang.user.domain.entity.user.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
@Commit
class UserApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
        final String zipCode = "05510";
        final String address1 = "서울 송파구 송파대로 570";
        final String address2 = "8~26층 ";

        UserDto dto = new UserDto();
        dto.setEmail("email@naver.com");
        dto.setUserName("name");
        dto.setPhoneNumber("010-2222-2222");
        dto.setSlackId("slackId");
        String passwordEncode = "password";
        UserRoleEnum role = UserRoleEnum.getRoleEnum(MASTER);
        dto.setRole(role.getAuthority());
        dto.setNearHubId(UUID.fromString("121212"));

        UserEntity insertUser =
                UserEntity.create(null, dto.getEmail(), passwordEncode, dto.getUserName(), dto.getPhoneNumber(), dto.getSlackId(), dto.getRole(), zipCode, address1, address2, dto.getNearHubId());
        entityManager.persist(insertUser);

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QUserEntity qUser = QUserEntity.userEntity;

        UserEntity resultUser = query
                .selectFrom(qUser)
                .fetchOne();

        assertThat(resultUser).isEqualTo(insertUser);

        // lombok 작동되는지 체크
        assertThat(resultUser.getEmail()).isEqualTo(insertUser.getEmail());
    }

    @Test
    public void search() {

    }

}

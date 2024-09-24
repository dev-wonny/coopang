package com.coopang.user;

import static com.coopang.user.application.enums.UserRoleEnum.Authority.MASTER;
import static org.assertj.core.api.Assertions.assertThat;

import com.coopang.user.application.enums.UserRoleEnum;
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

@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
@Commit
class UserApplicationTests {

    @Autowired
    EntityManager entityManager;


    @Test
    void contextLoads() {

        UserDto dto = new UserDto();
        dto.setEmail("email@naver.com");
        dto.setUsername("name");
        dto.setPhoneNumber("010-2222-2222");
        dto.setSlackId("slackId");
        String passwordEncode = "password";
        UserRoleEnum role = UserRoleEnum.getRoleEnum(MASTER);
        dto.setRole(role.getAuthority());

        UserEntity insertUser = UserEntity.create(dto, passwordEncode);
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

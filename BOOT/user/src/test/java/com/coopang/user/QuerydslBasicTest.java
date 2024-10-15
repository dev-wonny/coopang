package com.coopang.user;

import static com.coopang.apidata.application.user.enums.UserRoleEnum.Authority.MASTER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.user.QUserEntity;
import com.coopang.user.domain.entity.user.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
//@Commit
class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;
    QUserEntity qUser;


    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        final String passwordEncode = "password";
        UserRoleEnum role = UserRoleEnum.getRoleEnum(MASTER);

        final String zipCode = "05510";
        final String address1 = "서울 송파구 송파대로 570";
        final String address2 = "8~26층 ";

        UserDto dto = new UserDto();
        dto.setEmail("email@naver.com");
        dto.setUserName("name");
        dto.setPhoneNumber("010-2222-2222");
        dto.setSlackId("slackId");
        dto.setRole(role.getAuthority());
        dto.setNearHubId(UUID.fromString("12121212"));

        UserDto dto1 = new UserDto();
        dto1.setEmail("email1@naver.com");
        dto1.setUserName("name1");
        dto1.setPhoneNumber("010-2222-2222");
        dto1.setSlackId("slackId1");
        dto1.setRole(role.getAuthority());
        dto1.setNearHubId(UUID.fromString("12121212"));


        UserDto dto2 = new UserDto();
        dto2.setEmail("email2@naver.com");
        dto2.setUserName("name2");
        dto2.setPhoneNumber("010-2222-2222");
        dto2.setSlackId("slackId2");
        dto2.setRole(role.getAuthority());
        dto2.setNearHubId(UUID.fromString("12121212"));


        UserDto dto3 = new UserDto();
        dto3.setEmail("email3@naver.com");
        dto3.setUserName("name3");
        dto3.setPhoneNumber("010-2222-2222");
        dto3.setSlackId("slackId3");
        dto3.setRole(role.getAuthority());
        dto3.setNearHubId(UUID.fromString("12121212"));


        UserEntity insertUser =
                UserEntity.create(null, dto.getEmail(), passwordEncode, dto.getUserName(), dto.getPhoneNumber(), dto.getSlackId(), dto.getRole(), zipCode, address1, address2, dto.getNearHubId());
        UserEntity insertUser1 = UserEntity.create(null, dto1.getEmail(), passwordEncode, dto1.getUserName(), dto1.getPhoneNumber(), dto1.getSlackId(), dto1.getRole(), zipCode, address1, address2,
                dto1.getNearHubId());
        UserEntity insertUser2 = UserEntity.create(null, dto2.getEmail(), passwordEncode, dto2.getUserName(), dto2.getPhoneNumber(), dto2.getSlackId(), dto2.getRole(), zipCode, address1, address2,
                dto2.getNearHubId());
        UserEntity insertUser3 = UserEntity.create(null, dto3.getEmail(), passwordEncode, dto3.getUserName(), dto3.getPhoneNumber(), dto3.getSlackId(), dto3.getRole(), zipCode, address1, address2,
                dto3.getNearHubId());


        em.persist(insertUser);
        em.persist(insertUser1);
        em.persist(insertUser2);
        em.persist(insertUser3);

        qUser = QUserEntity.userEntity;
    }

    @Test
    public void startJPQL() {
        String qlString =
                "select m from UserEntity m " +
                        "where m.userName = :userName";
        UserEntity findMember = em.createQuery(qlString, UserEntity.class)
                .setParameter("userName", "name")
                .getSingleResult();
        assertThat(findMember.getUserName()).isEqualTo("name");
    }

    @Test
    public void startQuerydsl() {
        UserEntity resultUser = queryFactory
                .select(qUser)
                .from(qUser)
                .where(qUser.userName.eq("name"))
                .fetchOne();
        assertThat(resultUser.getUserName()).isEqualTo("name");
    }

    @Test
    public void search() {
        UserEntity resultUser = queryFactory
                .selectFrom(qUser)
                .where(qUser.userName.eq("name2"))
                .fetchOne();

        assertThat(resultUser.getUserName()).isEqualTo("name2");
    }

}
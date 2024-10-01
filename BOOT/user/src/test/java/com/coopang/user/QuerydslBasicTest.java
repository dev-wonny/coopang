package com.coopang.user;

import static com.coopang.apidata.application.user.enums.UserRoleEnum.Authority.MASTER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
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
        dto.setUsername("name");
        dto.setPhoneNumber("010-2222-2222");
        dto.setSlackId("slackId");
        dto.setRole(role.getAuthority());

        UserDto dto1 = new UserDto();
        dto1.setEmail("email1@naver.com");
        dto1.setUsername("name1");
        dto1.setPhoneNumber("010-2222-2222");
        dto1.setSlackId("slackId1");
        dto1.setRole(role.getAuthority());

        UserDto dto2 = new UserDto();
        dto2.setEmail("email2@naver.com");
        dto2.setUsername("name2");
        dto2.setPhoneNumber("010-2222-2222");
        dto2.setSlackId("slackId2");
        dto2.setRole(role.getAuthority());


        UserDto dto3 = new UserDto();
        dto3.setEmail("email3@naver.com");
        dto3.setUsername("name3");
        dto3.setPhoneNumber("010-2222-2222");
        dto3.setSlackId("slackId3");
        dto3.setRole(role.getAuthority());


        UserEntity insertUser = UserEntity.create(dto.getEmail(), passwordEncode, dto.getUsername(), dto.getPhoneNumber(), dto.getSlackId(), dto.getRole(), zipCode, address1, address2);
        UserEntity insertUser1 = UserEntity.create(dto1.getEmail(), passwordEncode, dto1.getUsername(), dto1.getPhoneNumber(), dto1.getSlackId(), dto1.getRole(), zipCode, address1, address2);
        UserEntity insertUser2 = UserEntity.create(dto2.getEmail(), passwordEncode, dto2.getUsername(), dto2.getPhoneNumber(), dto2.getSlackId(), dto2.getRole(), zipCode, address1, address2);
        UserEntity insertUser3 = UserEntity.create(dto3.getEmail(), passwordEncode, dto3.getUsername(), dto3.getPhoneNumber(), dto3.getSlackId(), dto3.getRole(), zipCode, address1, address2);


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
                        "where m.username = :username";
        UserEntity findMember = em.createQuery(qlString, UserEntity.class)
                .setParameter("username", "name")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("name");
    }

    @Test
    public void startQuerydsl() {
        UserEntity resultUser = queryFactory
                .select(qUser)
                .from(qUser)
                .where(qUser.username.eq("name"))
                .fetchOne();
        assertThat(resultUser.getUsername()).isEqualTo("name");
    }

    @Test
    public void search() {
        UserEntity resultUser = queryFactory
                .selectFrom(qUser)
                .where(qUser.username.eq("name2"))
                .fetchOne();

        assertThat(resultUser.getUsername()).isEqualTo("name2");
    }

}
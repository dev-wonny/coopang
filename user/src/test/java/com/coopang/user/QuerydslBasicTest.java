package com.coopang.user;

import static com.coopang.user.application.enums.Authority.MASTER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.QUserEntity;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.presentation.request.SignupRequestDto;
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

        String passwordEncode = "password";
        UserRoleEnum role = UserRoleEnum.getRoleEnum(MASTER);


        SignupRequestDto dto = new SignupRequestDto();
        dto.setEmail("email@naver.com");
        dto.setUsername("name");
        dto.setPhoneNumber("010-2222-2222");
        dto.setSlackId("slackId");

        SignupRequestDto dto1 = new SignupRequestDto();
        dto1.setEmail("email1@naver.com");
        dto1.setUsername("name1");
        dto1.setPhoneNumber("010-2222-2222");
        dto1.setSlackId("slackId1");

        SignupRequestDto dto2 = new SignupRequestDto();
        dto2.setEmail("email2@naver.com");
        dto2.setUsername("name2");
        dto2.setPhoneNumber("010-2222-2222");
        dto2.setSlackId("slackId2");


        SignupRequestDto dto3 = new SignupRequestDto();
        dto3.setEmail("email3@naver.com");
        dto3.setUsername("name3");
        dto3.setPhoneNumber("010-2222-2222");
        dto3.setSlackId("slackId3");

        UserEntity insertUser = UserEntity.create(dto, passwordEncode, role);
        UserEntity insertUser1 = UserEntity.create(dto1, passwordEncode, role);
        UserEntity insertUser2 = UserEntity.create(dto2, passwordEncode, role);
        UserEntity insertUser3 = UserEntity.create(dto3, passwordEncode, role);


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
package com.coopang.user;

import com.coopang.user.infrastructure.repository.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
class UserJpaRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserJpaRepository userJpaRepository;
}

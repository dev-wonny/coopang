package com.coopang.ainoti.infrastructure.repository.slacknotification;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.domain.repository.slacknotification.SlackNotificationRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackNotificationJpaRepository extends JpaRepository<SlackNotificationEntity, UUID>,
    SlackNotificationRepository, SlackNotificationRepositoryCustom {

}

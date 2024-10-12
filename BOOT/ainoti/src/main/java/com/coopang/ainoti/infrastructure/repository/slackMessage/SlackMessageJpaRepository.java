package com.coopang.ainoti.infrastructure.repository.slackMessage;

import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.domain.repository.slackMessage.SlackMessageRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageJpaRepository extends JpaRepository<SlackMessageEntity, UUID>,
    SlackMessageRepository ,SlackMessageRepositoryCustom{

}

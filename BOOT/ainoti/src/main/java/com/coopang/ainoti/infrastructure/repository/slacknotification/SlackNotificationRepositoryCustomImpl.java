package com.coopang.ainoti.infrastructure.repository.slacknotification;


import static com.coopang.ainoti.domain.entitiy.slacknotification.QSlackMessageEntity.slackMessageEntity;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SlackNotificationRepositoryCustomImpl extends Querydsl4RepositorySupport implements
    SlackNotificationRepositoryCustom {

    public SlackNotificationRepositoryCustomImpl() {
        super(SlackNotificationEntity.class);
    }

    @Override
    public Page<SlackNotificationEntity> search(Pageable pageable) {
        return applyPagination(pageable,contentQuery ->
            contentQuery.selectFrom(slackMessageEntity)
                .where(slackMessageEntity.isDeleted.eq(false))
                ,
            countQuery -> countQuery.selectFrom(slackMessageEntity)
                .where(slackMessageEntity.isDeleted.eq(false))
                );
    }

    @Override
    public Page<SlackNotificationEntity> searchWithCondition(SlackMessageSearchConditionDto conditionDto,
        Pageable pageable) {

        return applyPagination(pageable,contentQuery ->
                contentQuery.selectFrom(slackMessageEntity)
                    .where(slackMessageEntity.isDeleted.eq(false),
                        betweenStartDateAndEndDate(conditionDto.getStartTime(),conditionDto.getEndTime()),
                        equalReceiveUserId(conditionDto.getReceiveUserId()),
                        containMessage(conditionDto.getMessage())
                        )
            ,
            countQuery -> countQuery.selectFrom(slackMessageEntity)
                .where(slackMessageEntity.isDeleted.eq(false),
                    betweenStartDateAndEndDate(conditionDto.getStartTime(),conditionDto.getEndTime()),
                    equalReceiveUserId(conditionDto.getReceiveUserId()),
                    containMessage(conditionDto.getMessage())
                )
        );
    }

    private Predicate containMessage(String message) {
        return message != null ? slackMessageEntity.slackMessage.contains(message) : null;
    }

    private Predicate equalReceiveUserId(UUID receiveUserId) {
        return receiveUserId != null ? slackMessageEntity.receiveUserId.eq(receiveUserId) : null;
    }

    private Predicate betweenStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {

        if(startDate==null && endDate==null) {
            return null;
        }

        if (startDate == null) {
            return slackMessageEntity.createdAt.before(endDate);
        }

        if(endDate==null) {

            return slackMessageEntity.createdAt.after(startDate);
        }

        return slackMessageEntity.createdAt.between(startDate, endDate);
    }
}

package com.coopang.ainoti.infrastructure.repository.noti;

import static com.coopang.ainoti.domain.entity.noti.QSlackMessageEntity.slackMessageEntity;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


@Repository
public class SlackMessageRepositoryCustomImpl extends Querydsl4RepositorySupport implements SlackMessageRepositoryCustom {

    public SlackMessageRepositoryCustomImpl() {
        super(SlackMessageEntity.class);
    }

    @Override
    public Page<SlackMessageEntity> search(SlackMessageSearchConditionDto condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(slackMessageEntity)
                .where(
                    receiveSlackIdEq(condition.getReceiveSlackId())
                    , receiveUserIdEq(condition.getReceiveUserId())
                    , slackMessageStatusEq(condition.getSlackMessageStatus())
                    , slackMessageContains(condition.getSlackMessage())
                    , slackMessageEntity.isDeleted.eq(condition.isDeleted())

                ),
            countQuery -> countQuery
                .selectFrom(slackMessageEntity)
                .where(
                    receiveSlackIdEq(condition.getReceiveSlackId())
                    , receiveUserIdEq(condition.getReceiveUserId())
                    , slackMessageStatusEq(condition.getSlackMessageStatus())
                    , slackMessageContains(condition.getSlackMessage())
                    , slackMessageEntity.isDeleted.eq(condition.isDeleted())

                )
        );
    }

    @Override
    public List<SlackMessageEntity> findSlackMessageList(SlackMessageSearchConditionDto condition) {
        return selectFrom(slackMessageEntity)
            .where(
                receiveSlackIdEq(condition.getReceiveSlackId())
                , receiveUserIdEq(condition.getReceiveUserId())
                , slackMessageStatusEq(condition.getSlackMessageStatus())
                , slackMessageContains(condition.getSlackMessage())
                , slackMessageEntity.isDeleted.eq(condition.isDeleted())

            )
            .fetch();
    }

    private Predicate receiveSlackIdEq(String receiveSlackId) {
        return StringUtils.hasText(receiveSlackId) ? slackMessageEntity.receiveSlackId.eq(receiveSlackId) : null;
    }

    private Predicate receiveUserIdEq(UUID receiveUserId) {
        return !ObjectUtils.isEmpty(receiveUserId) ? slackMessageEntity.receiveUserId.eq(receiveUserId) : null;
    }

    private Predicate slackMessageStatusEq(SlackMessageStatus slackMessageStatus) {
        return !ObjectUtils.isEmpty(slackMessageStatus) ? slackMessageEntity.slackMessageStatus.eq(slackMessageStatus) : null;
    }

    private Predicate slackMessageContains(String slackMessage) {
        return StringUtils.hasText(slackMessage) ? slackMessageEntity.slackMessage.contains(slackMessage) : null;
    }
}

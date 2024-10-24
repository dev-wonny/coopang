package com.coopang.ainoti.infrastructure.repository.noti;

import static com.coopang.ainoti.domain.entity.noti.QSlackMessageEntity.slackMessageEntity;

import com.coopang.apidata.application.noti.enums.SlackMessageStatus;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public class SlackMessageRepositoryCustomImpl extends Querydsl4RepositorySupport implements SlackMessageRepositoryCustom {

    public SlackMessageRepositoryCustomImpl() {
        super(SlackMessageEntity.class);
    }

    @Override
    public Page<SlackMessageEntity> search(SlackMessageSearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(slackMessageEntity)
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(slackMessageEntity)
                .where(
                    whereClause
                )
        );
    }

    @Override
    public List<SlackMessageEntity> findSlackMessageList(SlackMessageSearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(slackMessageEntity)
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(SlackMessageSearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(slackMessageIdEq(condition.getSlackMessageId()));
        whereClause.and(receiveSlackIdEq(condition.getReceiveSlackId()));
        whereClause.and(receiveUserIdEq(condition.getReceiveUserId()));
        whereClause.and(slackMessageStatusEq(condition.getSlackMessageStatus()));
        whereClause.and(slackMessageContains(condition.getSlackMessage()));
        whereClause.and(sentTimeFromGoe(condition.getSentTimeFrom()));
        whereClause.and(sentTimeToLoe(condition.getSentTimeTo()));
        whereClause.and(slackMessageEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate slackMessageIdEq(UUID slackMessageId) {
        return !ObjectUtils.isEmpty(slackMessageId) ? slackMessageEntity.slackMessageId.eq(slackMessageId) : null;
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

    private Predicate sentTimeFromGoe(LocalDateTime sentTimeFrom) {
        return !ObjectUtils.isEmpty(sentTimeFrom) ? slackMessageEntity.sentTime.goe(sentTimeFrom) : null;
    }

    private Predicate sentTimeToLoe(LocalDateTime sentTimeTo) {
        return !ObjectUtils.isEmpty(sentTimeTo) ? slackMessageEntity.sentTime.loe(sentTimeTo) : null;
    }
}

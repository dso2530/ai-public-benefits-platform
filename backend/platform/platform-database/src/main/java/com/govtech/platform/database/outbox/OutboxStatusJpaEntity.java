package com.govtech.platform.database.outbox;

public enum OutboxStatusJpaEntity {

    READY_TO_PUBLISH,

    PUBLISHING,

    MARKED_AS_PUBLISHED
}
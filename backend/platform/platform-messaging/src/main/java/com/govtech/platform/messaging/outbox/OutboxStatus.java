package com.govtech.platform.messaging.outbox;

public enum OutboxStatus {

    READY_TO_PUBLISH,

    PUBLISHING,

    MARKED_AS_PUBLISHED
}
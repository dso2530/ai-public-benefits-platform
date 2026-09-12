package com.govtech.platform.messaging.dlt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class DltReplayScheduler {

    private final DltReplayService replayService;

    @Scheduled(fixedDelayString = "${messaging.kafka.dlt.replay.fixed-delay:30000}")
    public void replay() {

        log.info("========== DLT REPLAY SCHEDULER ==========");

        replayService.replay();
    }
}
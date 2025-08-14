package com.myanime.application.jobs;

import com.myanime.domain.port.input.job.SyncAnimeUC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncAnimeCronJob {

    private final SyncAnimeUC syncAnimeUC;

    @Value(value = "${cron.sync-anime-disable}")
    private Boolean disable = Boolean.FALSE;

    @Scheduled(initialDelay = 1000 * 30, fixedDelay = Long.MAX_VALUE)
    public void runJob() {
        if (Boolean.TRUE.equals(disable)) {
            log.info("SyncAnimeCronJob is disabled, skipping execution.");
            return;
        }

        syncAnimeUC.run();
    }

}

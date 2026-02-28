package com.myanime.application.jobs;

import com.myanime.domain.port.input.job.SyncUC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SyncUserCronJob {

    private final SyncUC syncUC;

    public SyncUserCronJob(@Qualifier("syncUserService") SyncUC syncUC) {
        this.syncUC = syncUC;
    }

    @Value(value = "${cron.sync-user-disable}")
    private Boolean disable = Boolean.FALSE;

    @Scheduled(initialDelay = 1000 * 30, fixedDelay = Long.MAX_VALUE)
    public void runJob() {
        if (Boolean.TRUE.equals(disable)) {
            log.info("SyncUserCronJob is disabled, skipping execution.");
            return;
        }

        log.info("Starting user sync job...");
        syncUC.run();
        log.info("User sync job completed.");
    }
}


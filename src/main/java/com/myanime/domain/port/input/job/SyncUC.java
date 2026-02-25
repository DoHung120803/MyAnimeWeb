package com.myanime.domain.port.input.job;

/**
 * Common interface for all sync use cases to Elasticsearch
 * <p>
 * Implementations:
 * - SyncAnimeService: Syncs anime data to ES
 * - SyncUserService: Syncs user data to ES
 * </p>
 */
public interface SyncUC {
    /**
     * Run the sync operation
     */
    void run();
}


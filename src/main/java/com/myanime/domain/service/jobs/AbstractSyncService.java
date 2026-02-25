package com.myanime.domain.service.jobs;

import com.myanime.domain.port.input.job.SyncUC;
import com.myanime.domain.port.output.SyncableRepository;
import com.myanime.infrastructure.elasticsearch.Bulk;
import com.myanime.infrastructure.elasticsearch.ESDocument;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Abstract service for syncing data to Elasticsearch
 * This provides a template method pattern for sync operations
 *
 * @param <T> The model type to sync
 */
@Slf4j
public abstract class AbstractSyncService<T> implements SyncUC {

    private static final Integer LIMIT = 200;

    protected final SyncableRepository<T> repository;
    protected final ESDocument esDocument;

    protected AbstractSyncService(SyncableRepository<T> repository, ESDocument esDocument) {
        this.repository = repository;
        this.esDocument = esDocument;
    }

    /**
     * Get the Elasticsearch index name for this entity type
     */
    protected abstract String getIndexName();

    /**
     * Get the ID from the model
     */
    protected abstract String getId(T model);

    /**
     * Convert the model to a map for Elasticsearch indexing
     */
    protected abstract Map<String, Object> toIndexMap(T model);

    /**
     * Get the entity name for logging purposes
     */
    protected abstract String getEntityName();

    @Override
    public void run() {
        String minId = null;
        Bulk bulkRequest = new Bulk();

        while (true) {
            List<T> records = repository.findByMinIdAndLimit(minId, LIMIT);

            if (records.isEmpty()) {
                break; // No more data to process
            }

            log.info("Start syncing {} {} with minId: {}", records.size(), getEntityName(), minId);

            try {
                for (T item : records) {
                    String id = getId(item);
                    Map<String, Object> mapData = toIndexMap(item);

                    bulkRequest.addBulkRequestUpdate(
                            id,
                            getIndexName(),
                            mapData
                    );
                }

                minId = getId(records.getLast());
                esDocument.executeBulkRequest(bulkRequest.getBulkRequest());
                bulkRequest.clear();

            } catch (Exception e) {
                log.error(">>> Error syncing {} with minId: {}. Error: {}", getEntityName(), minId, e.getMessage(), e);
                Sentry.captureException(e);
            }
        }

        log.info("Syncing {} completed successfully.", getEntityName());
    }
}


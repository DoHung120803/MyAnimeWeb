package com.myanime.domain.port.output;

import java.util.List;

/**
 * Interface for repositories that support sync operations
 * @param <T> The model type
 */
public interface SyncableRepository<T> {
    /**
     * Find records by minimum ID with limit for pagination
     * @param minId The minimum ID to start from (null for first page)
     * @param limit Maximum number of records to return
     * @return List of records
     */
    List<T> findByMinIdAndLimit(String minId, Integer limit);
}


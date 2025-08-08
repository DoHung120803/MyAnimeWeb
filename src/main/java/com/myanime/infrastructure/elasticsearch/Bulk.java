package com.myanime.infrastructure.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.core.TimeValue;

import java.util.Map;

@Getter
@Setter
public class Bulk {
    private BulkRequest bulkRequest;

    public Bulk() {
        this.bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.NONE);
    }

    public UpdateRequest upsert(String id, String index, Map<String, Object> mapData) {
        mapData.remove("time");

        return new UpdateRequest(index, id)
                .doc(mapData)
                .upsert(mapData)
                .timeout(TimeValue.timeValueSeconds(1))
                .retryOnConflict(3);
    }

    public void addBulkRequestUpdate(String id, String index, Map<String, Object> mapData) {
        UpdateRequest request = upsert(id, index, mapData);
        bulkRequest.add(request);
    }

    public int size() {
        return bulkRequest.numberOfActions();
    }

    public void clear() {
        bulkRequest.requests().clear();
    }
}

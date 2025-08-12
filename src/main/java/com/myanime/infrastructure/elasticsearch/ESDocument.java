package com.myanime.infrastructure.elasticsearch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class ESDocument {

    private final RestHighLevelClient client;

    public void executeBulkRequest(BulkRequest bulkRequest) throws IOException {
        if (bulkRequest == null || bulkRequest.numberOfActions() == 0) {
            return;
        }

        BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        bulkRequest.requests().clear();

        if (responses.hasFailures()) {
            throw new ElasticsearchException("Bulk request failed: " + responses.buildFailureMessage());
        }
    }
}

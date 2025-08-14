package com.myanime.infrastructure.elasticsearch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

@Getter
@Setter
@RequiredArgsConstructor
public class ESDocument {

    private final RestHighLevelClient client;

    public ESDocument(String host, Integer port, String username, String password) {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "https"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        this.client = new RestHighLevelClient(builder);
    }

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

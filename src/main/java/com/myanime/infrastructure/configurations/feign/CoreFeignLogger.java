package com.myanime.infrastructure.configurations.feign;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

@Slf4j
public class CoreFeignLogger extends feign.Logger {

    private static final String MDC_CALL_TO_KEY = "call_to";
    private static final String MDC_REQUEST_KEY = "request";
    private static final String MDC_RESPONSE_TIME_KEY = "response_time";
    private static final String MDC_RESPONSE_STATUS_KEY = "response_status";

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        List<String> logs = new ArrayList<>();
        if (request.body() != null && (logLevel.ordinal() >= Level.FULL.ordinal())) {
            String bodyText =
                    request.charset() != null ? new String(request.body(), request.charset()) : null;
            logs.add(formatV2(configKey, "%s", bodyText != null ? bodyText : "Binary data"));

        }
        MDC.put(MDC_CALL_TO_KEY, request.httpMethod().name() + " " + request.url());
        MDC.put(MDC_REQUEST_KEY, String.join("\n", logs));
    }

    @Override
    protected Response logAndRebufferResponse(
            String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        List<String> logs = new ArrayList<>();
        MDC.put(MDC_RESPONSE_TIME_KEY, elapsedTime + "");
        MDC.put(MDC_RESPONSE_STATUS_KEY, response.status() + "");
        int status = response.status();

        try {
            int bodyLength = 0;
            if (response.body() != null && !(status == 204 || status == 205)) {
                // HTTP 204 No Content "...response MUST NOT include a message-body"
                // HTTP 205 Reset Content "...response MUST NOT include an entity"
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                bodyLength = bodyData.length;
                if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                    logs.add(formatV2(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data")));
                }
                log.info(String.join("\n", logs));
                return response.toBuilder().body(bodyData).build();
            } else {
                logs.add(format(configKey, "<--- END HTTP (%s-byte body)", bodyLength));
            }
            log.info(String.join("\n", logs));
            return response;
        } finally {
            cleanCtx();
        }
    }

    private void cleanCtx() {
        MDC.remove(MDC_CALL_TO_KEY);
        MDC.remove(MDC_REQUEST_KEY);
        MDC.remove(MDC_RESPONSE_TIME_KEY);
        MDC.remove(MDC_RESPONSE_STATUS_KEY);
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // Not using SLF4J's support for parameterized messages (even though it would be more efficient)
        // because it would
        // require the incoming message formats to be SLF4J-specific.
        log.info(format(configKey, format, args));
    }

    private String format(String configKey, String format, Object... args) {
        String tag = methodTag(configKey);

        // Combine tag with format string using a placeholder for tag
        return String.format("%s%s", tag, String.format(format, args));
    }

    private String formatV2(String format, Object... args) {
        return String.format(format, args);
    }
}

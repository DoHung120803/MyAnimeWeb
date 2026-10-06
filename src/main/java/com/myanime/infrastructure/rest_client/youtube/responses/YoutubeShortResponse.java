package com.myanime.infrastructure.rest_client.youtube.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeShortResponse {

    private List<Entry> entries;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {

        private Command command;

        public String getVideoId() {
            ReelWatchEndpoint endpoint = getReelWatchEndpoint();

            return endpoint != null
                    ? endpoint.getVideoId()
                    : null;
        }

        public String getTitle() {
            PlayerResponse playerResponse = getPlayerResponse();

            return playerResponse != null
                    && playerResponse.getVideoDetails() != null
                    ? playerResponse.getVideoDetails().getTitle()
                    : null;
        }

        public String getDescription() {
            PlayerResponse playerResponse = getPlayerResponse();

            return playerResponse != null
                    && playerResponse.getVideoDetails() != null
                    ? playerResponse.getVideoDetails().getShortDescription()
                    : null;
        }

        public String getCommentCount() {
            ReelItemWatchResponse response = getReelItemWatchResponse();

            if (response == null || response.getEngagementPanels() == null) {
                return null;
            }

            for (EngagementPanel panel : response.getEngagementPanels()) {

                if (panel.getEngagementPanelSectionListRenderer() == null) {
                    continue;
                }

                Header header =
                        panel.getEngagementPanelSectionListRenderer().getHeader();

                if (header == null) {
                    continue;
                }

                EngagementPanelTitleHeaderRenderer headerRenderer =
                        header.getEngagementPanelTitleHeaderRenderer();

                if (headerRenderer == null
                        || headerRenderer.getContextualInfo() == null
                        || headerRenderer.getContextualInfo().getRuns() == null
                        || headerRenderer.getContextualInfo().getRuns().isEmpty()) {
                    continue;
                }

                return headerRenderer
                        .getContextualInfo()
                        .getRuns()
                        .getFirst()
                        .getText();
            }

            return null;
        }

        public String getLikeCount() {
            PlayerResponse playerResponse = getPlayerResponse();

            if (playerResponse != null
                    && playerResponse.getMicroformat() != null
                    && playerResponse.getMicroformat()
                    .getPlayerMicroformatRenderer() != null) {

                return playerResponse
                        .getMicroformat()
                        .getPlayerMicroformatRenderer()
                        .getLikeCount();
            }

            return null;
        }

        public String getAuthorName() {
            PlayerResponse playerResponse = getPlayerResponse();

            if (playerResponse != null
                    && playerResponse.getMicroformat() != null
                    && playerResponse.getMicroformat()
                    .getPlayerMicroformatRenderer() != null) {

                return playerResponse
                        .getMicroformat()
                        .getPlayerMicroformatRenderer()
                        .getOwnerChannelName();
            }

            return null;
        }

        public String getCoverUrl() {
            ReelWatchEndpoint endpoint = getReelWatchEndpoint();

            if (endpoint == null
                    || endpoint.getThumbnail() == null
                    || endpoint.getThumbnail().getThumbnails() == null
                    || endpoint.getThumbnail().getThumbnails().isEmpty()) {
                return null;
            }

            return endpoint
                    .getThumbnail()
                    .getThumbnails()
                    .getFirst()
                    .getUrl();
        }

        public String getLengthSeconds() {
            PlayerResponse playerResponse = getPlayerResponse();

            return playerResponse != null
                    && playerResponse.getVideoDetails() != null
                    ? playerResponse.getVideoDetails().getLengthSeconds()
                    : null;
        }

        private ReelWatchEndpoint getReelWatchEndpoint() {
            return command != null
                    ? command.getReelWatchEndpoint()
                    : null;
        }

        private UnserializedPrefetchData getUnserializedPrefetchData() {
            ReelWatchEndpoint endpoint = getReelWatchEndpoint();

            return endpoint != null
                    ? endpoint.getUnserializedPrefetchData()
                    : null;
        }

        private PlayerResponse getPlayerResponse() {
            UnserializedPrefetchData data = getUnserializedPrefetchData();

            return data != null
                    ? data.getPlayerResponse()
                    : null;
        }

        private ReelItemWatchResponse getReelItemWatchResponse() {
            UnserializedPrefetchData data = getUnserializedPrefetchData();

            return data != null
                    ? data.getReelItemWatchResponse()
                    : null;
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Command {
        private ReelWatchEndpoint reelWatchEndpoint;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReelWatchEndpoint {

        private String videoId;

        private ThumbnailContainer thumbnail;

        private UnserializedPrefetchData unserializedPrefetchData;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnserializedPrefetchData {

        private PlayerResponse playerResponse;

        private ReelItemWatchResponse reelItemWatchResponse;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerResponse {
        private VideoDetails videoDetails;
        private Microformat microformat;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoDetails {
        private String videoId;
        private String title;
        private String shortDescription;
        private String lengthSeconds;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Microformat {
        private PlayerMicroformatRenderer playerMicroformatRenderer;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerMicroformatRenderer {
        private String likeCount;
        private String ownerChannelName;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReelItemWatchResponse {
        private List<EngagementPanel> engagementPanels;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EngagementPanel {
        private EngagementPanelSectionListRenderer engagementPanelSectionListRenderer;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EngagementPanelSectionListRenderer {
        private Header header;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private EngagementPanelTitleHeaderRenderer engagementPanelTitleHeaderRenderer;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EngagementPanelTitleHeaderRenderer {
        private ContextualInfo contextualInfo;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContextualInfo {
        private List<RunText> runs;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RunText {
        private String text;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThumbnailContainer {

        private List<ThumbnailItem> thumbnails;

        @JsonProperty("isOriginalAspectRatio")
        private Boolean isOriginalAspectRatio;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ThumbnailItem {

        private String url;
        private Integer width;
        private Integer height;
    }
}
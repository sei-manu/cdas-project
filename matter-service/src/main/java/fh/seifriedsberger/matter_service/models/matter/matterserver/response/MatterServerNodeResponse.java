package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MatterServerNodeResponse {
    @JsonProperty("node_id")
    private int nodeId;

    @JsonProperty("date_commissioned")
    private LocalDateTime dateCommissioned;

    @JsonProperty("last_interview")
    private LocalDateTime lastInterview;

    @JsonProperty("interview_version")
    private int interviewVersion;

    @JsonProperty("available")
    private boolean available;

    @JsonProperty("is_bridge")
    private boolean isBridge;

    @JsonProperty("attributes")
    private Map<String, Object> attributes;

    @JsonProperty("attribute_subscriptions")
    private List<Object> attributeSubscriptions;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public LocalDateTime getDateCommissioned() {
        return dateCommissioned;
    }

    public void setDateCommissioned(LocalDateTime dateCommissioned) {
        this.dateCommissioned = dateCommissioned;
    }

    public LocalDateTime getLastInterview() {
        return lastInterview;
    }

    public void setLastInterview(LocalDateTime lastInterview) {
        this.lastInterview = lastInterview;
    }

    public int getInterviewVersion() {
        return interviewVersion;
    }

    public void setInterviewVersion(int interviewVersion) {
        this.interviewVersion = interviewVersion;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isBridge() {
        return isBridge;
    }

    public void setBridge(boolean bridge) {
        isBridge = bridge;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Object> getAttributeSubscriptions() {
        return attributeSubscriptions;
    }

    public void setAttributeSubscriptions(List<Object> attributeSubscriptions) {
        this.attributeSubscriptions = attributeSubscriptions;
    }

    @Override
    public String toString() {
        return "MatterServerNodeResponse{" +
                "nodeId=" + nodeId +
                ", attributesCount=" + attributes.size() +
                '}';
    }
}

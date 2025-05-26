package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// TODO maybe make a generic Event Response (if needed?)
public class MatterServerAttributeUpdatedEvent extends MatterServerResponse {

    private EventType eventType;
    private int nodeId;
    private String attributePath;
    private Object value;

    @JsonCreator
    public MatterServerAttributeUpdatedEvent(
            @JsonProperty("event") String event,
            @JsonProperty("data") List<Object> data
    ) {
        super(ServerResponseType.EVENT);

        this.eventType = EventType.fromValue(event);
        if (data != null && data.size() == 3) {
            this.nodeId = (int) data.get(0); // or convert via Number
            this.attributePath = (String) data.get(1);
            this.value = data.get(2); // could be any type
        }
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getNodeId() {
        return nodeId;
    }

    public String getAttributePath() {
        return attributePath;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EventResponse{" +
                "event='" + eventType + '\'' +
                ", nodeId=" + nodeId +
                ", attributePath='" + attributePath + '\'' +
                ", value=" + value +
                '}';
    }
}

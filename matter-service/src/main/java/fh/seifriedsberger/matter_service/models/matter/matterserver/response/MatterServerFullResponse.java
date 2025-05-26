package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatterServerFullResponse extends MatterServerResponse {

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("result")
    @JsonDeserialize(using = MatterServerNodeListDeserializer.class)
    private List<MatterServerNodeResponse> result;

    public MatterServerFullResponse() {
        super(ServerResponseType.RESULT);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<MatterServerNodeResponse> getResult() {
        return result;
    }

    public void setResult(List<MatterServerNodeResponse> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MatterServerFullResponse{" +
                "messageId='" + messageId + '\'' +
                ", result=" + result +
                '}';
    }
}



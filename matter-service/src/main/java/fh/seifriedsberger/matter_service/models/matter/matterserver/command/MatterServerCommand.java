package fh.seifriedsberger.matter_service.models.matter.matterserver.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatterServerCommand {
    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("command")
    private String command;

    @JsonProperty("args")
    private Map<String, Object> args;

    public MatterServerCommand(APICommand apiCommand, String messageId) {
        this.command = apiCommand.getValue();
        this.messageId = messageId;
        this.args = new HashMap<>();
    }

    public MatterServerCommand withArg(String key, Object value) {
        this.args.put(key, value);
        return this;
    }

    public MatterServerCommand withArgs(Map<String, Object> args) {
        this.args.putAll(args);
        return this;
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getCommand() { return command; }
    public Map<String, Object> getArgs() { return args; }
}

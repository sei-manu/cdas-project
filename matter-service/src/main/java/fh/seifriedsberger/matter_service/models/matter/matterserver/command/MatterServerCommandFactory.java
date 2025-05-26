package fh.seifriedsberger.matter_service.models.matter.matterserver.command;

import java.util.HashMap;
import java.util.Map;

public class MatterServerCommandFactory {
    public static MatterServerCommand commissionWithCode(String messageId, String code, boolean networkOnly) {
        return new MatterServerCommand(APICommand.COMMISSION_WITH_CODE, messageId)
                .withArg("code", code)
                .withArg("network_only", networkOnly);
    }

    public static MatterServerCommand toggle(String messageId, int nodeId, String attributePath) {

        var splitted = attributePath.split("/");

        // TODO check if attributePath is of correct format (x/x/x)

        return new MatterServerCommand(APICommand.DEVICE_COMMAND, messageId)
                .withArgs(Map.of(
                        "endpoint_id", splitted[0],
                        "node_id", nodeId,
                        "payload", new HashMap<>(),
                        "cluster_id", splitted[1],
                        "command_name", "Toggle"
                ));
    }
}


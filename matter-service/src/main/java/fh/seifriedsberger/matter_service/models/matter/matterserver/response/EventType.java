package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

public enum EventType {
    NODE_ADDED("node_added"),
    NODE_UPDATED("node_updated"),
    NODE_REMOVED("node_removed"),
    NODE_EVENT("node_event"),
    ATTRIBUTE_UPDATED("attribute_updated"),
    SERVER_SHUTDOWN("server_shutdown"),
    SERVER_INFO_UPDATED("server_info_updated"),
    ENDPOINT_ADDED("endpoint_added"),
    ENDPOINT_REMOVED("endpoint_removed");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType fromValue(String value) throws IllegalArgumentException {
        for (EventType type : EventType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public String getValue() {
        return value;
    }
}

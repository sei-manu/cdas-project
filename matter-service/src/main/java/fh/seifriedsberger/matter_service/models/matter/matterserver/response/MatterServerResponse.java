package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

public abstract class MatterServerResponse {
    private ServerResponseType serverResponseType;

    public MatterServerResponse(ServerResponseType serverResponseType) {
        this.serverResponseType = serverResponseType;
    }

    public ServerResponseType getServerResponseType() {
        return serverResponseType;
    }
}



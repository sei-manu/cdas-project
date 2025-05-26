package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatterServerStatusResponse extends MatterServerResponse {
    @JsonProperty("fabric_id")
    private long fabricId;

    @JsonProperty("compressed_fabric_id")
    private long compressedFabricId;

    @JsonProperty("schema_version")
    private int schemaVersion;

    @JsonProperty("min_supported_schema_version")
    private int minSupportedSchemaVersion;

    @JsonProperty("sdk_version")
    private String sdkVersion;

    @JsonProperty("wifi_credentials_set")
    private boolean wifiCredentialsSet;

    @JsonProperty("thread_credentials_set")
    private boolean threadCredentialsSet;

    @JsonProperty("bluetooth_enabled")
    private boolean bluetoothEnabled;

    public MatterServerStatusResponse() {
        super(ServerResponseType.STATUS);
    }

    public long getFabricId() {
        return fabricId;
    }

    public void setFabricId(long fabricId) {
        this.fabricId = fabricId;
    }

    public long getCompressedFabricId() {
        return compressedFabricId;
    }

    public void setCompressedFabricId(long compressedFabricId) {
        this.compressedFabricId = compressedFabricId;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public int getMinSupportedSchemaVersion() {
        return minSupportedSchemaVersion;
    }

    public void setMinSupportedSchemaVersion(int minSupportedSchemaVersion) {
        this.minSupportedSchemaVersion = minSupportedSchemaVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public boolean isWifiCredentialsSet() {
        return wifiCredentialsSet;
    }

    public void setWifiCredentialsSet(boolean wifiCredentialsSet) {
        this.wifiCredentialsSet = wifiCredentialsSet;
    }

    public boolean isThreadCredentialsSet() {
        return threadCredentialsSet;
    }

    public void setThreadCredentialsSet(boolean threadCredentialsSet) {
        this.threadCredentialsSet = threadCredentialsSet;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        this.bluetoothEnabled = bluetoothEnabled;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "fabricId=" + fabricId +
                ", compressedFabricId=" + compressedFabricId +
                ", schemaVersion=" + schemaVersion +
                ", minSupportedSchemaVersion=" + minSupportedSchemaVersion +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", wifiCredentialsSet=" + wifiCredentialsSet +
                ", threadCredentialsSet=" + threadCredentialsSet +
                ", bluetoothEnabled=" + bluetoothEnabled +
                '}';
    }
}

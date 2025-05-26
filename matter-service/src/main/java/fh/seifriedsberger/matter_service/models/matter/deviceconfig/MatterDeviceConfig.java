package fh.seifriedsberger.matter_service.models.matter.deviceconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatterDeviceConfig {
    private String id;
    private String name;
    private final List<MatterSource> sources;
    private final List<String> vendorRestricted;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MatterDeviceConfig(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("sources") List<MatterSource> sources,
            @JsonProperty("vendorRestricted") List<String> vendorRestricted
    ) {
        this.id = id;
        this.name = name;
        this.sources = sources;
        this.vendorRestricted = vendorRestricted;
    }
}

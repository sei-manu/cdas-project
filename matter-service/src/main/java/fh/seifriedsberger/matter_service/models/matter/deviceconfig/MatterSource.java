package fh.seifriedsberger.matter_service.models.matter.deviceconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatterSource {

    private final String category;
    private String id;
    private final List<MatterDataPoint> datapoints;
    private final List<MatterControlPoint> controlpoints;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MatterSource(
            @JsonProperty("category") String category,
            @JsonProperty("id") String id,
            @JsonProperty("datapoints") List<MatterDataPoint> datapoints,
            @JsonProperty("controlpoints") List<MatterControlPoint> controlpoints
    ) {
        this.category = category;
        this.id = id;
        this.datapoints = datapoints;
        this.controlpoints = controlpoints;
    }
}



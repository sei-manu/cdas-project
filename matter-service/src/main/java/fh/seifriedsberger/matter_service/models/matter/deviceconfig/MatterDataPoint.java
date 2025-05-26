package fh.seifriedsberger.matter_service.models.matter.deviceconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatterDataPoint {
    private String id;
    private final String attributePath;
    private final String description;
    private final String unitOfMeasurement;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MatterDataPoint(
            @JsonProperty("id") String id,
            @JsonProperty("attributePath") String attributePath,
            @JsonProperty("description") String description,
            @JsonProperty("unitOfMeasurement") String unitOfMeasurement
    ) {
        this.id = id;
        this.attributePath = attributePath;
        this.description = description;
        this.unitOfMeasurement = unitOfMeasurement;
    }
}


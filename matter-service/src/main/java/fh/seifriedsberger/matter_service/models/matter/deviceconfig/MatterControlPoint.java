package fh.seifriedsberger.matter_service.models.matter.deviceconfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatterControlPoint {
    private String id;
    private final String attributePath;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MatterControlPoint(
            @JsonProperty("id") String id,
            @JsonProperty("attributePath") String attributePath
    ) {
        this.id = id;
        this.attributePath = attributePath;
    }
}


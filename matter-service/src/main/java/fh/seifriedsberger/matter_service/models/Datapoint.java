package fh.seifriedsberger.matter_service.models;


import lombok.Data;

import java.util.UUID;

@Data
public class Datapoint {
    private UUID id;
    private String attributePath;
    private String description;
    private String type;
    private String unitOfMeasurement;
    private UUID datasourceId;

    public Datapoint(String attributePath, String description, String type, String unitOfMeasurement) {
        this.attributePath = attributePath;
        this.description = description;
        this.type = type;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Datapoint() {

    }
}

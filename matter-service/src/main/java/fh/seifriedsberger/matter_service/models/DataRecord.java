package fh.seifriedsberger.matter_service.models;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class DataRecord {
    private Long id;
    private Instant timestamp;
    private Double numericValue;
    private String stringValue;
    private String unitOfMeasurement;
    private String attributeIdentifier;
    private UUID deviceId;
    private int nodeId;
}

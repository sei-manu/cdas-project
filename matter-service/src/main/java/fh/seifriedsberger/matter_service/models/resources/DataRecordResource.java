package fh.seifriedsberger.matter_service.models.resources;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DataRecordResource(Long id, OffsetDateTime timestamp, Double numericValue, String stringValue, String unitOfMeasurement, UUID deviceId, String attributePath) {}

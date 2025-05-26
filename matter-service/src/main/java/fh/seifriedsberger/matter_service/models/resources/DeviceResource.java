package fh.seifriedsberger.matter_service.models.resources;


import java.util.List;
import java.util.UUID;

public record DeviceResource (UUID id, String name, String type, List<DatapointResource> datapoints){
}

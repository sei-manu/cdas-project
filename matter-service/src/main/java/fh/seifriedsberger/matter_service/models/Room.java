package fh.seifriedsberger.matter_service.models;

import fh.seifriedsberger.matter_service.models.entites.RoomEntity;
import fh.seifriedsberger.matter_service.models.resources.DeviceResource;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Room {
    private UUID id;
    private String name;
    private String description;

    private List<Datasource> datasources;
}

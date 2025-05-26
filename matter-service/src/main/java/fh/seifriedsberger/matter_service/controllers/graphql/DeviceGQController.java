package fh.seifriedsberger.matter_service.controllers.graphql;


import fh.seifriedsberger.matter_service.models.Datasource;
import fh.seifriedsberger.matter_service.models.resources.DeviceResource;
import fh.seifriedsberger.matter_service.models.resources.RoomResource;
import fh.seifriedsberger.matter_service.services.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.BatchMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DeviceGQController {

    private final DatasourceService datasourceService;

    @Autowired
    public DeviceGQController(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

//    @SchemaMapping(typeName = "Room", field = "devices")
//    public List<DeviceResource> devices(RoomResource room) {
//        return datasourceService.getDatasourcesByRoomId(room.id()).stream()
//                .map(this::datasourceToResource)
//                .toList();
//    }

    @BatchMapping(typeName = "Room", field = "devices")
    public Map<RoomResource, List<DeviceResource>> devices(List<RoomResource> rooms) {
        List<UUID> roomIds = rooms.stream().map(RoomResource::id).toList();

        Map<UUID, List<Datasource>> groupedDatasources = datasourceService.getDatasourcesGroupedByRoomIds(roomIds);

        return rooms.stream().collect(Collectors.toMap(
                room -> room,
                room -> groupedDatasources.getOrDefault(room.id(), List.of())
                        .stream()
                        .map(this::datasourceToResource)
                        .toList()
        ));
    }

    private DeviceResource datasourceToResource(Datasource datasource) {
        return new DeviceResource(
                datasource.getId(),
                datasource.getName(),
                datasource.getType(),
                null
        );
    }
}

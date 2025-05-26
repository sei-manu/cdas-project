package fh.seifriedsberger.matter_service.models;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Datasource {
    private UUID id;
    private int nodeId;
    private String name;
    private String type;
    private UUID roomId;
    private List<Datapoint> datapoints;

    private Map<String, String> functionalityMap;

//    public DeviceResource toResource() {
//        return new DeviceResource(id, name, type, room.getId());
//    }
//
//    public DatasourceEntity toEntity() {
//        DatasourceEntity entity = new DatasourceEntity();
//        entity.setId(id);
//        entity.setNodeId(nodeId);
//        entity.setName(name);
//        entity.setType(type);
//        entity.setFunctionalityMap(functionalityMap);
//        entity.setRoom(room.toEntity());
//        return entity;
//    }

}

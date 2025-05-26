package fh.seifriedsberger.matter_service.controllers.graphql;


import fh.seifriedsberger.matter_service.models.Datapoint;
import fh.seifriedsberger.matter_service.models.resources.DatapointResource;
import fh.seifriedsberger.matter_service.models.resources.DeviceResource;
import fh.seifriedsberger.matter_service.services.DatapointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DatapointGQController {

    private final DatapointService datapointService;

    @Autowired
    public DatapointGQController(DatapointService datapointService) {
        this.datapointService = datapointService;
    }

    @BatchMapping(typeName = "Device", field = "datapoints")
    public Map<DeviceResource, List<DatapointResource>> datapoints(List<DeviceResource> deviceResources) {
        List<UUID> deviceIds = deviceResources.stream()
                .map(DeviceResource::id)
                .toList();

        Map<UUID, List<Datapoint>> grouped = datapointService.getDatapointsGroupedByDatasourceIds(deviceIds);

        return deviceResources.stream()
                .collect(Collectors.toMap(
                        device -> device,
                        device -> grouped.getOrDefault(device.id(), List.of()).stream()
                                .map(d -> new DatapointResource(
                                        d.getId(),
                                        d.getAttributePath(),
                                        d.getDescription(),
                                        d.getType(),
                                        d.getUnitOfMeasurement()
                                ))
                                .toList()
                ));
    }

}

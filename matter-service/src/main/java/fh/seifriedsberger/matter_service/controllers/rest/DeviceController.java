package fh.seifriedsberger.matter_service.controllers.rest;

import fh.seifriedsberger.matter_service.models.Datasource;
import fh.seifriedsberger.matter_service.models.dtos.CommissionDeviceDTO;
import fh.seifriedsberger.matter_service.models.matter.deviceconfig.MatterDeviceConfig;
import fh.seifriedsberger.matter_service.models.resources.DatapointResource;
import fh.seifriedsberger.matter_service.models.resources.DeviceResource;
import fh.seifriedsberger.matter_service.models.resources.MatterDeviceConfigResource;
import fh.seifriedsberger.matter_service.services.DatasourceService;
import fh.seifriedsberger.matter_service.services.MatterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController()
@RequestMapping("/api/devices")
public class DeviceController {

    private final DatasourceService datasourceService;

    private final MatterConfigService matterConfigService;

    @Autowired
    public DeviceController(DatasourceService datasourceService, MatterConfigService matterConfigService) {
        this.datasourceService = datasourceService;
        this.matterConfigService = matterConfigService;
    }

    @PostMapping
    public void commissionDevice(@RequestBody CommissionDeviceDTO commissionDeviceDTO) {
        var device = this.datasourceService.commissionDevice(commissionDeviceDTO);
//        if (device == null) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Device could not be commissioned");
//        }
//        return datasourceToResource(device);
    }

    @GetMapping
    public List<DeviceResource> getDevices(@RequestParam("type") Optional<String> type) {
        return this.datasourceService.getAllDatasources().stream().map(this::datasourceToResource).toList();
    }

    @GetMapping("/supported-device-types")
    public List<MatterDeviceConfigResource> getDeviceTypes() {
        return this.matterConfigService.getAllConfigs().stream().map(this::deviceConfigToResource).toList();
    }

    @GetMapping("/{id}")
    public DeviceResource getDeviceById(@PathVariable("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            var ds = datasourceService.getDatasourceById(uuid);
            return datasourceToResource(ds);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is not in correct format");
        }
    }

    @GetMapping("/room/{roomId}")
    public List<DeviceResource> getDevicesByRoomId(@PathVariable("roomId") String roomId) {
        try {
            var uuid = UUID.fromString(roomId);
            return this.datasourceService.getDatasourcesByRoomId(uuid).stream().map(this::datasourceToResource).toList();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RoomID is not in correct format");
        }
    }

    @GetMapping("/types/{typeId}")
    public List<DeviceResource> getDevicesByType(@PathVariable("typeId") String typeId) {
        if (this.matterConfigService.typeExists(typeId)) {
            var name = this.matterConfigService.getTypeName(typeId);
            return this.datasourceService.getAllDatasources().stream()
                    .filter(ds -> ds.getType().equalsIgnoreCase(name))
                    .map(this::datasourceToResource)
                    .toList();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device Type " + typeId + " is not supported");
        }

    }

    private MatterDeviceConfigResource deviceConfigToResource(MatterDeviceConfig matterDeviceConfig) {
        return new MatterDeviceConfigResource(
                matterDeviceConfig.getId(),
                matterDeviceConfig.getName()
        );
    }

    private DeviceResource datasourceToResource(Datasource datasource) {
        return new DeviceResource(
                datasource.getId(),
                datasource.getName(),
                datasource.getType(),
                datasource.getDatapoints().stream()
                        .map(d -> new DatapointResource(d.getId(), d.getAttributePath(), d.getDescription(), d.getType(), d.getUnitOfMeasurement()))
                        .toList()
        );
    }
}

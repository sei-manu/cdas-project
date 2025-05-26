package fh.seifriedsberger.matter_service.services;

import fh.seifriedsberger.matter_service.models.Datapoint;
import fh.seifriedsberger.matter_service.models.Datasource;
import fh.seifriedsberger.matter_service.models.dtos.CommissionDeviceDTO;
import fh.seifriedsberger.matter_service.models.entites.DatasourceEntity;
import fh.seifriedsberger.matter_service.models.entites.RoomEntity;

import fh.seifriedsberger.matter_service.models.matter.matterserver.command.MatterServerCommandFactory;
import fh.seifriedsberger.matter_service.repositories.DatasourceRepository;
import fh.seifriedsberger.matter_service.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;

@Service
public class DatasourceService {

    private final DatasourceRepository datasourceRepository;

    private final DatapointService datapointService;

    private final MatterServerClient matterClient;

    @Autowired
    public DatasourceService(DatasourceRepository datasourceRepository, DatapointService dataPointService, MatterServerClient matterClient) {
        this.datasourceRepository = datasourceRepository;
        this.datapointService = dataPointService;
        this.matterClient = matterClient;
    }

    public Datasource commissionDevice(CommissionDeviceDTO commissionDeviceDTO) {
        // Send to Matter Service
        matterClient.sendCommand(MatterServerCommandFactory.commissionWithCode("commission-with-code", commissionDeviceDTO.commissionCode(), true));

        return null;

    }

    public List<Datasource> getAllDatasources() {

        return datasourceRepository.findAll().stream().map(entity ->
                        entityToDatasource(entity, datapointService.getDatapointsByDatasourceId(entity.getId())))
                .toList();
    }

    public Datasource getDatasourceById(UUID datasourceId) {
        var entity = datasourceRepository.findById(datasourceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID " + datasourceId + " was not found"));
        return entityToDatasource(entity, datapointService.getDatapointsByDatasourceId(datasourceId));
    }

    public List<Datasource> getDatasourcesByRoomId(UUID roomId) {
        // to not get nested too much do not return datapoints
        return datasourceRepository.findByRoomId(roomId).stream().map(entity -> entityToDatasource(entity, List.of())).toList();
    }

    public Map<UUID, List<Datasource>> getDatasourcesGroupedByRoomIds(List<UUID> roomIds) {
        return datasourceRepository.findByRoomIdIn(roomIds)
                .stream()
                .map(this::entityToDatasource)
                .collect(Collectors.groupingBy(Datasource::getRoomId));
    }

    private DatasourceEntity datasourceToEntity(Datasource datasource, RoomEntity roomEntity) {
        DatasourceEntity entity = new DatasourceEntity();
        entity.setId(datasource.getId());
        entity.setNodeId(datasource.getNodeId());
        entity.setName(datasource.getName());
        entity.setType(datasource.getType());
        entity.setFunctionalityMap(datasource.getFunctionalityMap());
        entity.setRoom(roomEntity);
        return entity;
    }

    private Datasource entityToDatasource(DatasourceEntity entity, List<Datapoint> datapoints) {
        Datasource datasource = new Datasource();
        datasource.setId(entity.getId());
        datasource.setNodeId(entity.getNodeId());
        datasource.setName(entity.getName());
        datasource.setType(entity.getType());
        datasource.setFunctionalityMap(entity.getFunctionalityMap());
        datasource.setDatapoints(datapoints);
        datasource.setRoomId(entity.getRoom().getId());
        return datasource;
    }

    private Datasource entityToDatasource(DatasourceEntity entity) {
        Datasource datasource = new Datasource();
        datasource.setId(entity.getId());
        datasource.setNodeId(entity.getNodeId());
        datasource.setName(entity.getName());
        datasource.setType(entity.getType());
        datasource.setFunctionalityMap(entity.getFunctionalityMap());
        datasource.setRoomId(entity.getRoom().getId());
        return datasource;
    }
}

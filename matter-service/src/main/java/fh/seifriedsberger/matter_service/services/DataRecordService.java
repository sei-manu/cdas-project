package fh.seifriedsberger.matter_service.services;


import fh.seifriedsberger.matter_service.models.DataRecord;
import fh.seifriedsberger.matter_service.models.entites.DataRecordEntity;
import fh.seifriedsberger.matter_service.models.matter.matterserver.command.APICommand;
import fh.seifriedsberger.matter_service.models.matter.matterserver.command.MatterServerCommand;
import fh.seifriedsberger.matter_service.repositories.DataRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service()
public class DataRecordService {

    private final MatterServerClient matterServerClient;

    private final DataRecordRepository dataRecordRepository;

    private final DatasourceService datasourceService;

    @Autowired
    public DataRecordService(MatterServerClient matterServerClient, DataRecordRepository dataRecordRepository, DatasourceService datasourceService) {
        this.matterServerClient = matterServerClient;
        this.dataRecordRepository = dataRecordRepository;
        this.datasourceService = datasourceService;
    }

    public void pollAllDevices() {
        matterServerClient.sendCommand(new MatterServerCommand(APICommand.GET_NODES, "poll-all-devices"));
    }

    public void pollDevice(UUID deviceId) {
        var datasource = datasourceService.getDatasourceById(deviceId);
        var nodeId = datasource.getNodeId();

        var command = new MatterServerCommand(APICommand.GET_NODE, "poll-device-" + nodeId).withArg("node_id", nodeId);

        matterServerClient.sendCommand(command);
    }

    public List<DataRecord> getDataRecordsForDevices(List<UUID> deviceIds,
                                                     Optional<OffsetDateTime> from,
                                                     Optional<OffsetDateTime> to) {
        Specification<DataRecordEntity> spec = Specification.where((root, query, cb) ->
                root.get("datapoint").get("datasource").get("id").in(deviceIds));

        if (from.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("timestamp"), from.get()));
        }

        if (to.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("timestamp"), to.get()));
        }

        return dataRecordRepository.findAll(spec).stream()
                .map(this::entityToModel)
                .toList();
    }

    public List<DataRecord> getDataRecordsForDatapoints(List<String> attributePaths,
                                                        Optional<OffsetDateTime> from,
                                                        Optional<OffsetDateTime> to) {
        Specification<DataRecordEntity> spec = Specification.where((root, query, cb) ->
                root.get("datapoint").get("attributePath").in(attributePaths));

        if (from.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("timestamp"), from.get()));
        }

        if (to.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("timestamp"), to.get()));
        }

        return dataRecordRepository.findAll(spec).stream()
                .map(this::entityToModel)
                .toList();
    }


    public List<DataRecord> getDataRecords(Optional<UUID> deviceId, Optional<UUID> datapointId, Optional<OffsetDateTime> from, Optional<OffsetDateTime> to) {
        Specification<DataRecordEntity> spec = Specification.where(null);

        if (deviceId.isPresent()) {
            spec.and((root, query, cb) ->
                    cb.equal(root.get("datapoint").get("device").get("id"), deviceId.get()));
        }

        if (datapointId.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("datapoint").get("id"), datapointId.get()));

        }

        if (from.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("timestamp"), from.get()));

        }

        if (to.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("timestamp"), to.get()));

        }

        return dataRecordRepository.findAll(spec).stream()
                .map(this::entityToModel) // optional mapping
                .toList();
    }


    private DataRecord entityToModel(DataRecordEntity entity) {
        DataRecord model = new DataRecord();
        model.setId(entity.getId());
        model.setTimestamp(entity.getTimestamp());
        model.setNumericValue(entity.getNumericValue());
        model.setStringValue(entity.getStringValue());
        model.setUnitOfMeasurement(entity.getUnitOfMeasurement());

        model.setNodeId(entity.getDatapoint().getDatasource().getNodeId());
        model.setDeviceId(entity.getDatapoint().getDatasource().getId());
        model.setAttributeIdentifier(entity.getDatapoint().getAttributePath());
        return model;
    }



}

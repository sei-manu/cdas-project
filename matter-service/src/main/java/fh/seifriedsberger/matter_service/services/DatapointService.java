package fh.seifriedsberger.matter_service.services;

import fh.seifriedsberger.matter_service.models.Datapoint;
import fh.seifriedsberger.matter_service.models.entites.DatapointEntity;
import fh.seifriedsberger.matter_service.models.entites.DatasourceEntity;
import fh.seifriedsberger.matter_service.repositories.DatapointRepository;
import fh.seifriedsberger.matter_service.repositories.DatasourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DatapointService {

    private final DatapointRepository datapointRepository;

    private final DatasourceRepository datasourceRepository;

    @Autowired
    public DatapointService(DatapointRepository datapointRepository, DatasourceRepository datasourceRepository) {
        this.datapointRepository = datapointRepository;
        this.datasourceRepository = datasourceRepository;
    }

    public Datapoint addDatapoint(Datapoint datapoint, UUID datasourceId) {

        var dsEntity = datasourceToEntity(datasourceId);

        var dpEntity = datapointToEntity(datapoint, dsEntity);

        var inserted = datapointRepository.save(dpEntity);

        return entityToDatapoint(inserted);
    }

    public List<Datapoint> addMultipleDatapoints(List<Datapoint> datapoints, UUID datasourceId) {

        var dsEntity = datasourceToEntity(datasourceId);

        var datapointEntities = datapoints.stream().map(dp -> datapointToEntity(dp, dsEntity)).toList();

        var insertedEntites = datapointRepository.saveAll(datapointEntities);

        return insertedEntites.stream().map(this::entityToDatapoint).toList();
    }

    public List<Datapoint> getDatapointsByDatasourceId(UUID datasourceId) {

        var datapointEntities = datapointRepository.findByDatasourceId(datasourceId);

        return datapointEntities.stream().map(this::entityToDatapoint).toList();
    }

    public List<Datapoint> getAllDatapoints() {
        var datapointEntities = datapointRepository.findAll();
        return datapointEntities.stream().map(this::entityToDatapoint).toList();
    }

    public Datapoint getDatapointById(UUID id) {
        var datapointEntity = datapointRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Datapoint with id " + id + " not found"));
        return entityToDatapoint(datapointEntity);
    }


    public Map<UUID, List<Datapoint>> getDatapointsGroupedByDatasourceIds(List<UUID> datasourceIds) {
        List<DatapointEntity> allDatapoints = datapointRepository.findByDatasourceIdIn(datasourceIds);

        return allDatapoints.stream()
                .map(this::entityToDatapoint)
                .collect(Collectors.groupingBy(Datapoint::getDatasourceId));
    }

    private Datapoint entityToDatapoint(DatapointEntity entity) {
        Datapoint dp = new Datapoint();
        dp.setAttributePath(entity.getAttributePath());
        dp.setType(entity.getType());
        dp.setDescription(entity.getDescription());
        dp.setId(entity.getId());
        dp.setUnitOfMeasurement(entity.getUnitOfMeasurement());
        dp.setDatasourceId(entity.getDatasource().getId());
        return dp;
    }

    private DatapointEntity datapointToEntity(Datapoint datapoint, DatasourceEntity datasourceEntity) {
        DatapointEntity datapointEntity = new DatapointEntity();
        datapointEntity.setDatasource(datasourceEntity);
        datapointEntity.setAttributePath(datapoint.getAttributePath());
        datapointEntity.setDescription(datapoint.getDescription());
        datapointEntity.setType(datapoint.getType());
        datapointEntity.setId(datapoint.getId());
        datapointEntity.setUnitOfMeasurement(datapoint.getUnitOfMeasurement());
        return datapointEntity;
    }

    private DatasourceEntity datasourceToEntity(UUID datasourceId) {
        return datasourceRepository.findById(datasourceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Datasource with id " + datasourceId + " not found during creation of datapoints"));
    }

}

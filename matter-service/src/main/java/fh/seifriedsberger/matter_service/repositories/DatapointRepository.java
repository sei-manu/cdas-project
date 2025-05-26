package fh.seifriedsberger.matter_service.repositories;


import fh.seifriedsberger.matter_service.models.entites.DatapointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DatapointRepository extends JpaRepository<DatapointEntity, UUID> {
    List<DatapointEntity> findByDatasourceId(UUID datasourceId);

    List<DatapointEntity> findByDatasourceIdIn(List<UUID> datasourceIds);

}

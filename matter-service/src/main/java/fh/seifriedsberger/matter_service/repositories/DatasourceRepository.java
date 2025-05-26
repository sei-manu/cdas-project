package fh.seifriedsberger.matter_service.repositories;

import fh.seifriedsberger.matter_service.models.entites.DatasourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.UUID;

public interface DatasourceRepository extends JpaRepository<DatasourceEntity, UUID> {
    List<DatasourceEntity> findByRoomId(UUID roomId);
    DatasourceEntity findByNodeId(int nodeId);
    List<DatasourceEntity> findByRoomIdIn(List<UUID> roomIds);
}

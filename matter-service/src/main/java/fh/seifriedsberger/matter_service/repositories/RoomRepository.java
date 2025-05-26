package fh.seifriedsberger.matter_service.repositories;

import fh.seifriedsberger.matter_service.models.entites.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
}

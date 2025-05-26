package fh.seifriedsberger.matter_service.repositories;

import fh.seifriedsberger.matter_service.models.entites.DataRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataRecordRepository extends JpaRepository<DataRecordEntity, Long>, JpaSpecificationExecutor<DataRecordEntity> {

}

package fh.seifriedsberger.matter_service.models.entites;


import java.time.Instant;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "data_record")
@Data
public class DataRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "datapoint_id", referencedColumnName = "id")
    private DatapointEntity datapoint;
    private Instant timestamp;
    private Double numericValue;
    private String stringValue;
    private String unitOfMeasurement;
}

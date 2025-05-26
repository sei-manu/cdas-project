package fh.seifriedsberger.matter_service.models.entites;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "datapoint")
@Data
public class DatapointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String attributePath;
    @ManyToOne
    @JoinColumn(name = "node_id", referencedColumnName = "nodeId")
    private DatasourceEntity datasource;
    private String description;
    private String type;
    private String unitOfMeasurement;
}


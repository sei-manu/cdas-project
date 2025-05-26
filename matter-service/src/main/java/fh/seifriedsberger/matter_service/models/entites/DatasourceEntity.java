package fh.seifriedsberger.matter_service.models.entites;

import fh.seifriedsberger.matter_service.models.Datasource;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "datasource")
@Data
public class DatasourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int nodeId;
    private String name;
    private String type;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> functionalityMap;
}

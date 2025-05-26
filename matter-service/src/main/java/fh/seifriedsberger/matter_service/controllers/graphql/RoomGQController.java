package fh.seifriedsberger.matter_service.controllers.graphql;

import fh.seifriedsberger.matter_service.models.Datasource;
import fh.seifriedsberger.matter_service.models.Room;
import fh.seifriedsberger.matter_service.models.entites.DataRecordEntity;
import fh.seifriedsberger.matter_service.models.resources.RoomResource;
import fh.seifriedsberger.matter_service.repositories.DataRecordRepository;
import fh.seifriedsberger.matter_service.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoomGQController {

    private final RoomService roomService;


    @Autowired
    public RoomGQController(RoomService roomService) {
        this.roomService = roomService;
    }

    @QueryMapping
    public List<RoomResource> rooms() {
        return roomService.getAllRooms()
                .stream()
                .map(this::roomToResource)
                .toList();
    }



    private RoomResource roomToResource(Room room) {
        return new RoomResource(room.getId(), room.getName(), room.getDescription());
    }
}

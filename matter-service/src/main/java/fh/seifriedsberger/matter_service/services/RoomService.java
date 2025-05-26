package fh.seifriedsberger.matter_service.services;

import fh.seifriedsberger.matter_service.models.Datasource;
import fh.seifriedsberger.matter_service.models.Room;
import fh.seifriedsberger.matter_service.models.entites.RoomEntity;
import fh.seifriedsberger.matter_service.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final DatasourceService datasourceService;

    @Autowired
    public RoomService(RoomRepository roomRepository, DatasourceService datasourceService) {
        this.roomRepository = roomRepository;
        this.datasourceService = datasourceService;
    }

    public Room addRoom(Room room) {
        var roomEntity = roomToEntity(room);
        var inserted = roomRepository.save(roomEntity);
        return entityToRoom(inserted, List.of());
    }

    public List<Room> getAllRooms() {
        return this.roomRepository.findAll().stream().map(this::entityToRoom)
                .toList();
    }

    public Room getRoomById(UUID roomId) {
        var roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with ID " + roomId + " not found"));
        return entityToRoom(roomEntity, datasourceService.getDatasourcesByRoomId(roomId));
    }

    public Room getDefaultRoom() {
        return getAllRooms().getFirst();
    }

    private RoomEntity roomToEntity(Room room) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(room.getId());
        roomEntity.setName(room.getName());
        roomEntity.setDescription(room.getDescription());
        return roomEntity;
    }

    private Room entityToRoom(RoomEntity roomEntity, List<Datasource> datasources) {
        Room room = new Room();
        room.setId(roomEntity.getId());
        room.setName(roomEntity.getName());
        room.setDescription(roomEntity.getDescription());
        room.setDatasources(datasources);
        return room;
    }

    private Room entityToRoom(RoomEntity roomEntity) {
        Room room = new Room();
        room.setId(roomEntity.getId());
        room.setName(roomEntity.getName());
        room.setDescription(roomEntity.getDescription());
        return room;
    }
}

package fh.seifriedsberger.matter_service.controllers.rest;

import fh.seifriedsberger.matter_service.models.Room;
import fh.seifriedsberger.matter_service.models.dtos.RoomDTO;
import fh.seifriedsberger.matter_service.models.resources.RoomResource;
import fh.seifriedsberger.matter_service.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomResource> getAllRooms() {
        return roomService.getAllRooms().stream()
                .map(this::roomToResource)
                .toList();
    }

    @GetMapping("/{id}")
    public RoomResource getRoomById(@PathVariable String id) {
        try {
            UUID roomId = UUID.fromString(id);
            return roomToResource(roomService.getRoomById(roomId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format");
        }
    }

    @PostMapping
    public RoomResource addRoom(@RequestBody RoomDTO roomDTO) {
        Room room = dtoToRoom(roomDTO);

        return roomToResource(roomService.addRoom(room));
    }

    private RoomResource roomToResource(Room room) {
        return new RoomResource(room.getId(), room.getName(), room.getDescription());
    }

    private Room dtoToRoom(RoomDTO roomDTO) {
        Room room = new Room();
        room.setName(roomDTO.name());
        room.setDescription(roomDTO.description());
        return room;
    }
}

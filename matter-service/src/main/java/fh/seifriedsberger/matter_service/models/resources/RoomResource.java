package fh.seifriedsberger.matter_service.models.resources;

import java.util.UUID;

public record RoomResource(UUID id, String name, String description) {
}
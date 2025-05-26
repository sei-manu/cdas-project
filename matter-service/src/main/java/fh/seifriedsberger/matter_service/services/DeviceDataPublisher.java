package fh.seifriedsberger.matter_service.services;

import fh.seifriedsberger.matter_service.models.DataRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceDataPublisher {

    private final Map<UUID, Sinks.Many<DataRecord>> deviceSinkMap = new ConcurrentHashMap<>();
    private final Map<UUID, Sinks.Many<DataRecord>> datapointSinkMap = new ConcurrentHashMap<>();

    public void publishForDevice(UUID deviceId, DataRecord record) {
        var sink = deviceSinkMap.get(deviceId);
        if (sink != null) {
            sink.tryEmitNext(record);
        }
    }

    public void publishForDatapoint(UUID datapointId, DataRecord record) {
        var sink = datapointSinkMap.get(datapointId);
        if (sink != null) {
            sink.tryEmitNext(record);
        }
    }

    public Flux<DataRecord> subscribeToDevice(UUID deviceId) {
        return deviceSinkMap
                .computeIfAbsent(deviceId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }

    public Flux<DataRecord> subscribeToDatapoint(UUID datapointId) {
        return datapointSinkMap
                .computeIfAbsent(datapointId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }
}

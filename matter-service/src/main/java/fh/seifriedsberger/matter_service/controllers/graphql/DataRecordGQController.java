package fh.seifriedsberger.matter_service.controllers.graphql;

import fh.seifriedsberger.matter_service.models.DataRecord;
import fh.seifriedsberger.matter_service.models.resources.DataRecordResource;
import fh.seifriedsberger.matter_service.models.resources.DatapointResource;
import fh.seifriedsberger.matter_service.models.resources.DeviceResource;
import fh.seifriedsberger.matter_service.services.DataRecordService;
import fh.seifriedsberger.matter_service.services.DeviceDataPublisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DataRecordGQController {


    private final DataRecordService dataRecordService;
    private final DeviceDataPublisher deviceDataPublisher;

    public DataRecordGQController(DataRecordService dataRecordService, DeviceDataPublisher deviceDataPublisher) {
        this.dataRecordService = dataRecordService;
        this.deviceDataPublisher = deviceDataPublisher;
    }

    @QueryMapping
    public List<DataRecordResource> deviceData(
            @Argument Optional<UUID> deviceId,
            @Argument Optional<UUID> datapointId,
            @Argument Optional<OffsetDateTime> from,
            @Argument Optional<OffsetDateTime> to) {

        return dataRecordService.getDataRecords(deviceId, datapointId, from, to).stream().map(this::recordToResource).collect(Collectors.toList());
    }

//    @QueryMapping
//    public List<DataRecordResource> dataRecordsByDate(@Argument Instant from, @Argument Instant to) {
//        return List.of();
//        //return dataRecordRepository.findByTimestampBetween(from, to);
//    }

    @SchemaMapping(typeName = "Device", field = "data")
    public List<DataRecordResource> deviceDataByDevice(DeviceResource device,
                                                       @Argument Optional<OffsetDateTime> from,
                                                       @Argument Optional<OffsetDateTime> to) {
        return dataRecordService.getDataRecords(Optional.of(device.id()), Optional.empty(), from, to)
                .stream().map(this::recordToResource).collect(Collectors.toList());
    }

    @SchemaMapping(typeName = "Datapoint", field = "data")
    public List<DataRecordResource> deviceDataByDatapoint(DatapointResource datapoint,
                                                          @Argument Optional<OffsetDateTime> from,
                                                          @Argument Optional<OffsetDateTime> to) {

        return dataRecordService.getDataRecords(Optional.empty(), Optional.of(datapoint.id()), from, to)
                .stream().map(this::recordToResource).collect(Collectors.toList());
    }

//    @BatchMapping(typeName = "Device", field = "data")
//
//    public Map<DeviceResource, List<DataRecordResource>> batchedDeviceData(List<DeviceResource> devices
//    ) {
//

    /// /        Optional<OffsetDateTime> from = Optional.ofNullable(env.getArgument("from"));
    /// /        Optional<OffsetDateTime> to = Optional.ofNullable(env.getArgument("to"));
//
//
//        List<UUID> deviceIds = devices.stream().map(DeviceResource::id).toList();
//
//        List<DataRecord> records = dataRecordService.getDataRecordsForDevices(deviceIds, Optional.empty(), Optional.empty());
//
//        Map<UUID, List<DataRecord>> grouped = records.stream()
//                .collect(Collectors.groupingBy(DataRecord::getDeviceId));
//
//        return devices.stream().collect(Collectors.toMap(
//                Function.identity(),
//                d -> grouped.getOrDefault(d.id(), List.of())
//                        .stream()
//                        .map(this::recordToResource)
//                        .toList()
//        ));
//    }
//
//    @BatchMapping(typeName = "Datapoint", field = "data")
//    public Map<DatapointResource, List<DataRecordResource>> batchedDatapointData(List<DatapointResource> datapoints,
//                                                                                 DataFetchingEnvironment env) {
//        Optional<OffsetDateTime> from = Optional.ofNullable(env.getArgument("from"));
//        Optional<OffsetDateTime> to = Optional.ofNullable(env.getArgument("to"));
//
//        List<String> attributePaths = datapoints.stream().map(DatapointResource::attributePath).toList();
//
//        List<DataRecord> records = dataRecordService.getDataRecordsForDatapoints(attributePaths, from, to);
//
//        Map<String, List<DataRecord>> grouped = records.stream()
//                .collect(Collectors.groupingBy(DataRecord::getAttributeIdentifier));
//
//        return datapoints.stream().collect(Collectors.toMap(
//                Function.identity(),
//                d -> grouped.getOrDefault(d.attributePath(), List.of())
//                        .stream()
//                        .map(this::recordToResource)
//                        .toList()
//        ));
//    }
    @SubscriptionMapping
    public Flux<DataRecordResource> onDeviceData(@Argument Optional<UUID> deviceId) {
        if (deviceId.isPresent()) {
            return deviceDataPublisher.subscribeToDevice(deviceId.get()).map(this::recordToResource);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Device ID provided");
        }
    }

    @SubscriptionMapping
    public Flux<DataRecordResource> onDatapointData(@Argument Optional<UUID> datapointId) {
        if (datapointId.isPresent()) {
            return deviceDataPublisher.subscribeToDatapoint(datapointId.get()).map(this::recordToResource);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Datapoint ID provided");
        }
    }


    private DataRecordResource recordToResource(DataRecord dataRecord) {
        return new DataRecordResource(
                dataRecord.getId(),
                OffsetDateTime.ofInstant(dataRecord.getTimestamp(), TimeZone.getDefault().toZoneId()),
                dataRecord.getNumericValue(),
                dataRecord.getStringValue(),
                dataRecord.getUnitOfMeasurement(),
                dataRecord.getDeviceId(),
                dataRecord.getAttributeIdentifier());
    }
}
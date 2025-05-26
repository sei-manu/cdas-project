package fh.seifriedsberger.matter_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fh.seifriedsberger.matter_service.models.DataRecord;
import fh.seifriedsberger.matter_service.models.Datapoint;
import fh.seifriedsberger.matter_service.models.entites.DataRecordEntity;
import fh.seifriedsberger.matter_service.models.entites.DatasourceEntity;
import fh.seifriedsberger.matter_service.models.entites.RoomEntity;
import fh.seifriedsberger.matter_service.models.matter.matterserver.command.APICommand;
import fh.seifriedsberger.matter_service.models.matter.matterserver.command.MatterServerCommand;
import fh.seifriedsberger.matter_service.models.matter.matterserver.response.*;
import fh.seifriedsberger.matter_service.repositories.DataRecordRepository;
import fh.seifriedsberger.matter_service.repositories.DatapointRepository;
import fh.seifriedsberger.matter_service.repositories.DatasourceRepository;
import fh.seifriedsberger.matter_service.repositories.RoomRepository;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;

@Service
public class MatterServerClient extends WebSocketClient {


    private final ObjectMapper objectMapper;

    private final MatterConfigService matterConfigService;
    private RoomRepository roomRepository;
    private DatasourceRepository datasourceRepository;
    private DatapointService datapointService;
    private DataRecordRepository dataRecordRepository;
    private final DatapointRepository datapointRepository;

    private final DeviceDataPublisher deviceDataPublisher;

    @Autowired
    public MatterServerClient(MatterConfigService configService, RoomRepository roomRepository, DatasourceRepository datasourceRepository, DatapointService datapointService, DataRecordRepository dataRecordRepository, DatapointRepository datapointRepository, DeviceDataPublisher publisher) throws URISyntaxException {
        // TODO configure url in confige file or smth comparable
        super(new URI("ws://localhost:5580/ws"));
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.matterConfigService = configService;
        this.roomRepository = roomRepository;
        this.datasourceRepository = datasourceRepository;
        this.datapointService = datapointService;
        this.dataRecordRepository = dataRecordRepository;
        this.datapointRepository = datapointRepository;
        this.deviceDataPublisher = publisher;
        connect();
        sendCommand(new MatterServerCommand(APICommand.START_LISTENING, "start_listen"));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("WebSocket opened");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        try {
            JsonNode rootNode = objectMapper.readTree(message);

            if (rootNode.has("event")) {
                // Event response (e.g. attribute_updated)
                if (rootNode.get("event").asText().equals(EventType.ATTRIBUTE_UPDATED.getValue())) {
                    MatterServerAttributeUpdatedEvent eventResponse = objectMapper.treeToValue(rootNode, MatterServerAttributeUpdatedEvent.class);
                    handleResponse(eventResponse);
//                    var sink = deviceSinks.get(eventResponse.getNodeId());
//                    if(sink != null) {
//                        sink.tryEmitNext(eventResponse);
//                    }
                    // handleResponse(eventResponse);
                } else {
                    // TODO handle better
                    System.err.println("Received unsupported event: " + rootNode.get("event"));
                }
            } else if (rootNode.has("message_id")) {

                String messageId = rootNode.get("message_id").asText();

                if (rootNode.has("result") && rootNode.get("result") != null) {
                    MatterServerFullResponse deviceResponse = objectMapper.treeToValue(rootNode, MatterServerFullResponse.class);


                    handleResponse(deviceResponse);
                    // TODO define constant somewhere
//                    if(messageId.equals("commission-with-code")) {
//                        eventSink.tryEmitNext(new MatterServerDeviceCommissionedEvent(messageId, deviceResponse.getMatterDatasourceData()));
//                    }
//
//
//                    for (MatterServerNodeResponse node : deviceResponse.getResult()){
//                        var sink = deviceSinks.get(node.getNodeId());
//                        if(sink != null) {
//                            sink.tryEmitNext(node);
//                        }
//                    }

                    // handleResponse(deviceResponse);

                } else if (rootNode.has("result") && rootNode.get("result") == null) {
                    System.out.println("Received confirmation for messageId: " + rootNode.get("message_id"));
                    // eventSink.tryEmitNext(new MatterServerEvent(messageId));
                } else if (rootNode.has("error_code") && rootNode.get("error_code") != null) {
                    int errorCode = Integer.parseInt(rootNode.get("error_code").asText());
                    String details = rootNode.get("details").asText();

                    // TODO error handling
                    // eventSink.tryEmitNext(rootNode.get("message_id").asText());

                    System.err.println("Received error code: " + errorCode + ", details: " + details);
                } else {
                    // TODO handle better
                    System.err.println("Received unsupported message: " + message);
                }
            } else if (rootNode.has("fabric_id")) {
                MatterServerResponse statusResponse = objectMapper.treeToValue(rootNode, MatterServerStatusResponse.class);
                handleResponse(statusResponse);
                // sink.tryEmitNext(statusResponse);
                // handleResponse(statusResponse);
            } else {
                // Handle unexpected format (or an unknown type of response)
                // TODO handle better
                System.err.println("Unexpected response format: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to parse message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + reason);
//        for (Sinks.Many<MatterServerResponse> listener : deviceSinks.values()) {
//            listener.tryEmitComplete();
//        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
//        for (Sinks.Many<MatterServerResponse> listener : deviceSinks.values()) {
//            listener.tryEmitError(ex);
//        }
    }

    public void sendCommand(MatterServerCommand command) {
        if (this.isOpen()) {

            // Convert the command to JSON
            try {
                String json = objectMapper.writeValueAsString(command);
                System.out.println("Sending: " + json);
                this.send(json);
            } catch (JsonProcessingException e) {
                // TODO implement better
                throw new RuntimeException("Json mapping exception in send command: " + e);
            }
        } else {
            System.err.println("Cannot send message, WebSocket not connected.");
        }
    }


    private void handleResponse(MatterServerResponse response) {
        switch (response.getServerResponseType()) {
            case EVENT -> {
                System.out.println("Received event: " + response);
            }
            case RESULT -> {
                System.out.println("Received result: " + response);

                MatterServerFullResponse fr = (MatterServerFullResponse) response;

                if (Objects.equals(fr.getMessageId(), "commission-with-code")) {
                    // Receive response

                    var node = fr.getResult().getFirst();
                    var deviceType = ((LinkedHashMap<?, ?>) ((ArrayList<?>) node.getAttributes().get("1/29/0")).getFirst()).get("0").toString();

                    var matterDeviceConfig = matterConfigService.loadConfig(deviceType);


                    RoomEntity roomEntity;
                    if (roomRepository.findAll().isEmpty()) {
                        roomEntity = new RoomEntity();
                        roomEntity.setName("Default Room");
                        roomEntity.setDescription("Default Room");
                        roomEntity = roomRepository.saveAndFlush(roomEntity);
                    } else {
                        roomEntity = roomRepository.findAll().getFirst();
//            roomEntity = roomRepository.findById(room.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room with id " + room.getId() + " not found"));
                    }


                    DatasourceEntity datasourceEntity = new DatasourceEntity();
                    datasourceEntity.setName(node.getAttributes().get("0/40/3").toString()); // 0/40/3 > Eve Energy 20EB...; Vendor: 0/40/1
                    datasourceEntity.setType(matterDeviceConfig.getName());
                    datasourceEntity.setRoom(roomEntity);
                    datasourceEntity.setFunctionalityMap(new HashMap<>());
                    datasourceEntity.setNodeId(fr.getResult().getFirst().getNodeId());

                    var de = this.datasourceRepository.save(datasourceEntity);

                    var sources = matterDeviceConfig.getSources();
                    List<Datapoint> datapoints = new ArrayList<>();
                    for (var source : sources) {
                        for (var dp : source.getDatapoints()) {
                            var toInsert = new Datapoint(dp.getAttributePath(), dp.getDescription(), dp.getId(), dp.getUnitOfMeasurement());
                            var inserted = this.datapointService.addDatapoint(toInsert, de.getId());
                            datapoints.add(inserted);
                        }
                    }

                    for (var dp : datapoints) {

                        var value = node.getAttributes().get(dp.getAttributePath()).toString();

                        DataRecordEntity record = new DataRecordEntity();
                        record.setTimestamp(Instant.now());

                        record.setUnitOfMeasurement(dp.getUnitOfMeasurement());

                        record.setDatapoint(datapointRepository.findById(dp.getId()).orElseThrow());

                        var optVal = parseDoubleOrNull(value);
                        if (optVal.isPresent()) {
                            record.setNumericValue(optVal.get());
                        } else {
                            record.setStringValue(value);
                        }

                        var entity = dataRecordRepository.save(record);

                        var model = entityToModel(entity);
                        this.deviceDataPublisher.publishForDevice(de.getId(), model);
                        this.deviceDataPublisher.publishForDatapoint(dp.getId(), model);
                    }


                }

                else if (fr.getMessageId().startsWith("poll-device-")) {
                    var node = fr.getResult().getFirst();

                    var datasource = this.datasourceRepository.findByNodeId(node.getNodeId());

                    var datapoints = this.datapointRepository.findByDatasourceId(datasource.getId());

                    // TODO remove code duplication
                    for (var dp : datapoints) {

                        var value = node.getAttributes().get(dp.getAttributePath()).toString();

                        DataRecordEntity record = new DataRecordEntity();
                        record.setTimestamp(Instant.now());

                        record.setUnitOfMeasurement(dp.getUnitOfMeasurement());

                        record.setDatapoint(datapointRepository.findById(dp.getId()).orElseThrow());

                        var optVal = parseDoubleOrNull(value);
                        if (optVal.isPresent()) {
                            record.setNumericValue(optVal.get());
                        } else {
                            record.setStringValue(value);
                        }

                        var entity = dataRecordRepository.save(record);

                        var model = entityToModel(entity);
                        this.deviceDataPublisher.publishForDevice(datasource.getId(), model);
                        this.deviceDataPublisher.publishForDatapoint(dp.getId(), model);
                    }
                }

                else if (fr.getMessageId().startsWith("poll-all-devices")) {
                    // TODO
                }
            }
            case ERROR -> {
                System.out.println("Received error: " + response);
            }
            case STATUS -> {
                System.out.println("Received status: " + response);
            }
            default -> {
                System.out.println("Received unsupported response: " + response);
            }
        }
    }

    private Optional<Double> parseDoubleOrNull(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private DataRecord entityToModel(DataRecordEntity entity) {
        DataRecord model = new DataRecord();
        model.setId(entity.getId());
        model.setTimestamp(entity.getTimestamp());
        model.setNumericValue(entity.getNumericValue());
        model.setStringValue(entity.getStringValue());
        model.setUnitOfMeasurement(entity.getUnitOfMeasurement());

        model.setNodeId(entity.getDatapoint().getDatasource().getNodeId());
        model.setDeviceId(entity.getDatapoint().getDatasource().getId());
        model.setAttributeIdentifier(entity.getDatapoint().getAttributePath());
        return model;
    }
}
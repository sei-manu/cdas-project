package fh.seifriedsberger.matter_service.controllers.rest;


import fh.seifriedsberger.matter_service.services.DataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
@RequestMapping("/api/data")
public class DataRecordController {

    private final DataRecordService dataRecordService;

    @Autowired
    public DataRecordController(DataRecordService dataRecordService) {
        this.dataRecordService = dataRecordService;
    }

    @PostMapping("/poll")
    public void pollDevices(){
        dataRecordService.pollAllDevices();
    }

    @PostMapping("/poll/{datasourceId}")
    public void pollDevices(@PathVariable UUID datasourceId){
        dataRecordService.pollDevice(datasourceId);
    }

}

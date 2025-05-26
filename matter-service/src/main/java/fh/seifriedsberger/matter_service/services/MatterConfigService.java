package fh.seifriedsberger.matter_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fh.seifriedsberger.matter_service.models.matter.deviceconfig.MatterDeviceConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatterConfigService {

    private Map<String, MatterDeviceConfig> configs = new HashMap<>();

    public MatterDeviceConfig loadConfig(String deviceType) {

        if (configs.containsKey(deviceType)) {
            return configs.get(deviceType);
        }

        String filename = "matter-device-configs/" + deviceType + ".yml";

        try {
            ClassPathResource resource = new ClassPathResource(filename);

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            MatterDeviceConfig config = mapper.readValue(resource.getInputStream(), MatterDeviceConfig.class);

            configs.put(deviceType, config);
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MatterDeviceConfig> getAllConfigs() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:matter-device-configs/*.yml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            for (Resource resource : resources) {
                MatterDeviceConfig config = mapper.readValue(resource.getInputStream(), MatterDeviceConfig.class);

                configs.put(config.getId(), config);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(configs.values());
    }

    public boolean typeExists(String type) {
        if(configs.isEmpty()) {
            getAllConfigs();
        }
        return configs.containsKey(type);
    }

    public String getTypeName(String type) {
        if(configs.isEmpty()) {
            getAllConfigs();
        }
        return configs.get(type).getName();
    }
}

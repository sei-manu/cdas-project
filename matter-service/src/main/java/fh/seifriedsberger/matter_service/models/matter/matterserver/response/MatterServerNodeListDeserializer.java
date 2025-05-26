package fh.seifriedsberger.matter_service.models.matter.matterserver.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MatterServerNodeListDeserializer extends JsonDeserializer<List<MatterServerNodeResponse>> {

    @Override
    public List<MatterServerNodeResponse> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        if (p.currentToken() == JsonToken.START_OBJECT) {
            MatterServerNodeResponse node = mapper.readValue(p, MatterServerNodeResponse.class);
            return Collections.singletonList(node);
        } else if (p.currentToken() == JsonToken.START_ARRAY) {
            return mapper.readValue(p, mapper.getTypeFactory().constructCollectionType(List.class, MatterServerNodeResponse.class));
        } else {
            return Collections.emptyList();
        }
    }
}
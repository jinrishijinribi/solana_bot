package com.rich.sol_bot.system.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LongJsonConverter {
    private LongJsonConverter() {
    }

    public static class LongSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long num, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (num != null) {
                jsonGenerator.writeString(num.toString());
            }
        }

        @Override
        public void serializeWithType(Long value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
            serialize(value, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }
    }
}

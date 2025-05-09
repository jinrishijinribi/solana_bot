package com.rich.sol_bot.system.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigInteger;

@JsonComponent
public class BigIntegerJsonConverter {
    private BigIntegerJsonConverter() {
    }

    public static class BigIntegerSerializer extends JsonSerializer<BigInteger> {
        @Override
        public void serialize(BigInteger bigInteger, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (bigInteger != null) {
                jsonGenerator.writeString(bigInteger.toString());
            }
        }

        @Override
        public void serializeWithType(BigInteger value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
            serialize(value, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }
    }
}

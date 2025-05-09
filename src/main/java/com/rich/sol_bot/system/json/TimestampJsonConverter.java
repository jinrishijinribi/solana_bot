package com.rich.sol_bot.system.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.rich.sol_bot.system.common.TimestampUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author T.J
 * @date 2022/2/9 21:30
 */
@JsonComponent
public class TimestampJsonConverter {
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("^\\d{10}$|^\\d{13}$");

    private TimestampJsonConverter() {
    }

    public static class TimestampDeserializer extends JsonDeserializer<Timestamp> {
        @Override
        public Timestamp deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            final String value = p.readValueAs(String.class);
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            Matcher matcher = TIMESTAMP_PATTERN.matcher(value);
            if (matcher.matches()) {
                return TimestampUtil.fromMillis(value);
            }
            return TimestampUtil.parse(value);
        }
    }

    public static class TimestampSerializer extends JsonSerializer<Timestamp> {
        @Override
        public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (timestamp != null) {
                jsonGenerator.writeString(String.valueOf(timestamp.getTime()));
            }
        }

        @Override
        public void serializeWithType(Timestamp value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
            serialize(value, gen, serializers);
            typeSer.writeTypeSuffix(gen, typeIdDef);
        }
    }
}

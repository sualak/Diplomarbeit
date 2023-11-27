package at.dertanzbogen.api.foundation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal>
{
    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
    {
        String value = jp.readValueAs(String.class);
        return new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
    }
}
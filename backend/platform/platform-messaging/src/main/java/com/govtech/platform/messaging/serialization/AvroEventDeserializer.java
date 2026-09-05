package com.govtech.platform.messaging.serialization;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

@Component
public class AvroEventDeserializer implements EventDeserializer {

    @Override
    public <T> T deserialize(
            byte[] payload,
            Class<T> targetType) {

        try {

            SpecificRecord prototype = (SpecificRecord) targetType
                    .getDeclaredConstructor()
                    .newInstance();

            BinaryDecoder decoder = DecoderFactory.get()
                    .binaryDecoder(payload, null);

            SpecificDatumReader<SpecificRecord> reader = new SpecificDatumReader<>(
                    prototype.getSchema());

            return targetType.cast(
                    reader.read(null, decoder));

        } catch (Exception exception) {

            throw new AvroDeserializationException(
                    "Unable to deserialize Avro event as "
                            + targetType.getName(),
                    exception);
        }
    }
}
package com.govtech.platform.messaging.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

@Component
public class AvroEventSerializer implements EventSerializer {

    private final EncoderFactory encoderFactory = EncoderFactory.get();

    @Override
    public byte[] serialize(Object event) {

        if (!(event instanceof SpecificRecord specificRecord)) {
            throw new IllegalArgumentException(
                    "Event must implement SpecificRecord: "
                            + event.getClass().getName());
        }

        try {

            SpecificDatumWriter<SpecificRecord> writer = new SpecificDatumWriter<>(
                    specificRecord.getSchema());

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            BinaryEncoder encoder = encoderFactory.binaryEncoder(
                    output,
                    null);

            writer.write(
                    specificRecord,
                    encoder);

            encoder.flush();

            return output.toByteArray();

        } catch (IOException exception) {

            throw new AvroSerializationException(
                    "Unable to serialize Avro event: "
                            + event.getClass().getName(),
                    exception);
        }
    }
}
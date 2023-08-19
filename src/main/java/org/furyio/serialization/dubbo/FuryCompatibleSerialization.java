package org.furyio.serialization.dubbo;

import io.fury.Fury;
import io.fury.collection.Tuple2;
import io.fury.memory.MemoryBuffer;
import io.fury.memory.MemoryUtils;
import io.fury.serializer.CompatibleMode;

/**
 * Fury serialization for dubbo. This integration support type forward/backward compatibility.
 *
 * @author chaokunyang
 */
public class FuryCompatibleSerialization extends BaseFurySerialization {
  public static final byte FURY_SERIALIZATION_ID = 29;
  private static final ThreadLocal<Tuple2<Fury, MemoryBuffer>> furyFactory =
      ThreadLocal.withInitial(
          () -> {
            Fury fury =
                Fury.builder()
                    .requireClassRegistration(false)
                    .withCompatibleMode(CompatibleMode.COMPATIBLE)
                    .build();
            MemoryBuffer buffer = MemoryUtils.buffer(32);
            return Tuple2.of(fury, buffer);
          });

  public byte getContentTypeId() {
    return FURY_SERIALIZATION_ID;
  }

  public String getContentType() {
    return "fury/compatible";
  }

  @Override
  protected Tuple2<Fury, MemoryBuffer> getFury() {
    return furyFactory.get();
  }
}

package org.furyio.serialization.dubbo;

import com.google.common.base.Preconditions;
import io.fury.Fury;
import io.fury.collection.Tuple2;
import io.fury.memory.MemoryBuffer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.ObjectOutput;
import org.apache.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Fury serialization framework integration with dubbo.
 *
 * @author chaokunyang
 */
public abstract class BaseFurySerialization implements Serialization {
  protected abstract Tuple2<Fury, MemoryBuffer> getFury();

  public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
    Tuple2<Fury, MemoryBuffer> tuple2 = getFury();
    return new FuryObjectOutput(tuple2.f0, tuple2.f1, output);
  }

  public ObjectInput deserialize(URL url, InputStream input) throws IOException {
    Tuple2<Fury, MemoryBuffer> tuple2 = getFury();
    MemoryBuffer buffer = tuple2.f1;
    buffer.readerIndex(0);
    int readOffset = input.read(buffer.getHeapMemory(), 0, 4);
    Preconditions.checkArgument(readOffset == 4, readOffset);
    int size = buffer.getInt(0);
    int end = 4 + size;
    buffer.ensure(end);
    while (readOffset < end) {
      readOffset += input.read(buffer.getHeapMemory(), readOffset, end - readOffset);
    }
    return new FuryObjectInput(tuple2.f0, buffer);
  }
}

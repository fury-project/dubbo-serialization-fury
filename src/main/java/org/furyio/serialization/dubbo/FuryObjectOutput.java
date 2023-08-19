package org.furyio.serialization.dubbo;

import com.google.common.base.Preconditions;
import io.fury.Fury;
import io.fury.memory.MemoryBuffer;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.dubbo.common.serialize.ObjectOutput;

/**
 * Fury implementation for {@link ObjectOutput}.
 *
 * @author chaokunyang
 */
public class FuryObjectOutput implements ObjectOutput {
  private final Fury fury;
  private final MemoryBuffer buffer;
  private final OutputStream output;

  public FuryObjectOutput(Fury fury, MemoryBuffer buffer, OutputStream output) {
    this.fury = fury;
    this.buffer = buffer;
    this.output = output;
    if (buffer.writerIndex() != 0) {
      throw new IllegalArgumentException("Index should be 0 instead of " + buffer.writerIndex());
    }
    buffer.writeInt(-1);
  }

  public void writeObject(Object obj) {
    fury.serializeJavaObjectAndClass(buffer, obj);
  }

  public void writeBool(boolean v) {
    buffer.writeBoolean(v);
  }

  public void writeByte(byte v) {
    buffer.writeByte(v);
  }

  public void writeShort(short v) {
    buffer.writeShort(v);
  }

  public void writeInt(int v) {
    buffer.writeVarInt(v);
  }

  public void writeLong(long v) {
    buffer.writeLong(v);
  }

  public void writeFloat(float v) {
    buffer.writeFloat(v);
  }

  public void writeDouble(double v) {
    buffer.writeDouble(v);
  }

  public void writeUTF(String v) {
    if (v != null) {
      buffer.writeBoolean(true);
      fury.writeJavaString(buffer, v);
    } else {
      buffer.writeBoolean(false);
    }
  }

  public void writeBytes(byte[] v) {
    if (v != null) {
      buffer.writeBoolean(true);
      buffer.writeBytesWithSizeEmbedded(v);
    } else {
      buffer.writeBoolean(false);
    }
  }

  public void writeBytes(byte[] v, int off, int len) {
    if (v != null) {
      buffer.writeBoolean(true);
      buffer.writePositiveVarInt(len);
      buffer.writeBytes(v, off, len);
    } else {
      buffer.writeBoolean(false);
    }
  }

  public void flushBuffer() throws IOException {
    byte[] heapMemory = buffer.getHeapMemory();
    Preconditions.checkNotNull(heapMemory);
    final int targetIndex = buffer.unsafeHeapReaderIndex();
    buffer.putInt(0, buffer.writerIndex() - 4);
    output.write(heapMemory, targetIndex, buffer.writerIndex());
    buffer.writerIndex(0);
    output.flush();
  }
}

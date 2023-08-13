package org.furyio.serialization.dubbo;

import com.google.common.base.Preconditions;
import io.fury.Fury;
import io.fury.memory.MemoryBuffer;
import org.apache.dubbo.common.serialize.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

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
    buffer.writeInt(-1);
  }

  public void writeObject(Object obj) throws IOException {
    fury.writeNonRef(buffer, obj);
  }

  public void writeBool(boolean v) throws IOException {
    buffer.writeBoolean(v);
  }

  public void writeByte(byte v) throws IOException {
    buffer.writeByte(v);
  }

  public void writeShort(short v) throws IOException {
    buffer.writeShort(v);
  }

  public void writeInt(int v) throws IOException {
    buffer.writeVarInt(v);
  }

  public void writeLong(long v) throws IOException {
    buffer.writeVarLong(v);
  }

  public void writeFloat(float v) throws IOException {
    buffer.writeFloat(v);
  }

  public void writeDouble(double v) throws IOException {
    buffer.writeDouble(v);
  }

  public void writeUTF(String v) throws IOException {
    fury.writeJavaString(buffer, v);
  }

  public void writeBytes(byte[] v) throws IOException {
    buffer.writeBytesWithSizeEmbedded(v);
  }

  public void writeBytes(byte[] v, int off, int len) throws IOException {
    buffer.writePositiveVarInt(len);
    buffer.writeBytes(v, off, len);
  }

  public void flushBuffer() throws IOException {
    byte[] heapMemory = buffer.getHeapMemory();
    Preconditions.checkNotNull(heapMemory);
    final int targetIndex = buffer.unsafeHeapReaderIndex();
    buffer.putInt(0, buffer.writerIndex());
    output.write(heapMemory, targetIndex, buffer.writerIndex());
    buffer.writerIndex(0);
    output.flush();
  }
}

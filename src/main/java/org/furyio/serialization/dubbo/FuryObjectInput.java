package org.furyio.serialization.dubbo;

import io.fury.Fury;
import io.fury.memory.MemoryBuffer;
import org.apache.dubbo.common.serialize.ObjectInput;

import java.io.IOException;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class FuryObjectInput implements ObjectInput {
  private final Fury fury;
  private final MemoryBuffer buffer;

  public FuryObjectInput(Fury fury, MemoryBuffer buffer) {
    this.fury = fury;
    this.buffer = buffer;
  }


  @Override
  public Object readObject() throws IOException, ClassNotFoundException {
    return fury.readNonRef(buffer);
  }

  @Override
  public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
    return (T) fury.readNonRef(buffer);
  }

  @Override
  public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
    return (T) fury.readNonRef(buffer);
  }

  @Override
  public boolean readBool() throws IOException {
    return buffer.readBoolean();
  }

  @Override
  public byte readByte() throws IOException {
    return buffer.readByte();
  }

  @Override
  public short readShort() throws IOException {
    return buffer.readShort();
  }

  @Override
  public int readInt() throws IOException {
    return buffer.readInt();
  }

  @Override
  public long readLong() throws IOException {
    return buffer.readLong();
  }

  @Override
  public float readFloat() throws IOException {
    return buffer.readFloat();
  }

  @Override
  public double readDouble() throws IOException {
    return buffer.readDouble();
  }

  @Override
  public String readUTF() throws IOException {
    return fury.readJavaString(buffer);
  }

  @Override
  public byte[] readBytes() throws IOException {
    return buffer.readBytesWithSizeEmbedded();
  }
}

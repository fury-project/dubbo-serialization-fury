package org.furyio.serialization.dubbo;

import io.fury.Fury;
import io.fury.memory.MemoryBuffer;
import java.lang.reflect.Type;
import org.apache.dubbo.common.serialize.ObjectInput;

@SuppressWarnings("unchecked")
public class FuryObjectInput implements ObjectInput {
  private final Fury fury;
  private final MemoryBuffer buffer;

  public FuryObjectInput(Fury fury, MemoryBuffer buffer) {
    this.fury = fury;
    this.buffer = buffer;
  }

  @Override
  public Object readObject() {
    return fury.deserializeJavaObjectAndClass(buffer);
  }

  @Override
  public <T> T readObject(Class<T> cls) {
    return (T) readObject();
  }

  @Override
  public <T> T readObject(Class<T> cls, Type type) {
    return (T) readObject();
  }

  @Override
  public boolean readBool() {
    return buffer.readBoolean();
  }

  @Override
  public byte readByte() {
    return buffer.readByte();
  }

  @Override
  public short readShort() {
    return buffer.readShort();
  }

  @Override
  public int readInt() {
    return buffer.readVarInt();
  }

  @Override
  public long readLong() {
    return buffer.readLong();
  }

  @Override
  public float readFloat() {
    return buffer.readFloat();
  }

  @Override
  public double readDouble() {
    return buffer.readDouble();
  }

  @Override
  public String readUTF() {
    if (buffer.readBoolean()) {
      return fury.readJavaString(buffer);
    } else {
      return null;
    }
  }

  @Override
  public byte[] readBytes() {
    if (buffer.readBoolean()) {
      return buffer.readBytesWithSizeEmbedded();
    } else {
      return null;
    }
  }
}

package org.furyio.serialization.dubbo;

import io.fury.Fury;
import io.fury.exception.InsecureException;
import io.fury.resolver.AllowListChecker;
import io.fury.serializer.Serializer;
import io.fury.serializer.SerializerFactory;
import java.io.Serializable;
import java.util.Set;
import org.apache.dubbo.common.utils.AllowClassNotifyListener;
import org.apache.dubbo.common.utils.SerializeCheckStatus;
import org.apache.dubbo.common.utils.SerializeSecurityManager;
import org.apache.dubbo.rpc.model.FrameworkModel;

@SuppressWarnings("rawtypes")
public class FuryCheckerListener implements AllowClassNotifyListener, SerializerFactory {
  private final SerializeSecurityManager securityManager;
  private final AllowListChecker checker;
  private volatile boolean checkSerializable;

  public FuryCheckerListener(FrameworkModel frameworkModel) {
    checker = new AllowListChecker();
    securityManager =
        frameworkModel.getBeanFactory().getOrRegisterBean(SerializeSecurityManager.class);
    securityManager.registerListener(this);
  }

  @Override
  public void notifyPrefix(Set<String> allowedList, Set<String> disAllowedList) {
    for (String prefix : allowedList) {
      checker.allowClass(prefix);
    }
    for (String prefix : disAllowedList) {
      checker.disallowClass(prefix);
    }
  }

  @Override
  public void notifyCheckStatus(SerializeCheckStatus status) {
    switch (status) {
      case DISABLE:
        checker.setCheckLevel(AllowListChecker.CheckLevel.DISABLE);
        return;
      case WARN:
        checker.setCheckLevel(AllowListChecker.CheckLevel.WARN);
        return;
      case STRICT:
        checker.setCheckLevel(AllowListChecker.CheckLevel.STRICT);
        return;
      default:
        throw new UnsupportedOperationException("Unsupported check level " + status);
    }
  }

  @Override
  public void notifyCheckSerializable(boolean checkSerializable) {
    this.checkSerializable = checkSerializable;
  }

  public AllowListChecker getChecker() {
    return checker;
  }

  public boolean isCheckSerializable() {
    return checkSerializable;
  }

  @Override
  public Serializer createSerializer(Fury fury, Class<?> cls) {
    if (checkSerializable && !Serializable.class.isAssignableFrom(cls)) {
      throw new InsecureException(String.format("%s is not Serializable", cls));
    }
    return null;
  }
}

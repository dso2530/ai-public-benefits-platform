package com.govtech.platform.messaging.publisher;

import java.util.concurrent.CompletableFuture;

public interface EventPublisher {

  CompletableFuture<Void> publish(
      String topic,
      String key,
      Object event);

}
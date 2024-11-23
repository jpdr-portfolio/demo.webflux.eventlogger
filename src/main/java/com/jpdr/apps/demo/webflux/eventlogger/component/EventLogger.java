package com.jpdr.apps.demo.webflux.eventlogger.component;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class EventLogger {
  
  private final KafkaTemplate<String, EventLoggerMessage> kafkaTemplate;
  private final String topic;
  private final String podName;
  
  public void logEvent(String method, Object data){
    Mono.defer( () -> Mono.just(EventLoggerMessage.builder()
        .podName(this.podName)
        .method(method)
        .timestamp(OffsetDateTime.now().toString())
        .data(data)
        .build()))
      .flatMap(message -> Mono.fromFuture(
        this.kafkaTemplate.send(this.topic, message).toCompletableFuture())
        .subscribeOn(Schedulers.boundedElastic()))
        .subscribe();
  }
  
}

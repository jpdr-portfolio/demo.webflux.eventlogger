package com.jpdr.apps.demo.webflux.eventlogger.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
public class EventLogger {
  
  private final KafkaTemplate<String, EventLoggerMessage> kafkaTemplate;
  private final String topic;
  private final String podName;
  private final DataConverter dataConverter;
  
  public void logEvent(String method, Object data){
    Mono.defer(() -> Mono.justOrEmpty(data))
      .map(dataConverter::toJsonNode)
      .map(jsonObject ->
        EventLoggerMessage.builder()
          .podName(this.podName)
          .method(method)
          .timestamp(OffsetDateTime.now().toString())
          .data(jsonObject.toPrettyString())
          .build())
      .flatMap(message -> Mono.fromFuture(
        this.kafkaTemplate.send(this.topic, message).toCompletableFuture()))
      .doOnError( error -> log.warn("An error occurred while logging the event: " + ExceptionUtils.getStackTrace(error)))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }
  
}

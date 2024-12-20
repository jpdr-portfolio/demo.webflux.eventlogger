package com.jpdr.apps.demo.webflux.eventlogger.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "app.event-logger")
public class EventLoggerProperties {
  
  @NotNull
  private KafkaProperties kafka;
  private List<String> hiddenFields;
  
  @Getter
  @Setter
  public static class KafkaProperties{
    @NotNull
    private String boostrapServer;
    @NotNull
    private String compressionType;
    @NotNull
    private Integer requestTimeoutMs;
    @NotNull
    private Integer deliveryTimeoutMs;
    @NotNull
    private Integer retries;
    @NotNull
    private Boolean addTypeInfoHeaders;
    @NotNull
    private String topic;
  }
  
}

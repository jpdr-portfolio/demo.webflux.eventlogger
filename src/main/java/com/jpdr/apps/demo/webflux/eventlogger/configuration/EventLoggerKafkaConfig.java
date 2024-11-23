package com.jpdr.apps.demo.webflux.eventlogger.configuration;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLoggerMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EventLoggerKafkaConfig {
  
  @Value("${app.event-logger.kafka.boostrap-server}")
  private String boostrapServer;
  @Value("${app.event-logger.kafka.compression-type}")
  private String compressionType;
  @Value("${app.event-logger.kafka.request-timeout-ms}")
  private Integer requestTimeoutMs;
  @Value("${app.event-logger.kafka.delivery-timeout-ms}")
  private Integer deliveryTimeoutMs;
  @Value("${app.event-logger.kafka.retries}")
  private Integer retries;
  @Value("${app.event-logger.kafka.add-type-info-headers}")
  private Boolean addTypeInfoHeaders;
  @Value("${app.event-logger.kafka.topic}")
  private String topic;
  @Value("${app.pod-name}")
  private String podName;
  
  @Bean(name = "eventLoggerKafkaProducerFactory")
  public ProducerFactory<String, EventLoggerMessage> producerFactory(){
    Map<String, Object> options = new HashMap<>();
    options.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    options.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.springframework.kafka.support.serializer.JsonSerializer");
    options.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.boostrapServer);
    options.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.requestTimeoutMs);
    options.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, this.deliveryTimeoutMs);
    options.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.compressionType);
    options.put(ProducerConfig.RETRIES_CONFIG, this.retries);
    options.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, this.addTypeInfoHeaders);
    return new DefaultKafkaProducerFactory<>(options);
  }
  
  @Bean(name = "eventLoggerKafkaTemplate")
  public KafkaTemplate<String, EventLoggerMessage> kafkaTemplate(
    @Qualifier("eventLoggerKafkaProducerFactory") ProducerFactory<String, EventLoggerMessage> producerFactory){
    return new KafkaTemplate<>(producerFactory);
  }
  
  @Bean
  public EventLogger eventLogger(
    @Qualifier("eventLoggerKafkaTemplate") KafkaTemplate<String, EventLoggerMessage> kafkaTemplate){
      return new EventLogger(kafkaTemplate, this.topic, this.podName);
  }

}

package com.jpdr.apps.demo.webflux.eventlogger.configuration;

import com.jpdr.apps.demo.webflux.eventlogger.component.DataConverter;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLoggerMessage;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EventLoggerKafkaConfig {
  
  private final EventLoggerProperties properties;
  private final DataConverter dataConverter;

  @Value("${app.pod-name}")
  private String podName;
  
  @Bean(name = "eventLoggerKafkaProducerFactory")
  public ProducerFactory<String, EventLoggerMessage> producerFactory(){
    Map<String, Object> options = new HashMap<>();
    options.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer");
    options.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.springframework.kafka.support.serializer.JsonSerializer");
    options.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
      this.properties.getKafka().getBoostrapServer());
    options.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
      this.properties.getKafka().getRequestTimeoutMs());
    options.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
      this.properties.getKafka().getDeliveryTimeoutMs());
    options.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,
      this.properties.getKafka().getCompressionType());
    options.put(ProducerConfig.RETRIES_CONFIG,
      this.properties.getKafka().getRetries());
    options.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,
      this.properties.getKafka().getAddTypeInfoHeaders());
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
      return new EventLogger(kafkaTemplate, this.properties.getKafka().getTopic(),
        this.podName, this.dataConverter);
  }

}

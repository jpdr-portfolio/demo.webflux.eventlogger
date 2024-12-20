package com.jpdr.apps.demo.webflux.eventlogger.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jpdr.apps.demo.webflux.eventlogger.configuration.EventLoggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataConverter {
  
  private final ObjectMapper objectMapper;
  private final EventLoggerProperties properties;
  
  public ObjectNode toObjectNode(Object dataObject){
    ObjectNode objectNode = this.objectMapper.valueToTree(dataObject);
    if (properties.getHiddenFields() != null && !this.properties.getHiddenFields().isEmpty()) {
      for (String fieldName : this.properties.getHiddenFields()) {
        if (objectNode.get(fieldName) != null) {
          objectNode.put(fieldName, "*".repeat(10));
        }
      }
    }
    return objectNode;
  }
  
}

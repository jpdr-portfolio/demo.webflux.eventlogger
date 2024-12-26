package com.jpdr.apps.demo.webflux.eventlogger.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jpdr.apps.demo.webflux.eventlogger.configuration.EventLoggerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataConverter {
  
  private final ObjectMapper objectMapper;
  private final EventLoggerProperties properties;
  
  public JsonNode toJsonNode(Object dataObject){
    JsonNode jsonNode = this.objectMapper.valueToTree(dataObject);
    if (properties.getHiddenFields() != null && !this.properties.getHiddenFields().isEmpty()) {
      updateObjectFields(jsonNode);
    }
    return jsonNode;
  }
  
  private JsonNode updateObjectFields(JsonNode jsonNode){
    if(jsonNode.isObject()){
      ObjectNode objectNode = (ObjectNode) jsonNode;
      for (String fieldName : this.properties.getHiddenFields()) {
        if (objectNode.get(fieldName) != null) {
          objectNode.put(fieldName, "*".repeat(10));
        }
      }
    }else{
      if (jsonNode.isArray()){
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        for(JsonNode childNode : arrayNode){
          updateObjectFields(childNode);
        }
      }
    }
    return jsonNode;
  }
  
}

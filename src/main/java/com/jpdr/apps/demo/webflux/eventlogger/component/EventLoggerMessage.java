package com.jpdr.apps.demo.webflux.eventlogger.component;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventLoggerMessage {
  
  String podName;
  String method;
  String timestamp;
  Object data;
  
}

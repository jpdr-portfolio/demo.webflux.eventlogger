package com.jpdr.apps.demo.webflux.eventlogger;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLoggerMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.OffsetDateTime;

@SpringBootApplication
public class Application {
	
	@Autowired
	private EventLogger eventLogger;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@PostConstruct
	public void postConstruct(){
		this.eventLogger.logEvent("method", "data");
	}

}

package com.ads.assignment1.dbaccess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
@SpringBootApplication
@ComponentScan(basePackages= {"com.ads.assignment1.dbaccess.*"})

public class DbaccessApplication {
	
	private static final String dateFormat = "yyyy-MM-dd";
	private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

	public static void main(String[] args) {
		//SpringApplication.run(DbaccessApplication.class, args);
		SpringApplication app = new SpringApplication(DbaccessApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8008"));
		// Local Debug
		//app.setDefaultProperties(Collections.singletonMap("server.port", "8088"));
		app.run(args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(){
		return builder ->{
			builder.simpleDateFormat(dateFormat);
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
			builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
			builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
		};
	}
}

package com.basic.crud.autocrud.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Getter
public class ApplicationVariables {

    @Value("${database.files.location}")
    public static String dbFileLocation;

}

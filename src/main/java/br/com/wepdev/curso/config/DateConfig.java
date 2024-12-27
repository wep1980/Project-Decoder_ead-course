package br.com.wepdev.curso.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;


/**
 * Classe de configuracao de data UTC Global
 * OBS: Essa classe não funciona devido a conflito com a lib de specification
 */
//@Configuration  // com essa linhga comentada essa classe para de funcionar
public class DateConfig {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static LocalDateSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));


    @Bean // Bean de configuracao de data format
    @Primary // O Spring prioriza essa config
    public ObjectMapper objectMapper(){
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper().registerModule(module);
    }
}

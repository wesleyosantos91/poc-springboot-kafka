package io.github.wesleyosantos91.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
@ConfigurationProperties(prefix = "app.kafka.dlt.exceptions")
@Slf4j
@Getter
@Setter
public class Exceptions {

    private List<Class<? extends Exception>> recovered;
    private List<Class<? extends Exception>> notRecovered;

    private List<Class<? extends Exception>> parse(List<String> exceptions) {
        log.info("Parse from the list of exceptions {}", exceptions);

        return requireNonNull(exceptions).stream()
                        .map(className -> {
                            try{
                                return Class.forName(className);

                            }catch(ClassNotFoundException e){
                                throw new RuntimeException(e);
                            }
                        }).map(e -> (Class<? extends Exception>)e).collect(Collectors.toList());
    }
}

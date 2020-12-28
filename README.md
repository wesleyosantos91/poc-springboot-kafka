# SPRING KAFKA

## APLICAÇÃO SPRING BOOT
> Projeto para aplicar prova de conceito de spring com kafka (Em desenvolvimento).

# Tecnologias
- Java 11
- Spring Boot 2.4.1.RELEASE
    - spring-boot-starter-web
    - spring-boot-starter-data-jpa
    - spring-boot-devtools
    - spring-kafka
- Apache Avro
- Lombok
- Tomcat (Embedded no Spring Boot)
- Git

# Execução

A execução das aplicações são feitas através do de um comando Maven que envoca a inicialização do Spring Boot.

- Scripts
  ### Executar docker-compose
    - ``` cd src/main/docker/```
    - ```docker-compose -f docker-compose.yml up```
  ### Compilar o modulo BOM
    - ``` ./mvnw clean install```
  ### Executar a aplicação producer
    -  ```cd poc-spring-kafka-producer/ ```
    -  ```./mvnw clean compile spring-boot:run```
  ### Executar a aplicação producer
    -  ```cd poc-spring-kafka-consumer/ ```
    -  ```./mvnw clean compile spring-boot:run```

  # Utilização

    -  Efetuar uma requisição REST com verbo POST na seguinte URL: http://localhost:8080/persons/publish
    - Payload
      ``` 
           {
              "name": "wesley",
              "age": 29,
              "cpf": "00000000000"     
            }
       ```


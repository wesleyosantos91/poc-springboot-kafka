<div align="center">

![](https://img.shields.io/badge/Status-Em%20Desenvolvimento-orange)
</div>

<div align="center">

# POC - Springboot, Java, Schema Registry, Apache Avro e Apache Kafka.
Trata-se de uma aplicação de prova de conceito para desenvolver um consumer com retry e DLT, e um producer kafka, utilizando arquitetura: Springboot, Java, Schema Registry, Apache Avro e Apache Kafka.

![](https://img.shields.io/badge/Autor-Wesley%20Oliveira%20Santos-brightgreen)
![](https://img.shields.io/badge/Language-Java-brightgreen)
![](https://img.shields.io/badge/Framework-Springboot-brightgreen)
![](https://img.shields.io/badge/Framework-Apache%20Avro-brightgreen)
![](https://img.shields.io/badge/Message%20Broker-Apache%20Kafka-brightgreen)

</div> 

## Fundamentos teóricos

> Springboot: O Spring Boot é um projeto da Spring que veio para facilitar o processo de configuração e publicação de nossas aplicações. A intenção é ter o seu projeto rodando o mais rápido possível e sem complicação.

> Apache Kafka: Apache Kafka é uma plataforma open-source de processamento de streams desenvolvida pela Apache Software Foundation, escrita em Scala e Java. O projeto tem como objetivo fornecer uma plataforma unificada, de alta capacidade e baixa latência para tratamento de dados em tempo real.

> Schema Registry: O Schema Registry valida se a mensagem que está sendo enviada por uma aplicação é compatível. Podemos usar vários formatos de arquivos para criar os nossos schemas como XML, CSV, JSON mas aqui usaremos Apache Avro que é um formato desenvolvido para criação de schemas com tipagem.

> Apache avro: O Avro é uma estrutura de serialização de chamada e procedimento remoto orientada a linhas, desenvolvida no projeto Hadoop do Apache. Ele usa JSON para definir tipos e protocolos de dados e serializa dados em um formato binário.

> Kotlin: Kotlin é uma Linguagem de programação multiplataforma, orientada a objetos e funcional, concisa e estaticamente tipada, desenvolvida pela JetBrains em 2011, que compila para a Máquina virtual Java e que também pode ser traduzida para a linguagem JavaScript e compilada para código nativo.

## Tecnologias
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

## Execução

A execução das aplicações são feitas através do de um comando Maven que envoca a inicialização do Spring Boot.

- Scripts
  ### Executar docker-compose
  - ```docker-compose -f docker-compose.yml up```
  ### Compilar o modulo BOM
  - ``` ./mvnw clean install```
  ### Executar a aplicação producer
  -  ```cd poc-spring-kafka-producer/ ```
  -  ```./mvnw clean compile spring-boot:run```
  ### Executar a aplicação producer
  -  ```cd poc-spring-kafka-consumer/ ```
  -  ```./mvnw clean compile spring-boot:run```

## Utilização
- Schema Registry para validar schemas avro cadastrados e versões: http://localhost:8001
- Efetuar uma requisição REST com verbo POST na seguinte URL: http://localhost:8080/persons/publish
- Payload
    ``` 
         {
            "name": "wesley",
            "age": 29,
            "cpf": "00000000000"     
          }
    

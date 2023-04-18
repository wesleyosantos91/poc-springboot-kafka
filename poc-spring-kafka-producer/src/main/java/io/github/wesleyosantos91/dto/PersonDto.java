package io.github.wesleyosantos91.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PersonDto implements Serializable {

    private String name;
    private int age;
    private String cpf;

}

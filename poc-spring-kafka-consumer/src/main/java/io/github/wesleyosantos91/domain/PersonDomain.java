package io.github.wesleyosantos91.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PersonDomain implements Serializable {

    private String name;
    private int age;
    private String cpf;

}

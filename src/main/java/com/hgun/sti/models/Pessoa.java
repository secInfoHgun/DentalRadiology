package com.hgun.sti.models;

import com.hgun.sti.models.abstracts.AbstractEntidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="pessoas")
public class Pessoa extends AbstractEntidade {
    private String nome;
    private String cpf;
    private Integer idade;
    private Character sexo;
    private String preccp;
    private String email;
}

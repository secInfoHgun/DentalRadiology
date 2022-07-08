package com.hgun.sti.models;

import com.hgun.sti.models.abstracts.AbstractEntidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="exames")
public class Exame extends AbstractEntidade {
    @OneToOne
    private Pessoa paciente;

    @OneToOne
    private Usuario dentista;


}

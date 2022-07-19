package com.hgun.sti.models.tipos;

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
@Table(name="atm")
public class Atm extends AbstractEntidade {
    private String nome;
}

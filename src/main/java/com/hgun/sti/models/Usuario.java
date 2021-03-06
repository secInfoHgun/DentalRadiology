package com.hgun.sti.models;

import com.hgun.sti.models.abstracts.AbstractEntidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="usuarios")
public class Usuario extends AbstractEntidade {

    private String login;
    private String senha;

    private String postoGraduacao;
    private String nomeDeGuerra;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Pessoa pessoa;

    @Transient
    private String senhaConfirmada;

    @Transient
    private String inputFilter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();
}

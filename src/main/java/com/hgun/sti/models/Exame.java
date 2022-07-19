package com.hgun.sti.models;

import com.hgun.sti.models.abstracts.AbstractEntidade;
import com.hgun.sti.models.tipos.Dente;
import com.hgun.sti.models.tipos.Interproximal;
import com.hgun.sti.models.tipos.Periapical;
import com.hgun.sti.models.tipos.Tomografia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="exames")
public class Exame extends AbstractEntidade {

    private String observacoes;
    private Date dataSolicitacao;

    private Boolean radiografiaPanoramica;

    private Boolean teleradiografia;

    private Boolean atm;

    private Boolean status = true;

    @Transient
    private String dataInicioFilter;

    @Transient
    private String dataFimFilter;

    @Transient
    private String ativoFilter;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Pessoa paciente;

    @OneToOne
    private Usuario dentista;

    @OneToOne
    private Periapical periapical;

    @OneToOne
    private Tomografia tomografia;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "exames_interproximal",
            joinColumns = @JoinColumn(name = "exame_id"),
            inverseJoinColumns = @JoinColumn(name = "interproximal_id")
    )
    private Set<Interproximal> interproximais = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dentes_periapical",
            joinColumns = @JoinColumn(name = "exame_id"),
            inverseJoinColumns = @JoinColumn(name = "dente_id")
    )
    private Set<Dente> dentesPeriapical = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dentes_tomografia",
            joinColumns = @JoinColumn(name = "exame_id"),
            inverseJoinColumns = @JoinColumn(name = "dente_id")
    )
    private Set<Dente> dentesTomografia = new HashSet<>();

    public String getDataSolicitacaoFormatter(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:YY").format(this.dataSolicitacao);
    }
}

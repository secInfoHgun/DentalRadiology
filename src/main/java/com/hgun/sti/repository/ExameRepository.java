package com.hgun.sti.repository;

import com.hgun.sti.models.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ExameRepository extends JpaRepository<Exame, Long> {

    @Query("SELECT e FROM Exame e WHERE e.status = true")
    List<Exame> findAllAtivo();

    @Query("select e from Exame e where e.dataSolicitacao between :dataInicio and :dataFim")
    List<Exame> findByDataBetween(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

    @Query("SELECT e FROM Exame e WHERE e.status = :ativo")
    List<Exame> findByStatus(@Param("ativo") Boolean ativo);

}

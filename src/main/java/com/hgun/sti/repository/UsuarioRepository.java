package com.hgun.sti.repository;

import com.hgun.sti.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true")
    List<Usuario> findAllAtivo();

    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    public Usuario getUsuarioByLogin(@Param("login") String username);

    @Query(value = "select u.*" +
            "from usuarios u " +
            "left join usuarios_roles ur on u.id = ur.usuario_id " +
            "where (ur.role_id = (select r2.role_id from roles r2 where r2.name like '%USER%') or u.id = :idUsuarioLogado) " +
            "and u.ativo = true", nativeQuery = true)
    List<Usuario> findAllUsers(Long idUsuarioLogado);
}

package com.pesoas.api.repository;

import com.pesoas.api.entity.Contato;
import com.pesoas.api.repository.contatos.ContatoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long>, ContatoRepositoryQuery {
    List<Contato> findByPessoaFisicaId(Long pessoaId);

    @Modifying
    @Query("UPDATE Contato e SET e.principal = 'N' WHERE e.pessoaFisica.id = :pessoaId")
    int marcarTodosComoNaoPrincipalParaPessoa(@Param("pessoaId") Long pessoaId);
}

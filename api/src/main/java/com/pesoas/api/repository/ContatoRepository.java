package com.pesoas.api.repository;

import com.pesoas.api.entity.Contato;
import com.pesoas.api.repository.contatos.ContatoRepositoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long>, ContatoRepositoryQuery {
    List<Contato> findByPessoaId(Long pessoaId);

    @Override
    @Query("SELECT c FROM Contato c JOIN FETCH c.pessoa") // JOIN FETCH é a chave aqui
    Page<Contato> findAll(Pageable pageable);

    // Adicione também para a busca por ID para garantir consistência
    //@Query("SELECT c FROM Contato c JOIN FETCH c.pessoa WHERE c.id = :id")
    //Optional<Contato> findByIdWithPessoa(@Param("id") Long id);
    @Modifying
    @Query("UPDATE Contato e SET e.principal = 'N' WHERE e.pessoa.id = :pessoaId")
    int marcarTodosComoNaoPrincipalParaPessoa(@Param("pessoaId") Long pessoaId);
}

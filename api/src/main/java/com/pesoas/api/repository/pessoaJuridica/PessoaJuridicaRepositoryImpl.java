package com.pesoas.api.repository.pessoaJuridica;

import com.pesoas.api.entity.PessoaJuridica;
import com.pesoas.api.entity.PessoaJuridica_;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaRepositoryImpl implements PessoaJuridicaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<PessoaJuridica> filtrar(PessoaFilter filter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaJuridica> criteria = builder.createQuery(PessoaJuridica.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<PessoaJuridica> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(filter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<PessoaJuridica> filtrar(PessoaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaJuridica> criteria = builder.createQuery(PessoaJuridica.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        TypedQuery<PessoaJuridica> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            PessoaFilter filter, CriteriaBuilder builder, Root<PessoaJuridica> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(filter.getId() != null) {
            predicates.add(builder.equal(root.get(PessoaJuridica_.ID),filter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(filter.getNome())) {
            Expression<String> nomeSemAcentoDoBanco = builder.function(
                    "f_unaccent",
                    String.class,
                    root.get(PessoaJuridica_.NOME)
            );

            String termoBuscaSemAcento = "%" + org.apache.commons.lang3.StringUtils.stripAccents(
                    filter.getNome().toLowerCase()
            ) + "%";

            predicates.add(
                    builder.like(builder.lower(nomeSemAcentoDoBanco), termoBuscaSemAcento)
            );
        }

        // CNPJ
        if (StringUtils.hasLength(filter.getCpf())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(PessoaJuridica_.CNPJ)),
                            "%" + filter.getCpf() + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(PessoaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<PessoaJuridica> root = criteria.from(PessoaJuridica.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}

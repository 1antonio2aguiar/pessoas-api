package com.pesoas.api.repository.cidades;

import com.pesoas.api.entity.Cidade;
import com.pesoas.api.entity.Cidade_;
import com.pesoas.api.filter.enderecos.CidadeFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CidadeRepositoryImpl implements CidadeRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Cidade> filtrar(CidadeFilter cidadeFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cidade> criteria = builder.createQuery(Cidade.class);
        Root<Cidade> root = criteria.from(Cidade.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(cidadeFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Cidade> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(cidadeFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Cidade> filtrar(CidadeFilter cidadeFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cidade> criteria = builder.createQuery(Cidade.class);
        Root<Cidade> root = criteria.from(Cidade.class);

        Predicate[] predicates = criarRestricoes(cidadeFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Cidade> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            CidadeFilter cidadeFilter, CriteriaBuilder builder, Root<Cidade> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(cidadeFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Cidade_.ID), cidadeFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(cidadeFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Cidade_.NOME)),
                            "%" + cidadeFilter.getNome().toLowerCase() + "%"));
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

    private Long total(CidadeFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Cidade> root = criteria.from(Cidade.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
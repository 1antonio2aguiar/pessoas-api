package com.pesoas.api.repository.bairros;

import com.pesoas.api.entity.Bairro_;
import com.pesoas.api.entity.Cidade_;
import com.pesoas.api.filter.enderecos.BairroFilter;
import com.pesoas.api.entity.Bairro;
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

public class BairroRepositoryImpl implements BairroRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Bairro> filtrar(BairroFilter bairroFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Bairro> criteria = builder.createQuery(Bairro.class);
        Root<Bairro> root = criteria.from(Bairro.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(bairroFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Bairro> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(bairroFilter));
    }

    //Aqui da lista sem paginacao
    @Override
    public List<Bairro> filtrar(BairroFilter bairroFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Bairro> criteria = builder.createQuery(Bairro.class);
        Root<Bairro> root = criteria.from(Bairro.class);

        Predicate[] predicates = criarRestricoes(bairroFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Bairro> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            BairroFilter bairroFilter, CriteriaBuilder builder, Root<Bairro> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(bairroFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Bairro_.ID), bairroFilter.getId()));
        }

        // NOME DO BAIRRO
        if (StringUtils.hasLength(bairroFilter.getNome())) {
            predicates.add(
                builder.like(
                    builder.lower(root.get(Bairro_.NOME)),
                    "%" + bairroFilter.getNome().toLowerCase() + "%"));
        }

        // NOME DA CIDADE
        if (StringUtils.hasLength(bairroFilter.getDistritoFilter().getCidadeFilter().getNome())) {
            predicates.add(
                builder.like(
                    builder.lower(root.get(Bairro_.DISTRITO).get(Cidade_.NOME)),
                    "%" + bairroFilter.getDistritoFilter().getCidadeFilter().getNome().toLowerCase() + "%"));
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

    private Long total(BairroFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Bairro> root = criteria.from(Bairro.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
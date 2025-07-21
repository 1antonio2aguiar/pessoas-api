package com.pesoas.api.repository.ceps;

import com.pesoas.api.entity.Cep_;
import com.pesoas.api.filter.enderecos.CepFilter;
import com.pesoas.api.entity.Cep;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class CepRepositoryImpl implements CepRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Cep> filtrar(CepFilter cepFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cep> criteria = builder.createQuery(Cep.class);
        Root<Cep> root = criteria.from(Cep.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(cepFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Cep> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(cepFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Cep> filtrar(CepFilter cepFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cep> criteria = builder.createQuery(Cep.class);
        Root<Cep> root = criteria.from(Cep.class);

        Predicate[] predicates = criarRestricoes(cepFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Cep> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            CepFilter cepFilter, CriteriaBuilder builder, Root<Cep> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        if(cepFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Cep_.ID), cepFilter.getId()));
        }

        // CEP
        if(cepFilter.getCep() != null) {
            predicates.add(builder.equal(root.get(Cep_.CEP), cepFilter.getCep()));
        }

        // NOME DO LOGRADOURO
        /*if (StringUtils.hasLength(cepFilter.getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Cep_.NOME)),
                            "%" + cepFilter.getNome().toLowerCase() + "%"));
        }*/

        // NOME DA CIDADE
        /*if (StringUtils.hasLength(cepFilter.getDistritoFilter().getCidadeFilter().getNome())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Cep_.DISTRITO).get(Cidade_.NOME)),
                            "%" + cepFilter.getDistritoFilter().getCidadeFilter().getNome().toLowerCase() + "%"));
        }*/

        return predicates.toArray(new Predicate[predicates.size()]);
    }
    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(CepFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Cep> root = criteria.from(Cep.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
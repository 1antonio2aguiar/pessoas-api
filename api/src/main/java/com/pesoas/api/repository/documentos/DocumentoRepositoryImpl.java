package com.pesoas.api.repository.documentos;

import com.pesoas.api.entity.Documento;
import com.pesoas.api.filter.enderecos.DocumentoFilter;
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

public class DocumentoRepositoryImpl implements DocumentoRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;
    @Override
    public Page<Documento> filtrar(DocumentoFilter documentoFilter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Documento> criteria = builder.createQuery(Documento.class);
        Root<Documento> root = criteria.from(Documento.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(documentoFilter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Documento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(documentoFilter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Documento> filtrar(DocumentoFilter documentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Documento> criteria = builder.createQuery(Documento.class);
        Root<Documento> root = criteria.from(Documento.class);

        Predicate[] predicates = criarRestricoes(documentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Documento> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    private Predicate[] criarRestricoes(
            DocumentoFilter documentoFilter, CriteriaBuilder builder, Root<Documento> root) {

        List<Predicate> predicates = new ArrayList<>();

        // ID
        /*if(documentoFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Documento_.ID), documentoFilter.getId()));
        }*/

        // PESSOA
        /*if (StringUtils.hasLength(documentoFilter.getPessoa())) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(Documento_.PESSOA_FISICA).get(PessoaFisica_.NOME)),
                            "%" + documentoFilter.getPessoa().toLowerCase() + "%"));
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

    private Long total(DocumentoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Documento> root = criteria.from(Documento.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
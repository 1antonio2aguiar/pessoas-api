package com.pesoas.api.repository.pessoas;

import com.pesoas.api.entity.*;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Pessoa> filtrar(PessoaFilter filter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        List<Order> orders = QueryUtils.toOrders(pageable.getSort(), root, builder);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates).orderBy(orders);

        TypedQuery<Pessoa> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(filter));

    }

    //Aqui da lista sem paginacao
    @Override
    public List<Pessoa> filtrar(PessoaFilter pessoaFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Pessoa> query = manager.createQuery(criteria);
        return query.getResultList();
    }

    // Em PessoaRepositoryImpl.java

    @Override
    public List<Pessoa> pesquisarPorNomeCpfCnpj(String termo) {
        // ... (lógica inicial de verificação do termo) ...

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        String termoNumerico = termo.replaceAll("\\D", "");
        boolean isNumeric = StringUtils.hasText(termoNumerico) && termo.matches("^[0-9./-]*$");

        Predicate predicate;

        if (isNumeric) {
            // ... (lógica para CPF e CNPJ permanece a mesma) ...
            if (termoNumerico.length() > 11) {
                // BUSCA APENAS POR CNPJ
                System.err.println("Modo de busca: CNPJ com '" + termoNumerico + "'");
                Root<PessoaJuridica> rootPj = builder.treat(root, PessoaJuridica.class);
                predicate = builder.like(rootPj.get(PessoaJuridica_.CNPJ), termoNumerico + "%");
            } else {
                // BUSCA APENAS POR CPF
                System.err.println("Modo de busca: CPF com '" + termoNumerico + "'");
                Root<PessoaFisica> rootPf = builder.treat(root, PessoaFisica.class);
                predicate = builder.like(rootPf.get(PessoaFisica_.CPF), termoNumerico + "%");
            }

        } else {
            // --- BUSCA APENAS POR NOME (COM A LÓGICA DE UNACCENT) ---
            //System.err.println("Modo de busca: NOME sem ACENTOS com '" + termo + "'");

            // 1. Aplica f_unaccent e lower na COLUNA do banco de dados.
            Expression<String> nomeSemAcentoDoBanco = builder.function(
                    "f_unaccent",
                    String.class,
                    root.get(Pessoa_.NOME)
            );

            // 2. Prepara o PARÂMETRO de busca (o termo) em Java, sem acentos e com wildcards.
            String termoBusca = "%" + org.apache.commons.lang3.StringUtils.stripAccents(termo.toLowerCase()) + "%";

            // 3. O predicado 'like' compara a coluna processada no banco com o parâmetro preparado em Java.
            predicate = builder.like(builder.lower(nomeSemAcentoDoBanco), termoBusca);
        }

        criteria.where(predicate);
        criteria.orderBy(builder.asc(root.get(Pessoa_.NOME)));

        TypedQuery<Pessoa> query = manager.createQuery(criteria);
        query.setMaxResults(15);

        try {
            // Tenta executar a consulta normalmente.
            return query.getResultList();
        } catch (Exception e) {
            //return new ArrayList<>(); // Ou Collections.emptyList();
            return Collections.emptyList();
        }
    }

    private Predicate[] criarRestricoes(
            PessoaFilter pessoaFilter, CriteriaBuilder builder, Root<Pessoa> root) {

        List<Predicate> predicates = new ArrayList<>();

        // Isto é crucial para poder filtrar por campos de PessoaFisica e PessoaJuridica
        Root<PessoaFisica> rootPf = builder.treat(root, PessoaFisica.class);
        Root<PessoaJuridica> rootPj = builder.treat(root, PessoaJuridica.class);

        // ID
        if(pessoaFilter.getId() != null) {
            predicates.add(builder.equal(root.get(Pessoa_.ID), pessoaFilter.getId()));
        }

        // NOME
        if (StringUtils.hasLength(pessoaFilter.getNome())) {
            // 1. Cria uma expressão que chama a função 'f_unaccent' no campo 'nome'
            Expression<String> nomeSemAcentoDoBanco = builder.function(
                    "f_unaccent",          // Nome da função no banco de dados
                    String.class,          // Tipo de retorno da função
                    root.get(Pessoa_.NOME) // Argumento(s) para a função
            );

            // 2. Prepara o termo de busca (parâmetro) também sem acentos e em minúsculo
            String nomeFiltroSemAcento = org.apache.commons.lang3.StringUtils.stripAccents(
                    pessoaFilter.getNome().toLowerCase()
            );

            // 3. Adiciona o predicado 'like' comparando ambos os valores sem acento
            predicates.add(
                    builder.like(
                            builder.lower(nomeSemAcentoDoBanco), // Aplica lower() na expressão da função
                            "%" + nomeFiltroSemAcento + "%"
                    )
            );
        }

        // CPF - *** CORRIGIDO PARA USAR O JOIN ***
        if (StringUtils.hasLength(pessoaFilter.getCpf())) {
            // O predicado do CPF só deve ser aplicado se a pessoa FOR do tipo 'F'
            Predicate tipoPessoaF = builder.equal(root.type(), PessoaFisica.class);
            Predicate cpfMatch = builder.like(
                    builder.lower(rootPf.get(PessoaFisica_.CPF)),
                    "%" + pessoaFilter.getCpf() + "%");
            predicates.add(builder.and(tipoPessoaF, cpfMatch));
        }

        // CNPJ - *** CORRIGIDO PARA USAR O 'root' TRATADO ***
        if (StringUtils.hasLength(pessoaFilter.getCnpj())) {
            // O predicado do CNPJ só deve ser aplicado se a pessoa FOR do tipo 'J'
            Predicate tipoPessoaJ = builder.equal(root.type(), PessoaJuridica.class);
            Predicate cnpjMatch = builder.like(
                    builder.lower(rootPj.get(PessoaJuridica_.CNPJ)),
                    "%" + pessoaFilter.getCnpj() + "%");
            predicates.add(builder.and(tipoPessoaJ, cnpjMatch));
        }

        // DATA NASCIMENTO - *** CORRIGIDO PARA USAR O 'root' TRATADO ***
        if(pessoaFilter.getDataNascimento() != null){
            LocalDate startDateLocalDate = Instant.ofEpochMilli(pessoaFilter.getDataNascimento().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            // O predicado de data de nascimento só deve ser aplicado se a pessoa FOR do tipo 'F'
            Predicate tipoPessoaF = builder.equal(root.type(), PessoaFisica.class);
            Predicate dataNascMatch = builder.equal(rootPf.get(PessoaFisica_.DATA_NASCIMENTO), startDateLocalDate);
            predicates.add(builder.and(tipoPessoaF, dataNascMatch));
        }

        //DATA NASCIMENTO
        /*if(pessoaFilter.getDataNascimento() != null){
            // Deve vir do frontEnd no formato MM/dd/yyyy

            Date data = pessoaFilter.getDataNascimento();

            Date startDate = DateUtils.truncate(data, Calendar.DATE);
            Date endDate = DateUtils.addSeconds(DateUtils.addDays(startDate, 1), -1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Note o formato
            LocalDate startDateLocalDate = Instant.ofEpochMilli(startDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate endDateLocalDate = Instant.ofEpochMilli(endDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            endDateLocalDate = startDateLocalDate.plusDays(0);

            System.err.println("Datas " + startDateLocalDate + " " + endDateLocalDate);

            predicates.add(
                    builder.between(root.get(PessoaFisica_.DATA_NASCIMENTO), startDateLocalDate, endDateLocalDate));
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

    private Long total(PessoaFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = criarRestricoes(filter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}

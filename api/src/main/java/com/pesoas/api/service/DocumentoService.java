package com.pesoas.api.service;

import com.pesoas.api.DTO.documentos.DadosInsertDocumentoRcd;
import com.pesoas.api.DTO.documentos.DadosListDocumentoRcd;
import com.pesoas.api.DTO.documentos.DadosUpdateDocumentoRcd;
import com.pesoas.api.entity.Documento;
import com.pesoas.api.entity.Pessoa;
import com.pesoas.api.entity.enuns.TipoDocumento;
import com.pesoas.api.filter.enderecos.DocumentoFilter;
import com.pesoas.api.repository.DocumentoRepository;
import com.pesoas.api.repository.PessoaRepository;
import com.pesoas.api.service.exceptions.DatabaseException;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentoService {
    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private PessoaRepository pessoaRepository;

    @Transactional(readOnly = true)
    public Page<DadosListDocumentoRcd> findAllPaginated(Pageable paginacao) {
        Page<Documento> docPage = documentoRepository.findAll(paginacao);
        return docPage.map(DadosListDocumentoRcd::new);
    }

    @Transactional(readOnly = true)
    public List<DadosListDocumentoRcd> pesquisarSemPaginacao(DocumentoFilter filter) {
        List<Documento> doc = documentoRepository.filtrar(filter);
        return doc.stream()
                .map(DadosListDocumentoRcd::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DadosListDocumentoRcd> pesquisarComPaginacao(DocumentoFilter filter, Pageable pageable) {
        Page<Documento> docPage = documentoRepository.filtrar(filter, pageable);
        return docPage.map(DadosListDocumentoRcd::new);
    }

    @Transactional(readOnly = true) // Importante para operações de leitura
    public DadosListDocumentoRcd findById(Long id) {
        Documento doc = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado: " + id));
        return new DadosListDocumentoRcd(doc);
    }
    @Transactional(readOnly = true)
    public List<DadosListDocumentoRcd> findDocumentosByPessoaId(Long pessoaId) {
        // Verificar se a pessoa existe
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new ObjectNotFoundException("Pessoa com ID " + pessoaId + " não encontrada.");
        }
        List<Documento> doc = documentoRepository.findByPessoaId(pessoaId);
        return doc.stream()
            .map(DadosListDocumentoRcd::new)
            .collect(Collectors.toList());
    }

    //Insert
    public Documento insert(DadosInsertDocumentoRcd dados){
        Documento documento = new Documento();
        BeanUtils.copyProperties(dados, documento, "id");

        //Busco a pessoa
        Pessoa pessoa = pessoaRepository.findById(dados.pessoaId())
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa com ID " + dados.pessoaId() + " não encontrada."));
        documento.setPessoa(pessoa);

        // set o enum
        documento.setTipoDocumento(TipoDocumento.toTipoDocumentoEnum(dados.tipoDocumento()));

        Documento docInsert = documentoRepository.save(documento);
        return docInsert;
    }

    // update
    public Documento update(Long id, DadosUpdateDocumentoRcd dados){
        
        Documento docUpd = documentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Documento não cadastrado. Id: " + id));
        BeanUtils.copyProperties(dados, docUpd, "id");
        // set o enum
        docUpd.setTipoDocumento(TipoDocumento.toTipoDocumentoEnum(dados.tipoDocumento()));

        return documentoRepository.save(docUpd);
    }

    // Delete
    public void delete(Long id){
        Documento docDel = documentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Documento não cadastrado. Id: " + id));
        try {
            documentoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }
}
package com.pesoas.api.controller;

import com.pesoas.api.DTO.documentos.DadosInsertDocumentoRcd;
import com.pesoas.api.DTO.documentos.DadosListDocumentoRcd;
import com.pesoas.api.DTO.documentos.DadosUpdateDocumentoRcd;
import com.pesoas.api.filter.enderecos.DocumentoFilter;
import com.pesoas.api.service.DocumentoService;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoController {
    @Autowired private DocumentoService documentoService;

    // Listar de documentos
    @GetMapping
    public Page<DadosListDocumentoRcd> findall(@PageableDefault(sort={"id"}) Pageable paginacao) {
        return documentoService.findAllPaginated(paginacao);
    }

    // Lista de documentos com filter
    @GetMapping("/list")
    public List<DadosListDocumentoRcd> pesquisar(DocumentoFilter filter) {
        return documentoService.pesquisarSemPaginacao(filter);
    }

    // Lista de documentos com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListDocumentoRcd> pesquisar(DocumentoFilter filter, Pageable pageable) {
        return documentoService.pesquisarComPaginacao(filter, pageable);
    }

    // documento por id
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<DadosListDocumentoRcd> findById(@PathVariable Long id) {
        DadosListDocumentoRcd docDto = documentoService.findById(id);
        return ResponseEntity.ok().body(docDto);
    }

    @GetMapping("/por-pessoa/{pessoaId}")
    public ResponseEntity<List<DadosListDocumentoRcd>> findDocumentoPorPessoa(@PathVariable Long pessoaId) {
        try {
            List<DadosListDocumentoRcd> documentos = documentoService.findDocumentosByPessoaId(pessoaId);
            return ResponseEntity.ok(documentos); // Retorna 200 OK mesmo se a lista for vazia
        } catch (ObjectNotFoundException e) {
            // Se você tiver um @ControllerAdvice para ObjectNotFoundException, ele cuidará disso.
            // Caso contrário, você pode retornar 404 aqui:
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou uma mensagem de erro
        }
    }

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertDocumentoRcd dados){
        var documentoSalva = documentoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(documentoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListDocumentoRcd(documentoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateDocumentoRcd dados){
        var salva = documentoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListDocumentoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        documentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



package com.pesoas.api.controller;

import com.pesoas.api.DTO.enderrecos.DadosInsertEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosListEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosUpdateEnderecoRcd;
import com.pesoas.api.filter.enderecos.EnderecoFilter;
import com.pesoas.api.service.EnderecoService;
import com.pesoas.api.service.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    @Autowired private EnderecoService enderecoService;

    // Listar de enderecos
    @GetMapping
    public Page<DadosListEnderecoRcd> findall(@PageableDefault(sort={"id"}) Pageable paginacao) {
        return enderecoService.findAllPaginated(paginacao);
    }

    // Lista de Ceps com filter
    @GetMapping("/list")
    public List<DadosListEnderecoRcd> pesquisar(EnderecoFilter filter) {
        return enderecoService.pesquisarSemPaginacao(filter);
    }

    // Lista de enderecos com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListEnderecoRcd> pesquisar(EnderecoFilter filter, Pageable pageable) {
        return enderecoService.pesquisarComPaginacao(filter, pageable);
    }

    // cep por id
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<DadosListEnderecoRcd> findById(@PathVariable Long id) {
        DadosListEnderecoRcd enderecoDto = enderecoService.findById(id);
        return ResponseEntity.ok().body(enderecoDto);
    }

    @GetMapping("/por-pessoa/{pessoaId}")
    public ResponseEntity<List<DadosListEnderecoRcd>> findEnderecosPorPessoa(@PathVariable Long pessoaId) {
        try {
            List<DadosListEnderecoRcd> enderecos = enderecoService.findEnderecosByPessoaId(pessoaId);
            return ResponseEntity.ok(enderecos); // Retorna 200 OK mesmo se a lista for vazia
        } catch (ObjectNotFoundException e) {
            // Se você tiver um @ControllerAdvice para ObjectNotFoundException, ele cuidará disso.
            // Caso contrário, você pode retornar 404 aqui:
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou uma mensagem de erro
        }
    }

    // cep por cep
    /*@GetMapping(value = "/numero/{cep}")
    public ResponseEntity<DadosListEnderecoRcd> findByCep(@PathVariable String cep) {
        DadosListEnderecoRcd enderecoRcd = enderecoService.findByCep(cep);
        return ResponseEntity.ok().body(enderecoRcd);
    }*/

    @PutMapping("/{enderecoId}/definir-como-principal")
    public ResponseEntity<Void> definirComoPrincipal(
            @PathVariable Long enderecoId,
            @RequestParam Long pessoaId) { // Ou obter pessoaId do usuário autenticado, se aplicável
        try {
            enderecoService.definirEnderecoComoPrincipal(pessoaId, enderecoId);
            return ResponseEntity.ok().build(); // Retorna 200 OK sem corpo se sucesso
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 se o endereço não pertence à pessoa
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 se o endereço não for encontrado
        } catch (Exception e) {
            // Logar o erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertEnderecoRcd dados){
        var enderecoSalva = enderecoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(enderecoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListEnderecoRcd(enderecoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEnderecoRcd dados){
        var salva = enderecoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListEnderecoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



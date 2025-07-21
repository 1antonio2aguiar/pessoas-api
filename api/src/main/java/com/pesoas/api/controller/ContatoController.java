package com.pesoas.api.controller;

import com.pesoas.api.DTO.contatos.DadosInsertContatoRcd;
import com.pesoas.api.DTO.contatos.DadosListContatoRcd;
import com.pesoas.api.DTO.contatos.DadosUpdateContatoRcd;
import com.pesoas.api.DTO.enderrecos.DadosInsertEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosListEnderecoRcd;
import com.pesoas.api.DTO.enderrecos.DadosUpdateEnderecoRcd;
import com.pesoas.api.filter.enderecos.ContatoFilter;
import com.pesoas.api.filter.enderecos.EnderecoFilter;
import com.pesoas.api.service.ContatoService;
import com.pesoas.api.service.EnderecoService;
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
@RequestMapping("/contato")
public class ContatoController {
    @Autowired private ContatoService contatoService;

    // Listar de contatos
    @GetMapping
    public Page<DadosListContatoRcd> findall(@PageableDefault(sort={"id"}) Pageable paginacao) {
        return contatoService.findAllPaginated(paginacao);
    }

    // Lista de contatos com filter
    @GetMapping("/list")
    public List<DadosListContatoRcd> pesquisar(ContatoFilter filter) {
        return contatoService.pesquisarSemPaginacao(filter);
    }

    // Lista de contatos com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListContatoRcd> pesquisar(ContatoFilter filter, Pageable pageable) {
        return contatoService.pesquisarComPaginacao(filter, pageable);
    }

    // contato por id
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<DadosListContatoRcd> findById(@PathVariable Long id) {
        DadosListContatoRcd contatoDto = contatoService.findById(id);
        return ResponseEntity.ok().body(contatoDto);
    }

    @GetMapping("/por-pessoa/{pessoaId}")
    public ResponseEntity<List<DadosListContatoRcd>> findContatoPorPessoa(@PathVariable Long pessoaId) {
        try {
            List<DadosListContatoRcd> contatos = contatoService.findContatosByPessoaId(pessoaId);
            return ResponseEntity.ok(contatos); // Retorna 200 OK mesmo se a lista for vazia
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
            @PathVariable Long contatoId,
            @RequestParam Long pessoaId) { // Ou obter pessoaId do usuário autenticado, se aplicável
        try {
            contatoService.definirContatoComoPrincipal(pessoaId, contatoId);
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
    public ResponseEntity insert(@RequestBody @Valid DadosInsertContatoRcd dados){
        var contatoSalva = contatoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(contatoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListContatoRcd(contatoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateContatoRcd dados){
        var salva = contatoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListContatoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        contatoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



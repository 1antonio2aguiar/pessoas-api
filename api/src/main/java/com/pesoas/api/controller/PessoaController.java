package com.pesoas.api.controller;

import com.pesoas.api.DTO.pessoas.fj.DadosListPessoaGeralRcd;
import com.pesoas.api.DTO.pessoas.fj.DadosPessoaFjReduzidoRcd;
import com.pesoas.api.filter.pessoas.PessoaFilter;
import com.pesoas.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/{id}")
    public ResponseEntity<DadosPessoaFjReduzidoRcd> getByIdReduzido(@PathVariable Long id) {
        DadosPessoaFjReduzidoRcd pessoaDto = pessoaService.findByIdReduzido(id);
        return ResponseEntity.ok(pessoaDto);
    }

    // **** NOVO ENDPOINT AQUI ****
    /**
     * Busca uma pessoa por ID e retorna o DTO COMPLETO (Geral).
     * Atende à nova chamada do Feign Client para dados completos.
     */
    @GetMapping("/{id}/completo")
    public ResponseEntity<DadosListPessoaGeralRcd> getByIdCompleto(@PathVariable Long id) {
        DadosListPessoaGeralRcd pessoaDto = pessoaService.findByIdCompleto(id);
        return ResponseEntity.ok(pessoaDto);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<Page<DadosListPessoaGeralRcd>> filtrarPessoas(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataNascimento,
            Pageable pageable
    ) {
        // 1. Monta o objeto PessoaFilter manualmente com os parâmetros recebidos.
        PessoaFilter filter = new PessoaFilter();
        filter.setId(id);
        filter.setNome(nome);
        filter.setCpf(cpf);
        filter.setCnpj(cnpj);
        filter.setDataNascimento(dataNascimento);

        // 2. Chama o serviço com o objeto de filtro montado.
        Page<DadosListPessoaGeralRcd> paginaDeResultados = pessoaService.buscarComFiltro(filter, pageable);

        return ResponseEntity.ok(paginaDeResultados);
    }

    @GetMapping // Mapeado para GET /pessoaFisica?ids=1,2,3
    public ResponseEntity<List<DadosPessoaFjReduzidoRcd>> getByIds(@RequestParam("ids") Set<Long> ids) {
        List<DadosPessoaFjReduzidoRcd> pessoasDtos = pessoaService.findByIds(ids);
        return ResponseEntity.ok(pessoasDtos);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<?> pesquisarPessoasPorTermo(
            @RequestParam("termo") String termo,
            @RequestParam(value = "completo", required = false, defaultValue = "false") boolean completo
    ) {
        if (completo) {
            List<DadosListPessoaGeralRcd> resultados = pessoaService.pesquisarPorNomeCpfCnpjAll(termo);
            return ResponseEntity.ok(resultados);
        } else {
            List<DadosPessoaFjReduzidoRcd> resultados = pessoaService.pesquisarPorNomeCpfCnpj(termo);
            return ResponseEntity.ok(resultados);
        }
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



package com.pesoas.api.filter.pessoas;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PessoaFisicaFilter {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
}

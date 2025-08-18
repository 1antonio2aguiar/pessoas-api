package com.pesoas.api.DTO.documentos;
import com.pesoas.api.entity.Documento;
import java.time.LocalDate;

public record DadosListDocumentoRcd(
        Long id,
        String pessoaNome,
        String numeroDocumento,
        Integer tipoDocumento, // tipoDocumento do Enum
        String tipoDocumentoDescricao, // descricao do Enum
        String orgaoExpedidor,
        LocalDate dataExpedicao,
        LocalDate dataValidade,
        String complemento

) {
    public DadosListDocumentoRcd(Documento doc) {
        this(doc.getId(),
                doc.getPessoa().getNome(),
                doc.getNumeroDocumento(),
                doc.getTipoDocumento() != null ? doc.getTipoDocumento().getCodigo() : null,
                doc.getTipoDocumento() != null ? doc.getTipoDocumento().getDescricao() : null,
                doc.getOrgaoExpedidor(),
                doc.getDataExpedicao(),
                doc.getDataValidade(),
                doc.getComplemento()
        );
    }
}
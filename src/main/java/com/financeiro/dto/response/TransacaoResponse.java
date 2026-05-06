package com.financeiro.dto.response;

import com.financeiro.entity.Transacao;
import com.financeiro.enums.Categoria;
import com.financeiro.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        Categoria categoria,
        LocalDate data,
        String observacao
) {
    public static TransacaoResponse from(Transacao t) {
        return new TransacaoResponse(
                t.getId(),
                t.getDescricao(),
                t.getValor(),
                t.getTipo(),
                t.getCategoria(),
                t.getData(),
                t.getObservacao()
        );
    }
}

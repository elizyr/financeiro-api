package com.financeiro.dto.response;

import java.math.BigDecimal;

public record ResumoResponse(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {
    public static ResumoResponse of(BigDecimal receitas, BigDecimal despesas) {
        return new ResumoResponse(receitas, despesas, receitas.subtract(despesas));
    }
}

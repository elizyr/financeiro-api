package com.financeiro.dto.request;

import com.financeiro.enums.Categoria;
import com.financeiro.enums.TipoTransacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Tipo é obrigatório")
        TipoTransacao tipo,

        @NotNull(message = "Categoria é obrigatória")
        Categoria categoria,

        @NotNull(message = "Data é obrigatória")
        LocalDate data,

        String observacao
) {}

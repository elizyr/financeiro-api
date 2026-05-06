package com.financeiro.controller;

import com.financeiro.dto.request.TransacaoRequest;
import com.financeiro.dto.response.ResumoResponse;
import com.financeiro.dto.response.TransacaoResponse;
import com.financeiro.entity.Usuario;
import com.financeiro.enums.TipoTransacao;
import com.financeiro.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transações", description = "Gerenciamento de receitas e despesas")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    @Operation(summary = "Criar nova transação")
    public ResponseEntity<TransacaoResponse> criar(
            @Valid @RequestBody TransacaoRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transacaoService.criar(request, usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todas as transações")
    public ResponseEntity<List<TransacaoResponse>> listarTodas(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.listarTodas(usuario));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar por tipo (RECEITA ou DESPESA)")
    public ResponseEntity<List<TransacaoResponse>> listarPorTipo(
            @PathVariable TipoTransacao tipo,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.listarPorTipo(usuario, tipo));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar por período")
    public ResponseEntity<List<TransacaoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.listarPorPeriodo(usuario, inicio, fim));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public ResponseEntity<TransacaoResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id, usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação")
    public ResponseEntity<TransacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.atualizar(id, request, usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar transação")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        transacaoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo geral (saldo, receitas e despesas)")
    public ResponseEntity<ResumoResponse> resumo(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.resumo(usuario));
    }

    @GetMapping("/resumo/periodo")
    @Operation(summary = "Resumo por período")
    public ResponseEntity<ResumoResponse> resumoPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(transacaoService.resumoPorPeriodo(usuario, inicio, fim));
    }
}

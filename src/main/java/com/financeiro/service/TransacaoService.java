package com.financeiro.service;

import com.financeiro.dto.request.TransacaoRequest;
import com.financeiro.dto.response.ResumoResponse;
import com.financeiro.dto.response.TransacaoResponse;
import com.financeiro.entity.Transacao;
import com.financeiro.entity.Usuario;
import com.financeiro.enums.TipoTransacao;
import com.financeiro.exception.BusinessException;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    @Transactional
    public TransacaoResponse criar(TransacaoRequest request, Usuario usuario) {
        Transacao transacao = Transacao.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .tipo(request.tipo())
                .categoria(request.categoria())
                .data(request.data())
                .observacao(request.observacao())
                .usuario(usuario)
                .build();

        return TransacaoResponse.from(transacaoRepository.save(transacao));
    }

    public List<TransacaoResponse> listarTodas(Usuario usuario) {
        return transacaoRepository
                .findByUsuarioIdOrderByDataDesc(usuario.getId())
                .stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    public List<TransacaoResponse> listarPorTipo(Usuario usuario, TipoTransacao tipo) {
        return transacaoRepository
                .findByUsuarioIdAndTipoOrderByDataDesc(usuario.getId(), tipo)
                .stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    public List<TransacaoResponse> listarPorPeriodo(Usuario usuario,
                                                     LocalDate inicio,
                                                     LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new BusinessException("Data início não pode ser maior que data fim");
        }
        return transacaoRepository
                .findByUsuarioIdAndDataBetweenOrderByDataDesc(usuario.getId(), inicio, fim)
                .stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    public TransacaoResponse buscarPorId(Long id, Usuario usuario) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));

        if (!transacao.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Acesso negado a esta transação");
        }

        return TransacaoResponse.from(transacao);
    }

    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoRequest request, Usuario usuario) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));

        if (!transacao.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Acesso negado a esta transação");
        }

        transacao.setDescricao(request.descricao());
        transacao.setValor(request.valor());
        transacao.setTipo(request.tipo());
        transacao.setCategoria(request.categoria());
        transacao.setData(request.data());
        transacao.setObservacao(request.observacao());

        return TransacaoResponse.from(transacaoRepository.save(transacao));
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));

        if (!transacao.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Acesso negado a esta transação");
        }

        transacaoRepository.delete(transacao);
    }

    public ResumoResponse resumo(Usuario usuario) {
        BigDecimal receitas = transacaoRepository
                .sumByUsuarioIdAndTipo(usuario.getId(), TipoTransacao.RECEITA);
        BigDecimal despesas = transacaoRepository
                .sumByUsuarioIdAndTipo(usuario.getId(), TipoTransacao.DESPESA);
        return ResumoResponse.of(receitas, despesas);
    }

    public ResumoResponse resumoPorPeriodo(Usuario usuario, LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new BusinessException("Data início não pode ser maior que data fim");
        }
        BigDecimal receitas = transacaoRepository
                .sumByUsuarioIdAndTipoAndPeriodo(usuario.getId(), TipoTransacao.RECEITA, inicio, fim);
        BigDecimal despesas = transacaoRepository
                .sumByUsuarioIdAndTipoAndPeriodo(usuario.getId(), TipoTransacao.DESPESA, inicio, fim);
        return ResumoResponse.of(receitas, despesas);
    }
}

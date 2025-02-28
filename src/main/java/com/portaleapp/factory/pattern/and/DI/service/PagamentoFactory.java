package com.portaleapp.factory.pattern.and.DI.service;

import com.portaleapp.factory.pattern.and.DI.model.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PagamentoFactory {
    private final Map<String, Pagamento> metodiPagamento;

    @Autowired
    public PagamentoFactory(Map<String, Pagamento> metodiPagamento) {
        this.metodiPagamento = metodiPagamento;
    }

    public Pagamento getPagamento(String tipo, double importo) {
        Pagamento pagamento = metodiPagamento.get(tipo.toLowerCase());
        if (pagamento == null) {
            throw new IllegalArgumentException("Tipo di pagamento non valido");
        }
        pagamento.setImporto(importo);
        return pagamento;
    }
}

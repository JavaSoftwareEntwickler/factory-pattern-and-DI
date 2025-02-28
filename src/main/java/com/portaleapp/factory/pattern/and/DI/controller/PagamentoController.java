package com.portaleapp.factory.pattern.and.DI.controller;


import com.portaleapp.factory.pattern.and.DI.model.Pagamento;
import com.portaleapp.factory.pattern.and.DI.service.PagamentoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamenti")
public class PagamentoController {
    private final PagamentoFactory pagamentoFactory;

    @Autowired
    public PagamentoController(PagamentoFactory pagamentoFactory) {
        this.pagamentoFactory = pagamentoFactory;
    }

    @PostMapping("/{tipo}/{importo}")
    public String effettuaPagamento(@PathVariable String tipo, @PathVariable double importo) {
        Pagamento pagamento = pagamentoFactory.getPagamento(tipo, importo);
        pagamento.paga();
        return "Pagamento di " + importo + "€ elaborato con successo via " + tipo;
    }
}
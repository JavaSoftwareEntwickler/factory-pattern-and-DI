package com.portaleapp.factory.pattern.and.DI.model;

import org.springframework.stereotype.Component;

@Component("carta")
public class CartaDiCredito extends Pagamento {
    @Override
    public void paga() {
        System.out.println("Pagamento di " + importo + "€ effettuato con Carta di Credito.");
    }
}

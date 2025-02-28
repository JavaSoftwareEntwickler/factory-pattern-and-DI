package com.portaleapp.factory.pattern.and.DI.model;

import org.springframework.stereotype.Component;

@Component("bonifico")
public class BonificoBancario extends Pagamento {
    @Override
    public void paga() {
        System.out.println("Pagamento di " + importo + "€ effettuato con Bonifico Bancario.");
    }
}

package com.portaleapp.factory.pattern.and.DI.model;

import org.springframework.stereotype.Component;

@Component("paypal")
public class PayPal extends Pagamento {
    @Override
    public void paga() {
        System.out.println("Pagamento di " + importo + "€ effettuato con PayPal.");
    }
}

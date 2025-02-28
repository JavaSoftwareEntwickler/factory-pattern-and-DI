package com.portaleapp.factory.pattern.and.DI.model;


public abstract class Pagamento {
    protected Double importo;

    public abstract void paga();

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}

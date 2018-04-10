/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.enuns;

import java.io.Serializable;

/**
 *
 * @author Jolcinei
 */
public enum EnumTipoConta implements Serializable{
    
    CC("CC","Conta Corrente"),
    CD("CD","Conta Depósito"),
    PG("PG","Conta Pagamento"),
    PP("PP","Poupança");
    
    private String tipo;
    
    private String descrição;

    private EnumTipoConta(String tipo, String descrição) {
        this.tipo = tipo;
        this.descrição = descrição;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }

    @Override
    public String toString() {
        return getDescrição();
    }
    
    
}

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
public enum EnumInstituidorArranjoPagamento implements Serializable{
    
    MASTERCARD("003","Mastercard","Crédito"),
    VISA("004","Visa","Crédito"),
    DINERS_CLUB("005","Diners Club","Crédito");
    
    private String dominio;
    
    private String descricao;
    
    private String tipo;

    private EnumInstituidorArranjoPagamento(String dominio, String descricao, String tipo) {
        this.dominio = dominio;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public String getDominio() {
        return dominio;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
    
    
}

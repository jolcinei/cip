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
public enum EnumInstituidorArranjoPagamento implements Serializable {

    MASTERCARD("003", "Mastercard", "Crédito"),
    VISA("004", "Visa", "Crédito"),
    DINERS_CLUB("005", "Diners Club", "Crédito"),
    AMERICAN_EXPRESS("006", "American Express", "Crédito"),
    HIPER_DEBITO("007", "Hiper Débito", "Débito"),
    ELO("008", "Elo", "Crédito"),
    ALELO("009", "Alelo", "Pré-Pago"),
    CABAL("010", "Cabal", "Crédito"),
    AGIPLAN("011", "Agiplan", "Crédito"),
    AURA("012", "Aura", "Crédito"),
    BANESCARD("013", "Banescard", "Crédito"),
    CALCARD("014", "Calcard", "Crédito"),
    CREDSYSTEM("015", "Credsystem", "Crédito"),
    CUP("016", "Cup", "Crédito"),
    REDESPLAN("017", "Redesplan", "Crédito"),
    SICRED("018", "Sicred", "Crédito"),
    SOROCRED("019", "Sorocred", "Crédito"),
    VERDECARD("020", "Verdecard", "Crédito"),
    HIPERCARD("021", "Hipercard", "Crédito"),
    AVISTA("022", "Avista", "Crédito"),
    CREDZ("023", "Credz", "Crédito"),
    DISCOVER("024", "Discover", "Crédito"),
    MAESTRO("025", "Maestro", "Débito"),
    VISA_ELECTRON("026", "Visa Electron", "Débito"),
    ELO_DEBITO("027", "Elo Débito", "Débito"),
    SICREDI_DEBITO("028", "Sicredi Débito", "Débito"),
    HIPER_CREDITO("029", "Hiper Crédito", "Crédito"),
    CABAL_DEBITO("030", "Cabal Débito", "Débito"),
    JCB("031", "JCB", "Crédito"),
    TICKET("032", "Ticket", "Pré-pago"),
    SODEXO("033", "Sodexo", "Pré-pago"),
    VR("034", "VR", "Pré-pago"),
    POLICARD("035", "Policard", "Pré-pago"),
    VALECARD("036", "Valecard", "Pré-pago"),
    GOODCARD("037", "Goodcard", "Crédito"),
    GREENCARD("038", "Greencard", "Pré-pago"),
    COOPERCARD("039", "Coopercard", "Pré-pago"),
    VEROCHEQUE("040", "Verocheque", "Pré-pago"),
    NUTRICASH("041", "Nutricash", "Pré-pago"),
    BANRICARD("042", "Banricard", "Pré-pago"),
    BANESCARD_DEBITO("043", "Banescard Débito", "Débito"),
    SOCORED_PRE_PAGO("044", "Socored Pré-pago", "Pré-pago"),
    MASTERCARD_PRE_PAGO("045", "Mastercard Pré-pago", "Pré-pago"),
    VISA_PRE_PAGO("046", "Visa Pré-pago", "Pré-pago"),
    OUROCARD("047", "Ourocard", "Débito"),
    SOROCRED_DEBITO("048", "Sorocred Débito", "Débito");

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

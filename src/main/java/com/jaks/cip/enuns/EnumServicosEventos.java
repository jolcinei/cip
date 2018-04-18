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
public enum EnumServicosEventos implements Serializable {

    //Credenciador para CIP
    ASLC027("027", "Credenciador informa as liquidações das transações de cartões de crédito."),
    ASLC029("029", "Credenciador informa as liquidações das transações de cartões de débito."),
    ASLC031("031", "Credenciador informa as liquidações das transações de antecipações de recebíveis de cartões de pagamento."),
    //CIP para Credenciador
    ASLC027RET("027RET", "Retorno do processamento das informacoes com as liquidacoes das transacoes de cartoes de credito."),
    ASLC029RET("029RET", "Retorno do processamento das informacoes com as liquidacoes das transacoes de cartoes de debito."),
    ASLC031RET("031RET", "Retorno do processamento das liquidacoes das transacoes de antecipações de recebíveis de cartões de pagamento."),
    ASLC028("028", "SLC informa o retorno do processamento das liquidacoes das transacoes de cartoes de credito."),
    ASLC030("030", "SLC informa o retorno do processamento das liquidacoes das transacoes de cartoes de debito."),
    ASLC034("034", "SLC informa o retorno do processamento das liquidacoes das transacoes de antecipações de recebíveis de cartões de pagamento."),
    //Domicilio para a CIP
    ASLC023("023", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das transacoes de cartoes de CREDITO."),
    ASLC025("025", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das transacoes de cartoes de DEBITO."),
    ASLC033("033", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das antecipacoes de recebiveis de cartoes de pagamento."),
    //CIP para Domicilio    
    ASLC022("022", "SLC informa as liquidações das transações de cartões de crédito para IF Domicilio."),
    ASLC024("024", "SLC informa as liquidações das transações de cartões de debito para IF Domicilio."),
    ASLC032("032", "SLC informa as liquidações das transações de antecipacoes de recebiveis de cartoes de pagamento para IF Domicilio.");

    private String codigo;

    private String descricao;

    private EnumServicosEventos(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}

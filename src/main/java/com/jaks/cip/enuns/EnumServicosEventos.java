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

    //Credenciador
    ASLC027("027", "Credenciador informa as liquidações das transações de cartões de crédito."),
    ASLC029("029", "Credenciador informa as liquidações das transações de cartões de débito."),
    ASLC031("031", "Credenciador informa as liquidações das transações de antecipações de recebíveis de cartões de pagamento."),
    //Domicilio
    //ASLC022("022", "SLC informa as liquidações das transações de cartões de crédito para IF Domicilio."),
    ASLC023("023", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das transacoes de cartoes de CREDITO."),
    //ASLC024("024", "SLC informa as liquidações das transações de cartões de debito para IF Domicilio."),
    ASLC025("025", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das transacoes de cartoes de DEBITO."),
    //ASLC032("022", "SLC informa as liquidações das transações de antecipacoes de recebiveis de cartoes de pagamento para IF Domicilio."),
    ASLC033("022", "IF Domicilio informa o retorno do processamento das informacoes com liquidacoes das antecipacoes de recebiveis de cartoes de pagamento.");

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

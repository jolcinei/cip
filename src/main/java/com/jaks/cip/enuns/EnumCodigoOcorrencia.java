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
public enum EnumCodigoOcorrencia implements Serializable {

    //ToDo
    //Cadastrar demais Ocorrencias
    C00("00", "Lancmento do Cartao de Credito ou Debito Efetuado"),
    C01("01", "Agendamento do cartao de Credito Efetuado");

    private String codigo;

    private String descricao;

    private EnumCodigoOcorrencia(String codigo, String descricao) {
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

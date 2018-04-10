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
public enum EnumCodigoErro implements Serializable {

    ESLC0006("ESLC0006", "Data inválida"),
    ESLC0007("ESLC0007", "CPF ou CNPJ Inválido"),
    ESLC0029("ESLC0029", "Requisicao enviada fora do horario"),
    EGEN0043("EGEN0043", "XML Invalido"),
    ESLC0088("ESLC0088", "Tipo de Pessoa Centralizadora fora do domínio"),
    ESLC0089("ESLC0089", "Código da Moeda fora do domínio"),
    ESLC0090("ESLC0090", "Tipo de Pessoa Ponto de Venda fora do domínio"),
    ESLC0091("ESLC0091", "Código do Instituidor do Arranjo de Pagamento fora do domínio"),
    ESLC0102("ESLC0102", "CNPJ Base Credenciador diferente do cadastro no SLC"),
    ESLC0103("ESLC0103", "CNPJ Credenciador diferente do cadastro no SLC"),
    ESLC0104("ESLC0104", "Numero Controle Credenciador Centralizadora não pode ser repetido"),
    ESLC0105("ESLC0105", "Numero Controle Credenciador Ponto de Venda não pode ser repetido"),
    ESLC0108("ESLC0108", "Indicador da Forma de Transferência fora do domínio"),
    ESLC0110("ESLC0110", "IF credora sem adesão a funcionalidade: liquidações das transações de cartões de débito"),
    ESLC0111("ESLC0111", "Tipo de Liquidação de débito fora do domínio"),
    ESLC0114("ESLC0114", "Data de Pagamento informada não permitida para o Tipo de Produto Liquidação de Débito"),
    ESLC0117("ESLC0117", "Valor do Pagamento deve ser maior que zero"),
    ESLC0118("ESLC0118", "Participante não cadastrado no SLC"),
    ESLC0119("ESLC0119", "Participante Administrado não é administrado pelo Participante Principal"),
    ESLC0121("ESLC0121", "Tag Tipo Conta não informada"),
    ESLC0122("ESLC0122", "ISPB informado não possui relacionamento com o Credenciador"),
    ESLC0123("ESLC0123", "Participante Administrado não aderiu a funcionalidade"),
    ESLC0124("ESLC0124", "Tipo de Conta informada inativa"),
    ESLC0125("ESLC0125", "Tipo de Conta informada fora do domínio"),
    ESLC0126("ESLC0126", "Campo obrigatório não preenchido"),
    ESLC0127("ESLC0127", "Dados enviados não correspondem ao Tipo de Conta informado"),
    ESLC0132("ESLC0132", "Não existe adesão cadastrada para o arranjo informado"),
    ESLC0133("ESLC0133", "ISPB Devedora informada não está vigente"),
    ESLC0138("ESLC0138", "Indicador de forma de transferência não permitido para o arranjo de pagamento informado"),
    ESLC0139("ESLC0139", "Não existe relacionamento cadastrado para o participante x arranjo de pagamento informado"),
    ESLC0140("ESLC0140", "Código Instituidor de Arranjo de Pagamento informado não permitido para o tipo de arquivo enviado"),
    ESLC0141("ESLC0141", "CNPJ Base do Credenciador informado divergente do informado na tag Participante Administrado"),
    ESLC0147("ESLC0147", "ISPB Liquidação Ponto de Venda não pode ser diferente do ISPB IF Credora"),
    ESLC0148("ESLC0148", "ISPB IF Credora não possui liquidante"),
    ESLC0149("ESLC0149", "Valor do Pagamento excedido o máximo permitido");

    private String codigo;

    private String descricao;

    private EnumCodigoErro(String codigo, String descricao) {
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

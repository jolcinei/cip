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
    C00("00", "Lancmento do Cartao de Credito ou Debito Efetuado", "Transacao com sucesso", "Registro do Arquivo de credito, debito e Antecipacao"),
    C01("01", "Agendamento do cartao de Credito Efetuado", "Transacao com sucesso apos Pre-Critica", "Registro do Arquivo de Credito"),
    C02("02", "Lancmento do Cartao de Credito ou Debito Efetuado Cancelado pelo Pagador/Credor", "Erro", "Registro do Arquivo de credito, debito e Antecipacao"),
    C03("03", "Data de Pagamento Iválida", "Erro", "DtPgto"),
    C04("04", "Conta Centralizador/Ponto de venda Ivalida", "Erro", "CtCentrlz"),
    C05("05", "Conta Centralizador/Ponto de venda Bloqueada", "Erro", "CtCentrlz"),
    C06("06", "Conta Centralizador/Ponto de venda Fechada", "Erro", "CtCentrlz"),
    C07("07", "Conta Centralizador/Ponto de venda Cancelada", "Erro", "CtCentrlz"),
    C08("08", "Agencia Centralizador/Ponto de venda Invalida", "Erro", "AgCentrlz"),
    C09("09", "Agencia Centralizador/Ponto de venda fechada", "Erro", "AgCentrlz"),
    C10("10", "Domicilio Bancario Invalido (Banco/Agencia/Conta/CNPJ/CPF)", "Erro", "CtCentrlz/AgCentrlz/CNPJ_CPFCentrlz/CNPJ_CPFPontoVenda"),
    C11("11", "CNPJ ou CPF Centralizador/Ponto nao localizado", "Erro", "CNPJ_CPFCentrlz/CNPJ_CPFPontoVenda"),
    C12("12", "CNPJ Base do Credenciador invalido", "Erro", "CNPJBaseCreddr"),
    C30("30", "Lancamento Recusado por falta de transferencia financeira", "Erro", "Registro do Arquivo de credito, debito e Antecipacao"),
    C31("31", "Lancamento Recusado pela Credenciadora", "Erro", "Registro do Arquivo de credito, debito e Antecipacao"),
    C32("32", "Lancamento Recusado - ausencia de acordo bilateral com Instituicao Domicilio", "Erro", "Registro do Arquivo de credito, debito e Antecipacao"),
    C33("33", "Divergencia no valor recebido para liquidacao", "Erro", "Registro do Arquivo de credito, debito e Antecipacao"),
    C99("99", "Erro no lancamento interno da I.D.", "Erro", "Registro do Arquivo de credito, debito e Antecipacao");

    private String codigo;

    private String descricao;

    private String situacao;

    private String regImpactados;

    private EnumCodigoOcorrencia(String codigo, String descricao, String situacao, String regImpactados) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.situacao = situacao;
        this.regImpactados = regImpactados;
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getRegImpactados() {
        return regImpactados;
    }

    public void setRegImpactados(String regImpactados) {
        this.regImpactados = regImpactados;
    }

}

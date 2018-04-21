/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.enuns;

import java.awt.Font;
import java.io.Serializable;

/**
 *
 * @author Jolcinei
 */
public enum EnumCodigoErro implements Serializable {

    EGEN0043("EGEN0043", "XML Invalido"),
    ESLC0006("ESLC0006", "Data inválida"),
    ESLC0007("ESLC0007", "CPF ou CNPJ Inválido"),
    ESLC0029("ESLC0029", "Requisicao enviada fora do horario"),
    ESLC0041("ESLC0041", "Grade horaria nao cadastrada"),
    ESLC0042("ESLC0042", "Não existe adesao para essa funcionalidade"),
    ESLC0088("ESLC0088", "Tipo de Pessoa Centralizadora fora do domínio"),
    ESLC0089("ESLC0089", "Código da Moeda fora do domínio"),
    ESLC0090("ESLC0090", "Tipo de Pessoa Ponto de Venda fora do domínio"),
    ESLC0091("ESLC0091", "Código da ocorrencia fora do domínio"),
    ESLC0092("ESLC0092", "Código do Instituidor do Arranjo de Pagamento fora do domínio"),
    ESLC0097("ESLC0097", "NU Liquidacao nao cadastrado"),
    ESLC0102("ESLC0102", "CNPJ Base Credenciador diferente do cadastro no SLC"),
    ESLC0103("ESLC0103", "CNPJ Credenciador diferente do cadastro no SLC"),
    ESLC0104("ESLC0104", "Numero Controle Credenciador Centralizadora não pode ser repetido"),
    ESLC0105("ESLC0105", "Numero Controle Credenciador Ponto de Venda não pode ser repetido"),
    ESLC0106("ESLC0106", "IF Credora sem adesao a funcionalidade: liquidacao das transacoes de cartoes de credito."),
    ESLC0107("ESLC0107", "Tipo de produto liquidacao de credito fora do domínio"),
    ESLC0108("ESLC0108", "Indicador da Forma de Transferência fora do domínio"),
    ESLC0109("ESLC0109", "Numero controle IF nao pode ser repetido"),
    ESLC0110("ESLC0110", "IF credora sem adesão a funcionalidade: liquidações das transações de cartões de débito"),
    ESLC0111("ESLC0111", "Tipo de Liquidação de débito fora do domínio"),
    ESLC0112("ESLC0112", "Data de pagamento informada nao permitida para o Tipo de Produto Cartao de Credito"),
    ESLC0113("ESLC0113", "Data de pagamento informada nao permitida para o Tipo de Produto igual a Ajustes de Credito"),
    ESLC0114("ESLC0114", "Data de Pagamento informada não permitida para o Tipo de Produto Liquidação de Débito"),
    ESLC0115("ESLC0115", "Erro no segmento ESTARQ, campo nao preenchido"),
    ESLC0116("ESLC0116", "Erro no segmento ESTARQ, formato do dado invalido"),
    ESLC0117("ESLC0117", "Valor do Pagamento deve ser maior que zero"),
    ESLC0118("ESLC0118", "Participante não cadastrado no SLC"),
    ESLC0119("ESLC0119", "Participante Administrado não é administrado pelo Participante Principal"),
    ESLC0120("ESLC0120", "Informar os campos Agencia e Conta Centralizadora"),
    ESLC0121("ESLC0121", "Tag Tipo Conta não informada"),
    ESLC0122("ESLC0122", "ISPB informado não possui relacionamento com o Credenciador"),
    ESLC0123("ESLC0123", "Participante Administrado não aderiu a funcionalidade"),
    ESLC0124("ESLC0124", "Tipo de Conta informada inativa"),
    ESLC0125("ESLC0125", "Tipo de Conta informada fora do domínio"),
    ESLC0126("ESLC0126", "Campo obrigatório não preenchido"),
    ESLC0127("ESLC0127", "Dados enviados não correspondem ao Tipo de Conta informado"),
    ESLC0128("ESLC0128", "Data informada nao permitida para Tipo de Produto Liquidacao de Cartoes."),
    ESLC0129("ESLC0129", "NU Liquidacao em processo de liquidacao."),
    ESLC0130("ESLC0130", "Prazo de recebimento do numero de liquidacao vencido."),
    ESLC0131("ESLC0131", "ISPB do Emissor nao pode ser diferente do Identificador do Participante Principal"),
    ESLC0132("ESLC0132", "Não existe adesão cadastrada para o arranjo informado"),
    ESLC0133("ESLC0133", "ISPB Devedora informada não está vigente"),
    ESLC0134("ESLC0134", "Codigo de Ocorrencia informado nao permitido para o primeiro retorno"),
    ESLC0135("ESLC0135", "Situacao do lancamento nao permite atualizacao"),
    ESLC0136("ESLC0136", "Codigo da Ocorrencia nao permitido para o tipo de Liquidacao do lancamento"),
    ESLC0137("ESLC0137", "Codigo da Ocorrencia nao permitido para lancamento nao liquidado"),
    ESLC0138("ESLC0138", "Indicador de forma de transferência não permitido para o arranjo de pagamento informado"),
    ESLC0139("ESLC0139", "Não existe relacionamento cadastrado para o participante x arranjo de pagamento informado"),
    ESLC0140("ESLC0140", "Código Instituidor de Arranjo de Pagamento informado não permitido para o tipo de arquivo enviado"),
    ESLC0141("ESLC0141", "CNPJ Base do Credenciador informado divergente do informado na tag Participante Administrado"),
    ESLC0142("ESLC0142", "Quantidade maxima de arquivos excedida para a data de processamento atual do sistema"),
    ESLC0143("ESLC0143", "Quantidade maxima de registros excedida para o meio de transmissao."),
    ESLC0144("ESLC0144", "A data informada é difernte da data referencia do sistema"),
    ESLC0145("ESLC0145", "Nome do arquivo já registrado"),
    ESLC0146("ESLC0146", "Codigo de Ocorrencia de sucesso e erro nao permitido no mesmo retorno"),
    ESLC0147("ESLC0147", "ISPB Liquidação Ponto de Venda não pode ser diferente do ISPB IF Credora"),
    ESLC0148("ESLC0148", "ISPB IF Credora não possui liquidante"),
    ESLC0149("ESLC0149", "Valor do Pagamento excedido o máximo permitido"),
    ESLC0999("ESLC0999", "Erro inesperado.");

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
        return descricao.toUpperCase();
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}

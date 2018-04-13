/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.model;

import com.jaks.cip.enuns.EnumCodigoErro;
import com.jaks.cip.enuns.EnumInstituidorArranjoPagamento;
import com.jaks.cip.enuns.EnumTipoRetornado;
import java.util.Date;
import javax.persistence.*;
import org.joda.time.DateTime;

/**
 *
 * @author Jolcinei
 */
public class PontoVenda {

    @Column(name = "Id")
    private int id;

    //Numero Controle do credenciador do ponto de venda
    //formado sugerido data yyyyMMdd + sequencia de 12 posições.
    @Column(name = "NumCtrlCreddrPontoVenda", length = 20)
    private String NumCtrlCreddrPontoVenda;

    //Identificador do Participante junto ao banco central 8 posições.
    @Column(name = "ISPBIFLiquidPontoVenda", length = 8)
    private String ISPBIFLiquidPontoVenda;

    //Código do ponto de venda
    @Column(name = "CodPontoVenda", length = 25)
    private String CodPontoVenda;

    //Nome do ponto venda.
    @Column(name = "NomePontoVenda", length = 80)
    private String NomePontoVenda;

    //Tipo pessoa F - Física, J - Jurídica.
    @Column(name = "TpPessoaPontoVenda", length = 1)
    private String TpPessoaPontoVenda;

    //CNPJ ou CPF ponto de venda.
    @Column(name = "CNPJ_CPFPontoVenda", length = 14)
    private String CNPJ_CPFPontoVenda;

    //Código do Arranjo de pagamento.
    @Column(name = "CodInstitdrArrajPgto", length = 3)
    private EnumInstituidorArranjoPagamento CodInstitdrArrajPgto;

    //Tipo de produto de liquidação de crédito
    @Column(name = "TpProdLiquidCred", length = 2)
    private String TpProdLiquidCred;

    //Tipo de produto de liquidação de crédito
    @Column(name = "TpProdLiquidDeb", length = 2)
    private String TpProdLiquidDeb;

    //Tipo de produto de liquidação de crédito
    @Column(name = "TpProdLiquidCarts", length = 2)
    private String TpProdLiquidCarts;

    //Indicador da forma de pagamento 3-SILOC, 4-DEBITO EM CONTA CORRENTE, 5-STR
    @Column(name = "IndrFormaTransf", length = 1)
    private String IndrFormaTransf;

    //Código da moeda BRL, 790
    @Column(name = "CodMoeda", length = 3)
    private String CodMoeda;

    //Data de pagamento yyyyMMdd
    @Column(name = "DtPgto", length = 3)
    private String DtPgto;

    //Valor do pagamento deve ser preenchido com valor maior que 0.
    //Até 17 inteiros e 2 decimais 17,02. Para R$100,00 informe 100, para R$100,20 informe 100.2 etc.
    @Column(name = "VlrPgto", length = 3)
    private double VlrPgto;

    //Numero de controle do credenciador do ponto de venda aceito.
    //formado sugerido data yyyyMMdd + sequencia de 12 posições.
    @Column(name = "NumCtrlCreddrPontoVendaActo", length = 20)
    private String NumCtrlCreddrPontoVendaActo;

    //Numero de controle CIP ponto de venda aceito.
    //formado sugerido data yyyyMMdd + sequencia de 12 posições.
    @Column(name = "NumCtrlCIPPontoVendaActo", length = 20)
    private String NumCtrlCIPPontoVendaActo;

    //Numero único liquidação
    //formado sugerido data yyyyMMdd + sequencia de 13 posições.
    @Column(name = "NULiquid", length = 21)
    private String NULiquid;

    //Data hora manutenção yyyyMMddHHmmss
    @Column(name = "DtHrManut", length = 19)
    private DateTime DtHrManut;

    private EnumTipoRetornado enumTipoRetornado;

    //Pegar os erros por cada campo do arquivo.
    private EnumCodigoErro codigoErroNumCtrlCreddrPontoVenda;
    private EnumCodigoErro codigoErroISPBIFLiquidPontoVenda;
    private EnumCodigoErro codigoErroCodPontoVenda;
    private EnumCodigoErro codigoErroNomePontoVenda;
    private EnumCodigoErro codigoErroTpPessoaPontoVenda;
    private EnumCodigoErro codigoErroCNPJ_CPFPontoVenda;
    private EnumCodigoErro codigoErroCodInstitdrArrajPgto;
    private EnumCodigoErro codigoErroTpProdLiquidCred;
    private EnumCodigoErro codigoErroTpProdLiquidDeb;
    private EnumCodigoErro codigoErroTpProdLiquidCarts;
    private EnumCodigoErro codigoErroIndrFormaTransf;
    private EnumCodigoErro codigoErroCodMoeda;
    private EnumCodigoErro codigoErroDtPgto;
    private EnumCodigoErro codigoErroVlrPgto;
    private EnumCodigoErro codigoErroNumCtrlCreddrPontoVendaActo;
    private EnumCodigoErro codigoErroNumCtrlCIPPontoVendaActo;
    private EnumCodigoErro codigoErroNULiquid;
    private EnumCodigoErro codigoErroDtHrManut;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCtrlCreddrPontoVenda() {
        return NumCtrlCreddrPontoVenda;
    }

    public void setNumCtrlCreddrPontoVenda(String NumCtrlCreddrPontoVenda) {
        this.NumCtrlCreddrPontoVenda = NumCtrlCreddrPontoVenda;
    }

    public String getISPBIFLiquidPontoVenda() {
        return ISPBIFLiquidPontoVenda;
    }

    public void setISPBIFLiquidPontoVenda(String ISPBIFLiquidPontoVenda) {
        this.ISPBIFLiquidPontoVenda = ISPBIFLiquidPontoVenda;
    }

    public String getCodPontoVenda() {
        return CodPontoVenda;
    }

    public void setCodPontoVenda(String CodPontoVenda) {
        this.CodPontoVenda = CodPontoVenda;
    }

    public String getNomePontoVenda() {
        return NomePontoVenda;
    }

    public void setNomePontoVenda(String NomePontoVenda) {
        this.NomePontoVenda = NomePontoVenda;
    }

    public String getTpPessoaPontoVenda() {
        return TpPessoaPontoVenda;
    }

    public void setTpPessoaPontoVenda(String TpPessoaPontoVenda) {
        this.TpPessoaPontoVenda = TpPessoaPontoVenda;
    }

    public String getCNPJ_CPFPontoVenda() {
        return CNPJ_CPFPontoVenda;
    }

    public void setCNPJ_CPFPontoVenda(String CNPJ_CPFPontoVenda) {
        this.CNPJ_CPFPontoVenda = CNPJ_CPFPontoVenda;
    }

    public EnumInstituidorArranjoPagamento getCodInstitdrArrajPgto() {
        return CodInstitdrArrajPgto;
    }

    public void setCodInstitdrArrajPgto(EnumInstituidorArranjoPagamento CodInstitdrArrajPgto) {
        this.CodInstitdrArrajPgto = CodInstitdrArrajPgto;
    }

    public String getTpProdLiquidCred() {
        return TpProdLiquidCred;
    }

    public void setTpProdLiquidCred(String TpProdLiquidCred) {
        this.TpProdLiquidCred = TpProdLiquidCred;
    }

    public String getTpProdLiquidDeb() {
        return TpProdLiquidDeb;
    }

    public void setTpProdLiquidDeb(String TpProdLiquidDeb) {
        this.TpProdLiquidDeb = TpProdLiquidDeb;
    }

    public String getTpProdLiquidCarts() {
        return TpProdLiquidCarts;
    }

    public void setTpProdLiquidCarts(String TpProdLiquidCarts) {
        this.TpProdLiquidCarts = TpProdLiquidCarts;
    }

    public String getIndrFormaTransf() {
        return IndrFormaTransf;
    }

    public void setIndrFormaTransf(String IndrFormaTransf) {
        this.IndrFormaTransf = IndrFormaTransf;
    }

    public String getCodMoeda() {
        return CodMoeda;
    }

    public void setCodMoeda(String CodMoeda) {
        this.CodMoeda = CodMoeda;
    }

    public String getDtPgto() {
        return DtPgto;
    }

    public void setDtPgto(String DtPgto) {
        this.DtPgto = DtPgto;
    }

    public double getVlrPgto() {
        return VlrPgto;
    }

    public void setVlrPgto(double VlrPgto) {
        this.VlrPgto = VlrPgto;
    }

    public String getNumCtrlCreddrPontoVendaActo() {
        return NumCtrlCreddrPontoVendaActo;
    }

    public void setNumCtrlCreddrPontoVendaActo(String NumCtrlCreddrPontoVendaActo) {
        this.NumCtrlCreddrPontoVendaActo = NumCtrlCreddrPontoVendaActo;
    }

    public String getNumCtrlCIPPontoVendaActo() {
        return NumCtrlCIPPontoVendaActo;
    }

    public void setNumCtrlCIPPontoVendaActo(String NumCtrlCIPPontoVendaActo) {
        this.NumCtrlCIPPontoVendaActo = NumCtrlCIPPontoVendaActo;
    }

    public String getNULiquid() {
        return NULiquid;
    }

    public void setNULiquid(String NULiquid) {
        this.NULiquid = NULiquid;
    }

    public DateTime getDtHrManut() {
        return DtHrManut;
    }

    public void setDtHrManut(DateTime DtHrManut) {
        this.DtHrManut = DtHrManut;
    }

    public EnumTipoRetornado getEnumTipoRetornado() {
        return enumTipoRetornado;
    }

    public void setEnumTipoRetornado(EnumTipoRetornado enumTipoRetornado) {
        this.enumTipoRetornado = enumTipoRetornado;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCreddrPontoVenda() {
        return codigoErroNumCtrlCreddrPontoVenda;
    }

    public void setCodigoErroNumCtrlCreddrPontoVenda(EnumCodigoErro codigoErroNumCtrlCreddrPontoVenda) {
        this.codigoErroNumCtrlCreddrPontoVenda = codigoErroNumCtrlCreddrPontoVenda;
    }

    public EnumCodigoErro getCodigoErroISPBIFLiquidPontoVenda() {
        return codigoErroISPBIFLiquidPontoVenda;
    }

    public void setCodigoErroISPBIFLiquidPontoVenda(EnumCodigoErro codigoErroISPBIFLiquidPontoVenda) {
        this.codigoErroISPBIFLiquidPontoVenda = codigoErroISPBIFLiquidPontoVenda;
    }

    public EnumCodigoErro getCodigoErroCodPontoVenda() {
        return codigoErroCodPontoVenda;
    }

    public void setCodigoErroCodPontoVenda(EnumCodigoErro codigoErroCodPontoVenda) {
        this.codigoErroCodPontoVenda = codigoErroCodPontoVenda;
    }

    public EnumCodigoErro getCodigoErroNomePontoVenda() {
        return codigoErroNomePontoVenda;
    }

    public void setCodigoErroNomePontoVenda(EnumCodigoErro codigoErroNomePontoVenda) {
        this.codigoErroNomePontoVenda = codigoErroNomePontoVenda;
    }

    public EnumCodigoErro getCodigoErroTpPessoaPontoVenda() {
        return codigoErroTpPessoaPontoVenda;
    }

    public void setCodigoErroTpPessoaPontoVenda(EnumCodigoErro codigoErroTpPessoaPontoVenda) {
        this.codigoErroTpPessoaPontoVenda = codigoErroTpPessoaPontoVenda;
    }

    public EnumCodigoErro getCodigoErroCNPJ_CPFPontoVenda() {
        return codigoErroCNPJ_CPFPontoVenda;
    }

    public void setCodigoErroCNPJ_CPFPontoVenda(EnumCodigoErro codigoErroCNPJ_CPFPontoVenda) {
        this.codigoErroCNPJ_CPFPontoVenda = codigoErroCNPJ_CPFPontoVenda;
    }

    public EnumCodigoErro getCodigoErroCodInstitdrArrajPgto() {
        return codigoErroCodInstitdrArrajPgto;
    }

    public void setCodigoErroCodInstitdrArrajPgto(EnumCodigoErro codigoErroCodInstitdrArrajPgto) {
        this.codigoErroCodInstitdrArrajPgto = codigoErroCodInstitdrArrajPgto;
    }

    public EnumCodigoErro getCodigoErroTpProdLiquidCred() {
        return codigoErroTpProdLiquidCred;
    }

    public void setCodigoErroTpProdLiquidCred(EnumCodigoErro codigoErroTpProdLiquidCred) {
        this.codigoErroTpProdLiquidCred = codigoErroTpProdLiquidCred;
    }

    public EnumCodigoErro getCodigoErroTpProdLiquidDeb() {
        return codigoErroTpProdLiquidDeb;
    }

    public void setCodigoErroTpProdLiquidDeb(EnumCodigoErro codigoErroTpProdLiquidDeb) {
        this.codigoErroTpProdLiquidDeb = codigoErroTpProdLiquidDeb;
    }

    public EnumCodigoErro getCodigoErroTpProdLiquidCarts() {
        return codigoErroTpProdLiquidCarts;
    }

    public void setCodigoErroTpProdLiquidCarts(EnumCodigoErro codigoErroTpProdLiquidCarts) {
        this.codigoErroTpProdLiquidCarts = codigoErroTpProdLiquidCarts;
    }

    public EnumCodigoErro getCodigoErroIndrFormaTransf() {
        return codigoErroIndrFormaTransf;
    }

    public void setCodigoErroIndrFormaTransf(EnumCodigoErro codigoErroIndrFormaTransf) {
        this.codigoErroIndrFormaTransf = codigoErroIndrFormaTransf;
    }

    public EnumCodigoErro getCodigoErroCodMoeda() {
        return codigoErroCodMoeda;
    }

    public void setCodigoErroCodMoeda(EnumCodigoErro codigoErroCodMoeda) {
        this.codigoErroCodMoeda = codigoErroCodMoeda;
    }

    public EnumCodigoErro getCodigoErroDtPgto() {
        return codigoErroDtPgto;
    }

    public void setCodigoErroDtPgto(EnumCodigoErro codigoErroDtPgto) {
        this.codigoErroDtPgto = codigoErroDtPgto;
    }

    public EnumCodigoErro getCodigoErroVlrPgto() {
        return codigoErroVlrPgto;
    }

    public void setCodigoErroVlrPgto(EnumCodigoErro codigoErroVlrPgto) {
        this.codigoErroVlrPgto = codigoErroVlrPgto;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCreddrPontoVendaActo() {
        return codigoErroNumCtrlCreddrPontoVendaActo;
    }

    public void setCodigoErroNumCtrlCreddrPontoVendaActo(EnumCodigoErro codigoErroNumCtrlCreddrPontoVendaActo) {
        this.codigoErroNumCtrlCreddrPontoVendaActo = codigoErroNumCtrlCreddrPontoVendaActo;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCIPPontoVendaActo() {
        return codigoErroNumCtrlCIPPontoVendaActo;
    }

    public void setCodigoErroNumCtrlCIPPontoVendaActo(EnumCodigoErro codigoErroNumCtrlCIPPontoVendaActo) {
        this.codigoErroNumCtrlCIPPontoVendaActo = codigoErroNumCtrlCIPPontoVendaActo;
    }

    public EnumCodigoErro getCodigoErroNULiquid() {
        return codigoErroNULiquid;
    }

    public void setCodigoErroNULiquid(EnumCodigoErro codigoErroNULiquid) {
        this.codigoErroNULiquid = codigoErroNULiquid;
    }

    public EnumCodigoErro getCodigoErroDtHrManut() {
        return codigoErroDtHrManut;
    }

    public void setCodigoErroDtHrManut(EnumCodigoErro codigoErroDtHrManut) {
        this.codigoErroDtHrManut = codigoErroDtHrManut;
    }

}

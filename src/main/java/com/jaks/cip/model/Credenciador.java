/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.model;

import com.jaks.cip.enuns.EnumCodigoErro;
import com.jaks.cip.enuns.EnumCodigoOcorrencia;
import com.jaks.cip.enuns.EnumRetornoRequisicao;
import com.jaks.cip.enuns.EnumTipoRetornado;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import org.joda.time.DateTime;

/**
 *
 * @author Jolcinei
 */
public class Credenciador {

    @Column(name = "Id")
    private int id;

    //Identificador participante principal
    @Column(name = "IdentdPartPrincipal", length = 14)
    private String IdentdPartPrincipal;

    //CNPJ participante administrado
    @Column(name = "IdentPartAdmtd", length = 14)
    private String IdentPartAdmtd;

    //CNPJ base do credenciador com 8 posições.
    @Column(name = "CNPJBaseCreddr", length = 8)
    private String CNPJBaseCreddr;

    //CNPJ credenciador
    @Column(name = "CNPJCreddr", length = 14)
    private String CNPJCreddr;

    //Identificador do participante junto ao banco central 8 posições
    @Column(name = "ISPBIFDevdr", length = 8)
    private String ISPBIFDevdr;

    //Identificador do participante junto ao banco central 8 posições
    @Column(name = "ISPBIFCredr", length = 8)
    private String ISPBIFCredr;

    //Agência Credenciador sem digito verificador
    @Column(name = "AgCreddr", length = 4)
    private String AgCreddr;

    //Conta Corrente Credenciador com digito verificador
    @Column(name = "CtCreddr", length = 13)
    private String CtCreddr;

    //Nome do Credenciador
    @Column(name = "NomCreddr", length = 80)
    private String NomCreddr;

    //Situação do retorno
    @Column(name = "SitRetReq", length = 1)
    private EnumRetornoRequisicao SitRetReq;

    //Código da Ocorência 2 posições.
    @Column(name = "CodOcorc", length = 2)
    private EnumCodigoOcorrencia CodOcorc;

    //Numero único liquidação
    //formado sugerido data yyyyMMdd + sequencia de 13 posições.
    @Column(name = "NULiquid", length = 21)
    private String NULiquid;

    //Data hora manutenção yyyyMMddHHmmss
    @Column(name = "DtHrManut", length = 19)
    private DateTime DtHrManut;

    //Pegar os erros por cada campo do arquivo.
    private EnumCodigoErro codigoErroIdentdPartPrincipal;
    private EnumCodigoErro codigoErroIdentPartAdmtd;
    private EnumCodigoErro codigoErroCNPJBaseCreddr;
    private EnumCodigoErro codigoErroCNPJCreddr;
    private EnumCodigoErro codigoErroISPBIFDevdr;
    private EnumCodigoErro codigoErroISPBIFCredr;
    private EnumCodigoErro codigoErroAgCreddr;
    private EnumCodigoErro codigoErroCtCreddr;
    private EnumCodigoErro codigoErroNomeCreddr;
    private EnumCodigoErro codigoErroSitRetReq;
    private EnumCodigoErro codigoErroCodOcorc;
    private EnumCodigoErro codigoErroNULiquid;
    private EnumCodigoErro codigoErroDtHrManut;

    private EnumTipoRetornado enumTipoRetornado;

    private List<Centralizadora> centralizadoras;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentdPartPrincipal() {
        return IdentdPartPrincipal;
    }

    public void setIdentdPartPrincipal(String IdentdPartPrincipal) {
        this.IdentdPartPrincipal = IdentdPartPrincipal;
    }

    public String getIdentPartAdmtd() {
        return IdentPartAdmtd;
    }

    public void setIdentPartAdmtd(String IdentPartAdmtd) {
        this.IdentPartAdmtd = IdentPartAdmtd;
    }

    public String getCNPJBaseCreddr() {
        return CNPJBaseCreddr;
    }

    public void setCNPJBaseCreddr(String CNPJBaseCreddr) {
        this.CNPJBaseCreddr = CNPJBaseCreddr;
    }

    public String getCNPJCreddr() {
        return CNPJCreddr;
    }

    public void setCNPJCreddr(String CNPJCreddr) {
        this.CNPJCreddr = CNPJCreddr;
    }

    public String getISPBIFDevdr() {
        return ISPBIFDevdr;
    }

    public void setISPBIFDevdr(String ISPBIFDevdr) {
        this.ISPBIFDevdr = ISPBIFDevdr;
    }

    public String getISPBIFCredr() {
        return ISPBIFCredr;
    }

    public void setISPBIFCredr(String ISPBIFCredr) {
        this.ISPBIFCredr = ISPBIFCredr;
    }

    public String getAgCreddr() {
        return AgCreddr;
    }

    public void setAgCreddr(String AgCreddr) {
        this.AgCreddr = AgCreddr;
    }

    public String getCtCreddr() {
        return CtCreddr;
    }

    public void setCtCreddr(String CtCreddr) {
        this.CtCreddr = CtCreddr;
    }

    public String getNomCreddr() {
        return NomCreddr;
    }

    public void setNomCreddr(String NomCreddr) {
        this.NomCreddr = NomCreddr;
    }

    public EnumRetornoRequisicao getSitRetReq() {
        return SitRetReq;
    }

    public void setSitRetReq(EnumRetornoRequisicao SitRetReq) {
        this.SitRetReq = SitRetReq;
    }

    public EnumCodigoOcorrencia getCodOcorc() {
        return CodOcorc;
    }

    public void setCodOcorc(EnumCodigoOcorrencia CodOcorc) {
        this.CodOcorc = CodOcorc;
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

    public EnumCodigoErro getCodigoErroIdentdPartPrincipal() {
        return codigoErroIdentdPartPrincipal;
    }

    public void setCodigoErroIdentdPartPrincipal(EnumCodigoErro codigoErroIdentdPartPrincipal) {
        this.codigoErroIdentdPartPrincipal = codigoErroIdentdPartPrincipal;
    }

    public EnumCodigoErro getCodigoErroIdentPartAdmtd() {
        return codigoErroIdentPartAdmtd;
    }

    public void setCodigoErroIdentPartAdmtd(EnumCodigoErro codigoErroIdentPartAdmtd) {
        this.codigoErroIdentPartAdmtd = codigoErroIdentPartAdmtd;
    }

    public EnumCodigoErro getCodigoErroCNPJBaseCreddr() {
        return codigoErroCNPJBaseCreddr;
    }

    public void setCodigoErroCNPJBaseCreddr(EnumCodigoErro codigoErroCNPJBaseCreddr) {
        this.codigoErroCNPJBaseCreddr = codigoErroCNPJBaseCreddr;
    }

    public EnumCodigoErro getCodigoErroCNPJCreddr() {
        return codigoErroCNPJCreddr;
    }

    public void setCodigoErroCNPJCreddr(EnumCodigoErro codigoErroCNPJCreddr) {
        this.codigoErroCNPJCreddr = codigoErroCNPJCreddr;
    }

    public EnumCodigoErro getCodigoErroISPBIFDevdr() {
        return codigoErroISPBIFDevdr;
    }

    public void setCodigoErroISPBIFDevdr(EnumCodigoErro codigoErroISPBIFDevdr) {
        this.codigoErroISPBIFDevdr = codigoErroISPBIFDevdr;
    }

    public EnumCodigoErro getCodigoErroISPBIFCredr() {
        return codigoErroISPBIFCredr;
    }

    public void setCodigoErroISPBIFCredr(EnumCodigoErro codigoErroISPBIFCredr) {
        this.codigoErroISPBIFCredr = codigoErroISPBIFCredr;
    }

    public EnumCodigoErro getCodigoErroAgCreddr() {
        return codigoErroAgCreddr;
    }

    public void setCodigoErroAgCreddr(EnumCodigoErro codigoErroAgCreddr) {
        this.codigoErroAgCreddr = codigoErroAgCreddr;
    }

    public EnumCodigoErro getCodigoErroCtCreddr() {
        return codigoErroCtCreddr;
    }

    public void setCodigoErroCtCreddr(EnumCodigoErro codigoErroCtCreddr) {
        this.codigoErroCtCreddr = codigoErroCtCreddr;
    }

    public EnumCodigoErro getCodigoErroNomeCreddr() {
        return codigoErroNomeCreddr;
    }

    public void setCodigoErroNomeCreddr(EnumCodigoErro codigoErroNomeCreddr) {
        this.codigoErroNomeCreddr = codigoErroNomeCreddr;
    }

    public EnumCodigoErro getCodigoErroSitRetReq() {
        return codigoErroSitRetReq;
    }

    public void setCodigoErroSitRetReq(EnumCodigoErro codigoErroSitRetReq) {
        this.codigoErroSitRetReq = codigoErroSitRetReq;
    }

    public EnumCodigoErro getCodigoErroCodOcorc() {
        return codigoErroCodOcorc;
    }

    public void setCodigoErroCodOcorc(EnumCodigoErro codigoErroCodOcorc) {
        this.codigoErroCodOcorc = codigoErroCodOcorc;
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

    public List<Centralizadora> getCentralizadoras() {
        return centralizadoras;
    }

    public void setCentralizadoras(List<Centralizadora> centralizadoras) {
        this.centralizadoras = centralizadoras;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.IdentdPartPrincipal);
        hash = 97 * hash + Objects.hashCode(this.CNPJBaseCreddr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Credenciador other = (Credenciador) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.IdentdPartPrincipal, other.IdentdPartPrincipal)) {
            return false;
        }
        if (!Objects.equals(this.CNPJBaseCreddr, other.CNPJBaseCreddr)) {
            return false;
        }
        return true;
    }

}

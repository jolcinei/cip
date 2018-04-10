/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.model;

import com.jaks.cip.enuns.EnumCodigoErro;
import com.jaks.cip.enuns.EnumServicosEventos;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Jolcinei
 */
public class Arquivo {

    @Column(name = "Id")
    private int id;

    @Column(name = "NomArq", length = 80)
    private String NomArq;

    @Column(name = "NumCtrlEmis", length = 20)
    private String NumCtrlEmis;

    @Column(name = "NumCtrlDestOr", length = 20)
    private String NumCtrlDestOr;

    //Identificador do participante junto ao banco central 8 posições
    @Column(name = "ISPBEmissor", length = 8)
    private String ISPBEmissor;

    //Identificador do participante junto ao banco central 8 posições
    @Column(name = "ISPBDestinatario", length = 8)
    private String ISPBDestinatario;

    @Column(name = "DtHrArq", length = 19)
    private String DtHrArq;

    @Column(name = "SitCons", length = 2)
    private String SitCons;

    @Column(name = "DtRef", length = 10)
    private String DtRef;

    //Pegar os erros por cada campo do arquivo.
    private EnumCodigoErro codigoErroNomArq;
    private EnumCodigoErro codigoErroNumCtrlEmis;
    private EnumCodigoErro codigoErroNumCtrlDestOr;
    private EnumCodigoErro codigoErroISPBEmissor;
    private EnumCodigoErro codigoErroISPBDestinatario;
    private EnumCodigoErro codigoErroDtHrArq;
    private EnumCodigoErro codigoErroSitCons;
    private EnumCodigoErro codigoErroDtRef;

    private EnumServicosEventos servicosEventos;

    private Credenciador credenciador;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomArq() {
        return NomArq;
    }

    public void setNomArq(String NomArq) {
        this.NomArq = NomArq;
    }

    public String getNumCtrlEmis() {
        return NumCtrlEmis;
    }

    public void setNumCtrlEmis(String NumCtrlEmis) {
        this.NumCtrlEmis = NumCtrlEmis;
    }

    public String getNumCtrlDestOr() {
        return NumCtrlDestOr;
    }

    public void setNumCtrlDestOr(String NumCtrlDestOr) {
        this.NumCtrlDestOr = NumCtrlDestOr;
    }

    public String getISPBEmissor() {
        return ISPBEmissor;
    }

    public void setISPBEmissor(String ISPBEmissor) {
        this.ISPBEmissor = ISPBEmissor;
    }

    public String getISPBDestinatario() {
        return ISPBDestinatario;
    }

    public void setISPBDestinatario(String ISPBDestinatario) {
        this.ISPBDestinatario = ISPBDestinatario;
    }

    public String getDtHrArq() {
        return DtHrArq;
    }

    public void setDtHrArq(String DtHrArq) {
        this.DtHrArq = DtHrArq;
    }

    public String getSitCons() {
        return SitCons;
    }

    public void setSitCons(String SitCons) {
        this.SitCons = SitCons;
    }

    public String getDtRef() {
        return DtRef;
    }

    public void setDtRef(String DtRef) {
        this.DtRef = DtRef;
    }

    public Credenciador getCredenciador() {
        return credenciador;
    }

    public void setCredenciador(Credenciador credenciador) {
        this.credenciador = credenciador;
    }

    public EnumCodigoErro getCodigoErroNomArq() {
        return codigoErroNomArq;
    }

    public void setCodigoErroNomArq(EnumCodigoErro codigoErroNomArq) {
        this.codigoErroNomArq = codigoErroNomArq;
    }

    public EnumCodigoErro getCodigoErroNumCtrlEmis() {
        return codigoErroNumCtrlEmis;
    }

    public void setCodigoErroNumCtrlEmis(EnumCodigoErro codigoErroNumCtrlEmis) {
        this.codigoErroNumCtrlEmis = codigoErroNumCtrlEmis;
    }

    public EnumCodigoErro getCodigoErroNumCtrlDestOr() {
        return codigoErroNumCtrlDestOr;
    }

    public void setCodigoErroNumCtrlDestOr(EnumCodigoErro codigoErroNumCtrlDestOr) {
        this.codigoErroNumCtrlDestOr = codigoErroNumCtrlDestOr;
    }

    public EnumCodigoErro getCodigoErroISPBEmissor() {
        return codigoErroISPBEmissor;
    }

    public void setCodigoErroISPBEmissor(EnumCodigoErro codigoErroISPBEmissor) {
        this.codigoErroISPBEmissor = codigoErroISPBEmissor;
    }

    public EnumCodigoErro getCodigoErroISPBDestinatario() {
        return codigoErroISPBDestinatario;
    }

    public void setCodigoErroISPBDestinatario(EnumCodigoErro codigoErroISPBDestinatario) {
        this.codigoErroISPBDestinatario = codigoErroISPBDestinatario;
    }

    public EnumCodigoErro getCodigoErroDtHrArq() {
        return codigoErroDtHrArq;
    }

    public void setCodigoErroDtHrArq(EnumCodigoErro codigoErroDtHrArq) {
        this.codigoErroDtHrArq = codigoErroDtHrArq;
    }

    public EnumCodigoErro getCodigoErroSitCons() {
        return codigoErroSitCons;
    }

    public void setCodigoErroSitCons(EnumCodigoErro codigoErroSitCons) {
        this.codigoErroSitCons = codigoErroSitCons;
    }

    public EnumCodigoErro getCodigoErroDtRef() {
        return codigoErroDtRef;
    }

    public void setCodigoErroDtRef(EnumCodigoErro codigoErroDtRef) {
        this.codigoErroDtRef = codigoErroDtRef;
    }

    public EnumServicosEventos getServicosEventos() {
        return servicosEventos;
    }

    public void setServicosEventos(EnumServicosEventos servicosEventos) {
        this.servicosEventos = servicosEventos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.NomArq);
        hash = 67 * hash + Objects.hashCode(this.DtHrArq);
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
        final Arquivo other = (Arquivo) obj;
        if (!Objects.equals(this.NomArq, other.NomArq)) {
            return false;
        }
        if (!Objects.equals(this.DtHrArq, other.DtHrArq)) {
            return false;
        }
        return true;
    }

}

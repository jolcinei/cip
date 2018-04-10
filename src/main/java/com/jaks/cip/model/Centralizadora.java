/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.model;

import com.jaks.cip.enuns.EnumCodigoErro;
import com.jaks.cip.enuns.EnumTipoConta;
import com.jaks.cip.enuns.EnumTipoRetornado;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Jolcinei
 */
public class Centralizadora {

    @Column(name = "ID")
    private int id;

    //data AAAAMMDD + sequencia de 12 posições.
    @Column(name = "NumCtrlCreddrCentrlz", length = 20)
    private String NumCtrlCreddrCentrlz;

    //F - Física, J - Jurídica
    @Column(name = "TpPessoaCentrlz", length = 1)
    private String TpPessoaCentrlz;

    // CPF ou CNPJ 14 posições.
    @Column(name = "CNPJ_CPFCentrlz", length = 14)
    private String CNPJ_CPFCentrlz;

    //Código de 1 a 25 posições.
    @Column(name = "CodCentrlz", length = 25)
    private String CodCentrlz;

    //Tipo da conta
    @Column(name = "TpCt", length = 2)
    private EnumTipoConta TpCt;

    //Agencia centralizadora sem digito verificador
    @Column(name = "AgCentrlz", length = 4)
    private String AgCentrlz;

    //Conta Corrente centralizadora com digito verificador
    @Column(name = "CtCentrlz", length = 13)
    private String CtCentrlz;

    //Conta Pagamento centralizadora com digito verificador
    @Column(name = "CtPgtoCentrlz", length = 13)
    private String CtPgtoCentrlz;

    //data AAAAMMDD + sequencia de 12 posições.
    @Column(name = "NumCtrlCreddrCentrlzActo", length = 20)
    private String NumCtrlCreddrCentrlzActo;

    //data AAAAMMDD + sequencia de 12 posições.
    @Column(name = "NumCtrlCIPCentrlzActo", length = 20)
    private String NumCtrlCIPCentrlzActo;

    private EnumTipoRetornado enumTipoRetornado;

    //Pegar os erros por cada campo do arquivo.
    private EnumCodigoErro codigoErroNumCtrlCreddrCentrlz;
    private EnumCodigoErro codigoErroTpPessoaCentrlz;
    private EnumCodigoErro codigoErroCNPJ_CPFCentrlz;
    private EnumCodigoErro codigoErroCodCentrlz;
    private EnumCodigoErro codigoErroTpCt;
    private EnumCodigoErro codigoErroAgCentrlz;
    private EnumCodigoErro codigoErroCtCentrlz;
    private EnumCodigoErro codigoErroCtPgtoCentrlz;
    private EnumCodigoErro codigoErroNumCtrlCreddrCentrlzActo;
    private EnumCodigoErro codigoErroNumCtrlCIPCentrlzActo;

    private List<PontoVenda> pontosVenda;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumCtrlCreddrCentrlz() {
        return NumCtrlCreddrCentrlz;
    }

    public void setNumCtrlCreddrCentrlz(String NumCtrlCreddrCentrlz) {
        this.NumCtrlCreddrCentrlz = NumCtrlCreddrCentrlz;
    }

    public String getTpPessoaCentrlz() {
        return TpPessoaCentrlz;
    }

    public void setTpPessoaCentrlz(String TpPessoaCentrlz) {
        this.TpPessoaCentrlz = TpPessoaCentrlz;
    }

    public String getCNPJ_CPFCentrlz() {
        return CNPJ_CPFCentrlz;
    }

    public void setCNPJ_CPFCentrlz(String CNPJ_CPFCentrlz) {
        this.CNPJ_CPFCentrlz = CNPJ_CPFCentrlz;
    }

    public String getCodCentrlz() {
        return CodCentrlz;
    }

    public void setCodCentrlz(String CodCentrlz) {
        this.CodCentrlz = CodCentrlz;
    }

    public EnumTipoConta getTpCt() {
        return TpCt;
    }

    public void setTpCt(EnumTipoConta TpCt) {
        this.TpCt = TpCt;
    }

    public String getAgCentrlz() {
        return AgCentrlz;
    }

    public void setAgCentrlz(String AgCentrlz) {
        this.AgCentrlz = AgCentrlz;
    }

    public String getCtCentrlz() {
        return CtCentrlz;
    }

    public void setCtCentrlz(String CtCentrlz) {
        this.CtCentrlz = CtCentrlz;
    }

    public String getCtPgtoCentrlz() {
        return CtPgtoCentrlz;
    }

    public void setCtPgtoCentrlz(String CtPgtoCentrlz) {
        this.CtPgtoCentrlz = CtPgtoCentrlz;
    }

    public String getNumCtrlCreddrCentrlzActo() {
        return NumCtrlCreddrCentrlzActo;
    }

    public void setNumCtrlCreddrCentrlzActo(String NumCtrlCreddrCentrlzActo) {
        this.NumCtrlCreddrCentrlzActo = NumCtrlCreddrCentrlzActo;
    }

    public String getNumCtrlCIPCentrlzActo() {
        return NumCtrlCIPCentrlzActo;
    }

    public void setNumCtrlCIPCentrlzActo(String NumCtrlCIPCentrlzActo) {
        this.NumCtrlCIPCentrlzActo = NumCtrlCIPCentrlzActo;
    }

    public EnumTipoRetornado getEnumTipoRetornado() {
        return enumTipoRetornado;
    }

    public void setEnumTipoRetornado(EnumTipoRetornado enumTipoRetornado) {
        this.enumTipoRetornado = enumTipoRetornado;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCreddrCentrlz() {
        return codigoErroNumCtrlCreddrCentrlz;
    }

    public void setCodigoErroNumCtrlCreddrCentrlz(EnumCodigoErro codigoErroNumCtrlCreddrCentrlz) {
        this.codigoErroNumCtrlCreddrCentrlz = codigoErroNumCtrlCreddrCentrlz;
    }

    public EnumCodigoErro getCodigoErroTpPessoaCentrlz() {
        return codigoErroTpPessoaCentrlz;
    }

    public void setCodigoErroTpPessoaCentrlz(EnumCodigoErro codigoErroTpPessoaCentrlz) {
        this.codigoErroTpPessoaCentrlz = codigoErroTpPessoaCentrlz;
    }

    public EnumCodigoErro getCodigoErroCNPJ_CPFCentrlz() {
        return codigoErroCNPJ_CPFCentrlz;
    }

    public void setCodigoErroCNPJ_CPFCentrlz(EnumCodigoErro codigoErroCNPJ_CPFCentrlz) {
        this.codigoErroCNPJ_CPFCentrlz = codigoErroCNPJ_CPFCentrlz;
    }

    public EnumCodigoErro getCodigoErroCodCentrlz() {
        return codigoErroCodCentrlz;
    }

    public void setCodigoErroCodCentrlz(EnumCodigoErro codigoErroCodCentrlz) {
        this.codigoErroCodCentrlz = codigoErroCodCentrlz;
    }

    public EnumCodigoErro getCodigoErroTpCt() {
        return codigoErroTpCt;
    }

    public void setCodigoErroTpCt(EnumCodigoErro codigoErroTpCt) {
        this.codigoErroTpCt = codigoErroTpCt;
    }

    public EnumCodigoErro getCodigoErroAgCentrlz() {
        return codigoErroAgCentrlz;
    }

    public void setCodigoErroAgCentrlz(EnumCodigoErro codigoErroAgCentrlz) {
        this.codigoErroAgCentrlz = codigoErroAgCentrlz;
    }

    public EnumCodigoErro getCodigoErroCtCentrlz() {
        return codigoErroCtCentrlz;
    }

    public void setCodigoErroCtCentrlz(EnumCodigoErro codigoErroCtCentrlz) {
        this.codigoErroCtCentrlz = codigoErroCtCentrlz;
    }

    public EnumCodigoErro getCodigoErroCtPgtoCentrlz() {
        return codigoErroCtPgtoCentrlz;
    }

    public void setCodigoErroCtPgtoCentrlz(EnumCodigoErro codigoErroCtPgtoCentrlz) {
        this.codigoErroCtPgtoCentrlz = codigoErroCtPgtoCentrlz;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCreddrCentrlzActo() {
        return codigoErroNumCtrlCreddrCentrlzActo;
    }

    public void setCodigoErroNumCtrlCreddrCentrlzActo(EnumCodigoErro codigoErroNumCtrlCreddrCentrlzActo) {
        this.codigoErroNumCtrlCreddrCentrlzActo = codigoErroNumCtrlCreddrCentrlzActo;
    }

    public EnumCodigoErro getCodigoErroNumCtrlCIPCentrlzActo() {
        return codigoErroNumCtrlCIPCentrlzActo;
    }

    public void setCodigoErroNumCtrlCIPCentrlzActo(EnumCodigoErro codigoErroNumCtrlCIPCentrlzActo) {
        this.codigoErroNumCtrlCIPCentrlzActo = codigoErroNumCtrlCIPCentrlzActo;
    }

    public List<PontoVenda> getPontosVenda() {
        return pontosVenda;
    }

    public void setPontosVenda(List<PontoVenda> pontosVenda) {
        this.pontosVenda = pontosVenda;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.NumCtrlCreddrCentrlz);
        hash = 29 * hash + Objects.hashCode(this.CNPJ_CPFCentrlz);
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
        final Centralizadora other = (Centralizadora) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.NumCtrlCreddrCentrlz, other.NumCtrlCreddrCentrlz)) {
            return false;
        }
        if (!Objects.equals(this.TpPessoaCentrlz, other.TpPessoaCentrlz)) {
            return false;
        }
        if (!Objects.equals(this.CNPJ_CPFCentrlz, other.CNPJ_CPFCentrlz)) {
            return false;
        }
        if (!Objects.equals(this.CodCentrlz, other.CodCentrlz)) {
            return false;
        }
        if (this.TpCt != other.TpCt) {
            return false;
        }
        if (!Objects.equals(this.AgCentrlz, other.AgCentrlz)) {
            return false;
        }
        if (!Objects.equals(this.CtCentrlz, other.CtCentrlz)) {
            return false;
        }
        if (!Objects.equals(this.CtPgtoCentrlz, other.CtPgtoCentrlz)) {
            return false;
        }
        return true;
    }

}

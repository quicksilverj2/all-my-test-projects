
package com.edel.rulestore.dao;

import com.google.gson.annotations.Expose;
import lombok.ToString;

@ToString
public class QuoteObjectParsed {

    @Expose
    private String akSz;
    @Expose
    private String altt;
    @Expose
    private String asTyp;
    @Expose
    private String askPr;
    @Expose
    private String atp;
    @Expose
    private String bdSz;
    @Expose
    private String bidPr;
    @Expose
    private String c;
    @Expose
    private String chg;
    @Expose
    private String chgP;
    @Expose
    private String desc;
    @Expose
    private String dpNm;
    @Expose
    private String exc;
    @Expose
    private String h;
    @Expose
    private String hiCt;
    @Expose
    private Boolean isMTFEnabled;
    @Expose
    private String l;
    @Expose
    private String loCt;
    @Expose
    private String ltSz;
    @Expose
    private String ltTQty;
    @Expose
    private String ltp;
    @Expose
    private String ltt;
    @Expose
    private String lut;
    @Expose
    private String ntTrdVal;
    @Expose
    private String nwsFlg;
    @Expose
    private String op;
    @Expose
    private String opInt;
    @Expose
    private String opIntChg;
    @Expose
    private String rchFlg;
    @Expose
    private String spot;
    @Expose
    private String sym;
    @Expose
    private String tkSz;
    @Expose
    private String trdSym;
    @Expose
    private String vol;
    @Expose
    private String yrH;
    @Expose
    private String yrLw;

    public String getAkSz() {
        return akSz;
    }

    public String getAltt() {
        return altt;
    }

    public String getAsTyp() {
        return asTyp;
    }

    public String getAskPr() {
        return askPr;
    }

    public String getAtp() {
        return atp;
    }

    public String getBdSz() {
        return bdSz;
    }

    public String getBidPr() {
        return bidPr;
    }

    public String getC() {
        return c;
    }

    public String getChg() {
        return chg;
    }

    public String getChgP() {
        return chgP;
    }

    public String getDesc() {
        return desc;
    }

    public String getDpNm() {
        return dpNm;
    }

    public String getExc() {
        return exc;
    }

    public String getH() {
        return h;
    }

    public String getHiCt() {
        return hiCt;
    }

    public Boolean getIsMTFEnabled() {
        return isMTFEnabled;
    }

    public String getL() {
        return l;
    }

    public String getLoCt() {
        return loCt;
    }

    public String getLtSz() {
        return ltSz;
    }

    public String getLtTQty() {
        return ltTQty;
    }

    public String getLtp() {
        return ltp;
    }

    public String getLtt() {
        return ltt;
    }

    public String getLut() {
        return lut;
    }

    public String getNtTrdVal() {
        return ntTrdVal;
    }

    public String getNwsFlg() {
        return nwsFlg;
    }

    public String getOp() {
        return op;
    }

    public String getOpInt() {
        return opInt;
    }

    public String getOpIntChg() {
        return opIntChg;
    }

    public String getRchFlg() {
        return rchFlg;
    }

    public String getSpot() {
        return spot;
    }

    public String getSym() {
        return sym;
    }

    public String getTkSz() {
        return tkSz;
    }

    public String getTrdSym() {
        return trdSym;
    }

    public String getVol() {
        return vol;
    }

    public String getYrH() {
        return yrH;
    }

    public String getYrLw() {
        return yrLw;
    }

    public static class Builder {

        private String akSz;
        private String altt;
        private String asTyp;
        private String askPr;
        private String atp;
        private String bdSz;
        private String bidPr;
        private String c;
        private String chg;
        private String chgP;
        private String desc;
        private String dpNm;
        private String exc;
        private String h;
        private String hiCt;
        private Boolean isMTFEnabled;
        private String l;
        private String loCt;
        private String ltSz;
        private String ltTQty;
        private String ltp;
        private String ltt;
        private String lut;
        private String ntTrdVal;
        private String nwsFlg;
        private String op;
        private String opInt;
        private String opIntChg;
        private String rchFlg;
        private String spot;
        private String sym;
        private String tkSz;
        private String trdSym;
        private String vol;
        private String yrH;
        private String yrLw;

        public QuoteObjectParsed.Builder withAkSz(String akSz) {
            this.akSz = akSz;
            return this;
        }

        public QuoteObjectParsed.Builder withAltt(String altt) {
            this.altt = altt;
            return this;
        }

        public QuoteObjectParsed.Builder withAsTyp(String asTyp) {
            this.asTyp = asTyp;
            return this;
        }

        public QuoteObjectParsed.Builder withAskPr(String askPr) {
            this.askPr = askPr;
            return this;
        }

        public QuoteObjectParsed.Builder withAtp(String atp) {
            this.atp = atp;
            return this;
        }

        public QuoteObjectParsed.Builder withBdSz(String bdSz) {
            this.bdSz = bdSz;
            return this;
        }

        public QuoteObjectParsed.Builder withBidPr(String bidPr) {
            this.bidPr = bidPr;
            return this;
        }

        public QuoteObjectParsed.Builder withC(String c) {
            this.c = c;
            return this;
        }

        public QuoteObjectParsed.Builder withChg(String chg) {
            this.chg = chg;
            return this;
        }

        public QuoteObjectParsed.Builder withChgP(String chgP) {
            this.chgP = chgP;
            return this;
        }

        public QuoteObjectParsed.Builder withDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public QuoteObjectParsed.Builder withDpNm(String dpNm) {
            this.dpNm = dpNm;
            return this;
        }

        public QuoteObjectParsed.Builder withExc(String exc) {
            this.exc = exc;
            return this;
        }

        public QuoteObjectParsed.Builder withH(String h) {
            this.h = h;
            return this;
        }

        public QuoteObjectParsed.Builder withHiCt(String hiCt) {
            this.hiCt = hiCt;
            return this;
        }

        public QuoteObjectParsed.Builder withIsMTFEnabled(Boolean isMTFEnabled) {
            this.isMTFEnabled = isMTFEnabled;
            return this;
        }

        public QuoteObjectParsed.Builder withL(String l) {
            this.l = l;
            return this;
        }

        public QuoteObjectParsed.Builder withLoCt(String loCt) {
            this.loCt = loCt;
            return this;
        }

        public QuoteObjectParsed.Builder withLtSz(String ltSz) {
            this.ltSz = ltSz;
            return this;
        }

        public QuoteObjectParsed.Builder withLtTQty(String ltTQty) {
            this.ltTQty = ltTQty;
            return this;
        }

        public QuoteObjectParsed.Builder withLtp(String ltp) {
            this.ltp = ltp;
            return this;
        }

        public QuoteObjectParsed.Builder withLtt(String ltt) {
            this.ltt = ltt;
            return this;
        }

        public QuoteObjectParsed.Builder withLut(String lut) {
            this.lut = lut;
            return this;
        }

        public QuoteObjectParsed.Builder withNtTrdVal(String ntTrdVal) {
            this.ntTrdVal = ntTrdVal;
            return this;
        }

        public QuoteObjectParsed.Builder withNwsFlg(String nwsFlg) {
            this.nwsFlg = nwsFlg;
            return this;
        }

        public QuoteObjectParsed.Builder withOp(String op) {
            this.op = op;
            return this;
        }

        public QuoteObjectParsed.Builder withOpInt(String opInt) {
            this.opInt = opInt;
            return this;
        }

        public QuoteObjectParsed.Builder withOpIntChg(String opIntChg) {
            this.opIntChg = opIntChg;
            return this;
        }

        public QuoteObjectParsed.Builder withRchFlg(String rchFlg) {
            this.rchFlg = rchFlg;
            return this;
        }

        public QuoteObjectParsed.Builder withSpot(String spot) {
            this.spot = spot;
            return this;
        }

        public QuoteObjectParsed.Builder withSym(String sym) {
            this.sym = sym;
            return this;
        }

        public QuoteObjectParsed.Builder withTkSz(String tkSz) {
            this.tkSz = tkSz;
            return this;
        }

        public QuoteObjectParsed.Builder withTrdSym(String trdSym) {
            this.trdSym = trdSym;
            return this;
        }

        public QuoteObjectParsed.Builder withVol(String vol) {
            this.vol = vol;
            return this;
        }

        public QuoteObjectParsed.Builder withYrH(String yrH) {
            this.yrH = yrH;
            return this;
        }

        public QuoteObjectParsed.Builder withYrLw(String yrLw) {
            this.yrLw = yrLw;
            return this;
        }

        public QuoteObjectParsed build() {
            QuoteObjectParsed quoteObjectParsed = new QuoteObjectParsed();
            quoteObjectParsed.akSz = akSz;
            quoteObjectParsed.altt = altt;
            quoteObjectParsed.asTyp = asTyp;
            quoteObjectParsed.askPr = askPr;
            quoteObjectParsed.atp = atp;
            quoteObjectParsed.bdSz = bdSz;
            quoteObjectParsed.bidPr = bidPr;
            quoteObjectParsed.c = c;
            quoteObjectParsed.chg = chg;
            quoteObjectParsed.chgP = chgP;
            quoteObjectParsed.desc = desc;
            quoteObjectParsed.dpNm = dpNm;
            quoteObjectParsed.exc = exc;
            quoteObjectParsed.h = h;
            quoteObjectParsed.hiCt = hiCt;
            quoteObjectParsed.isMTFEnabled = isMTFEnabled;
            quoteObjectParsed.l = l;
            quoteObjectParsed.loCt = loCt;
            quoteObjectParsed.ltSz = ltSz;
            quoteObjectParsed.ltTQty = ltTQty;
            quoteObjectParsed.ltp = ltp;
            quoteObjectParsed.ltt = ltt;
            quoteObjectParsed.lut = lut;
            quoteObjectParsed.ntTrdVal = ntTrdVal;
            quoteObjectParsed.nwsFlg = nwsFlg;
            quoteObjectParsed.op = op;
            quoteObjectParsed.opInt = opInt;
            quoteObjectParsed.opIntChg = opIntChg;
            quoteObjectParsed.rchFlg = rchFlg;
            quoteObjectParsed.spot = spot;
            quoteObjectParsed.sym = sym;
            quoteObjectParsed.tkSz = tkSz;
            quoteObjectParsed.trdSym = trdSym;
            quoteObjectParsed.vol = vol;
            quoteObjectParsed.yrH = yrH;
            quoteObjectParsed.yrLw = yrLw;
            return quoteObjectParsed;
        }

    }

}

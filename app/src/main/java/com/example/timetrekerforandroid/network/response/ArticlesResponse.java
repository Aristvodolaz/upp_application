package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class ArticlesResponse {
    @SerializedName("success") boolean success;
    @SerializedName("value") List<Articuls> articuls;
    @SerializedName("errorCode") int errorCode;

    public class Articuls {
        @SerializedName("ID")
        private Long id;

        @SerializedName("Pref")
        private String pref;

        @SerializedName("Nazvanie_Zadaniya")
        private String nazvanieZadaniya;

        @SerializedName("Status_Zadaniya")
        private Integer statusZadaniya;

        @SerializedName("Ispolnitel")
        private String ispolnitel;

        @SerializedName("Status")
        private Integer status;

        @SerializedName("Artikul")
        private Integer artikul;

        @SerializedName("Artikul_Syrya")
        private String artikulSyrya;

        @SerializedName("Nomenklatura")
        private Long nomenklatura;

        @SerializedName("Nazvanie_Tovara")
        private String nazvanieTovara;

        @SerializedName("SHK")
        private String shk;
        @SerializedName("SHK_Syrya")
        private String shkSyrya;

        @SerializedName("SHK_SPO")
        private String shkSpo;

        @SerializedName("SHK_SPO_1")
        private String shkSpo1;

        @SerializedName("Kol_vo_Syrya")
        private String kolVoSyrya;

        @SerializedName("Itog_Zakaz")
        private Integer itogZakaz;

        @SerializedName("Sht_v_MP")
        private Integer shtvMP;

        @SerializedName("Itog_MP")
        private Integer itogMP;

        @SerializedName("SOH")
        private String soh;

        @SerializedName("Tip_Postavki")
        private String tipPostavki;

        @SerializedName("Srok_Godnosti")
        private String srokGodnosti;

        @SerializedName("Op_1_Bl_1_Sht")
        private String op1Bl1Sht;

        @SerializedName("Op_2_Bl_2_Sht")
        private String op2Bl2Sht;

        @SerializedName("Op_3_Bl_3_Sht")
        private String op3Bl3Sht;

        @SerializedName("Op_4_Bl_4_Sht")
        private String op4Bl4Sht;

        @SerializedName("Op_5_Bl_5_Sht")
        private String op5Bl5Sht;

        @SerializedName("Op_6_Blis_6_10_Sht")
        private String op6Blis610Sht;

        @SerializedName("Op_7_Pereschyot")
        private String op7Pereschyot;

        @SerializedName("Op_9_Fasovka_Sborka")
        private String op9FasovkaSborka;

        @SerializedName("Op_10_Markirovka_SHT")
        private String op10MarkirovkaSht;

        @SerializedName("Op_11_Markirovka_Prom")
        private String op11MarkirovkaProm;

        @SerializedName("Op_12_Markirovka_Prom")
        private String op12MarkirovkaProm;

        @SerializedName("Op_13_Markirovka_Fabr")
        private String op13MarkirovkaFabr;

        @SerializedName("Op_14_TU_1_Sht")
        private String op14Tu1Sht;

        @SerializedName("Op_15_TU_2_Sht")
        private String op15Tu2Sht;

        @SerializedName("Op_16_TU_3_5")
        private String op16Tu35;

        @SerializedName("Op_17_TU_6_8")
        private String op17Tu68;

        @SerializedName("Op_468_Proverka_SHK")
        private String op468ProverkaShk;

        @SerializedName("Op_469_Spetsifikatsiya_TM")
        private String op469SpetsifikatsiyaTm;

        @SerializedName("Op_470_Dop_Upakovka")
        private String op470DopUpakovka;

        @SerializedName("Mesto")
        private Integer mesto;

        @SerializedName("Vlozhennost")
        private Integer vlozhennost;

        @SerializedName("Pallet_No")
        private Integer palletNo;

        @SerializedName("Time_Start")
        private String timeStart;

        @SerializedName("Time_Middle")
        private String timeMiddle;

        @SerializedName("Time_End")
        private String timeEnd;

        @SerializedName("Persent")
        private String persent;

        @SerializedName("SHK_WPS")
        private String shkWps;

        public Articuls(Long id, String pref, String nazvanieZadaniya, Integer statusZadaniya, String ispolnitel, Integer status, Integer artikul, String artikulSyrya, Long nomenklatura, String nazvanieTovara, String shk, String shkSpo, String shkSpo1, String kolVoSyrya, Integer itogZakaz, Integer shtvMP, Integer itogMP, String soh, String tipPostavki, String srokGodnosti, String op1Bl1Sht, String op2Bl2Sht, String op3Bl3Sht, String op4Bl4Sht, String op5Bl5Sht, String op6Blis610Sht, String op7Pereschyot, String op9FasovkaSborka, String op10MarkirovkaSht, String op11MarkirovkaProm, String op12MarkirovkaProm, String op13MarkirovkaFabr, String op14Tu1Sht, String op15Tu2Sht, String op16Tu35, String op17Tu68, String op468ProverkaShk, String op469SpetsifikatsiyaTm, String op470DopUpakovka, Integer mesto, Integer vlozhennost, Integer palletNo, String timeStart, String timeMiddle, String timeEnd, String persent, String shkWps) {
            this.id = id;
            this.pref = pref;
            this.nazvanieZadaniya = nazvanieZadaniya;
            this.statusZadaniya = statusZadaniya;
            this.ispolnitel = ispolnitel;
            this.status = status;
            this.artikul = artikul;
            this.artikulSyrya = artikulSyrya;
            this.nomenklatura = nomenklatura;
            this.nazvanieTovara = nazvanieTovara;
            this.shk = shk;
            this.shkSpo = shkSpo;
            this.shkSpo1 = shkSpo1;
            this.kolVoSyrya = kolVoSyrya;
            this.itogZakaz = itogZakaz;
            this.shtvMP = shtvMP;
            this.itogMP = itogMP;
            this.soh = soh;
            this.tipPostavki = tipPostavki;
            this.srokGodnosti = srokGodnosti;
            this.op1Bl1Sht = op1Bl1Sht;
            this.op2Bl2Sht = op2Bl2Sht;
            this.op3Bl3Sht = op3Bl3Sht;
            this.op4Bl4Sht = op4Bl4Sht;
            this.op5Bl5Sht = op5Bl5Sht;
            this.op6Blis610Sht = op6Blis610Sht;
            this.op7Pereschyot = op7Pereschyot;
            this.op9FasovkaSborka = op9FasovkaSborka;
            this.op10MarkirovkaSht = op10MarkirovkaSht;
            this.op11MarkirovkaProm = op11MarkirovkaProm;
            this.op12MarkirovkaProm = op12MarkirovkaProm;
            this.op13MarkirovkaFabr = op13MarkirovkaFabr;
            this.op14Tu1Sht = op14Tu1Sht;
            this.op15Tu2Sht = op15Tu2Sht;
            this.op16Tu35 = op16Tu35;
            this.op17Tu68 = op17Tu68;
            this.op468ProverkaShk = op468ProverkaShk;
            this.op469SpetsifikatsiyaTm = op469SpetsifikatsiyaTm;
            this.op470DopUpakovka = op470DopUpakovka;
            this.mesto = mesto;
            this.vlozhennost = vlozhennost;
            this.palletNo = palletNo;
            this.timeStart = timeStart;
            this.timeMiddle = timeMiddle;
            this.timeEnd = timeEnd;
            this.persent = persent;
            this.shkWps = shkWps;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPref() {
            return pref;
        }

        public void setPref(String pref) {
            this.pref = pref;
        }

        public String getNazvanieZadaniya() {
            return nazvanieZadaniya;
        }

        public void setNazvanieZadaniya(String nazvanieZadaniya) {
            this.nazvanieZadaniya = nazvanieZadaniya;
        }

        public Integer getStatusZadaniya() {
            return statusZadaniya;
        }

        public void setStatusZadaniya(Integer statusZadaniya) {
            this.statusZadaniya = statusZadaniya;
        }

        public String getIspolnitel() {
            return ispolnitel;
        }

        public void setIspolnitel(String ispolnitel) {
            this.ispolnitel = ispolnitel;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getArtikul() {
            return artikul;
        }

        public void setArtikul(Integer artikul) {
            this.artikul = artikul;
        }

        public String getArtikulSyrya() {
            return artikulSyrya;
        }

        public void setArtikulSyrya(String artikulSyrya) {
            this.artikulSyrya = artikulSyrya;
        }

        public Long getNomenklatura() {
            return nomenklatura;
        }

        public void setNomenklatura(Long nomenklatura) {
            this.nomenklatura = nomenklatura;
        }

        public String getNazvanieTovara() {
            return nazvanieTovara;
        }

        public void setNazvanieTovara(String nazvanieTovara) {
            this.nazvanieTovara = nazvanieTovara;
        }

        public String getShk() {
            return shk;
        }

        public void setShk(String shk) {
            this.shk = shk;
        }

        public String getShkSpo() {
            return shkSpo;
        }

        public void setShkSpo(String shkSpo) {
            this.shkSpo = shkSpo;
        }

        public String getShkSpo1() {
            return shkSpo1;
        }

        public void setShkSpo1(String shkSpo1) {
            this.shkSpo1 = shkSpo1;
        }

        public String getKolVoSyrya() {
            return kolVoSyrya;
        }

        public void setKolVoSyrya(String kolVoSyrya) {
            this.kolVoSyrya = kolVoSyrya;
        }

        public Integer getItogZakaz() {
            return itogZakaz;
        }

        public void setItogZakaz(Integer itogZakaz) {
            this.itogZakaz = itogZakaz;
        }

        public Integer getShtvMP() {
            return shtvMP;
        }

        public void setShtvMP(Integer shtvMP) {
            this.shtvMP = shtvMP;
        }

        public Integer getItogMP() {
            return itogMP;
        }

        public void setItogMP(Integer itogMP) {
            this.itogMP = itogMP;
        }

        public String getSoh() {
            return soh;
        }

        public void setSoh(String soh) {
            this.soh = soh;
        }

        public String getTipPostavki() {
            return tipPostavki;
        }

        public void setTipPostavki(String tipPostavki) {
            this.tipPostavki = tipPostavki;
        }

        public String getSrokGodnosti() {
            return srokGodnosti;
        }

        public void setSrokGodnosti(String srokGodnosti) {
            this.srokGodnosti = srokGodnosti;
        }

        public String getOp1Bl1Sht() {
            return op1Bl1Sht;
        }

        public void setOp1Bl1Sht(String op1Bl1Sht) {
            this.op1Bl1Sht = op1Bl1Sht;
        }

        public String getOp2Bl2Sht() {
            return op2Bl2Sht;
        }

        public void setOp2Bl2Sht(String op2Bl2Sht) {
            this.op2Bl2Sht = op2Bl2Sht;
        }

        public String getOp3Bl3Sht() {
            return op3Bl3Sht;
        }

        public void setOp3Bl3Sht(String op3Bl3Sht) {
            this.op3Bl3Sht = op3Bl3Sht;
        }

        public String getOp4Bl4Sht() {
            return op4Bl4Sht;
        }

        public void setOp4Bl4Sht(String op4Bl4Sht) {
            this.op4Bl4Sht = op4Bl4Sht;
        }

        public String getOp5Bl5Sht() {
            return op5Bl5Sht;
        }

        public void setOp5Bl5Sht(String op5Bl5Sht) {
            this.op5Bl5Sht = op5Bl5Sht;
        }

        public String getOp6Blis610Sht() {
            return op6Blis610Sht;
        }

        public void setOp6Blis610Sht(String op6Blis610Sht) {
            this.op6Blis610Sht = op6Blis610Sht;
        }

        public String getOp7Pereschyot() {
            return op7Pereschyot;
        }

        public void setOp7Pereschyot(String op7Pereschyot) {
            this.op7Pereschyot = op7Pereschyot;
        }

        public String getOp9FasovkaSborka() {
            return op9FasovkaSborka;
        }

        public void setOp9FasovkaSborka(String op9FasovkaSborka) {
            this.op9FasovkaSborka = op9FasovkaSborka;
        }

        public String getOp10MarkirovkaSht() {
            return op10MarkirovkaSht;
        }

        public void setOp10MarkirovkaSht(String op10MarkirovkaSht) {
            this.op10MarkirovkaSht = op10MarkirovkaSht;
        }

        public String getOp11MarkirovkaProm() {
            return op11MarkirovkaProm;
        }

        public void setOp11MarkirovkaProm(String op11MarkirovkaProm) {
            this.op11MarkirovkaProm = op11MarkirovkaProm;
        }

        public String getOp12MarkirovkaProm() {
            return op12MarkirovkaProm;
        }

        public void setOp12MarkirovkaProm(String op12MarkirovkaProm) {
            this.op12MarkirovkaProm = op12MarkirovkaProm;
        }

        public String getOp13MarkirovkaFabr() {
            return op13MarkirovkaFabr;
        }

        public void setOp13MarkirovkaFabr(String op13MarkirovkaFabr) {
            this.op13MarkirovkaFabr = op13MarkirovkaFabr;
        }

        public String getOp14Tu1Sht() {
            return op14Tu1Sht;
        }

        public void setOp14Tu1Sht(String op14Tu1Sht) {
            this.op14Tu1Sht = op14Tu1Sht;
        }

        public String getOp15Tu2Sht() {
            return op15Tu2Sht;
        }

        public void setOp15Tu2Sht(String op15Tu2Sht) {
            this.op15Tu2Sht = op15Tu2Sht;
        }

        public String getOp16Tu35() {
            return op16Tu35;
        }

        public void setOp16Tu35(String op16Tu35) {
            this.op16Tu35 = op16Tu35;
        }

        public String getOp17Tu68() {
            return op17Tu68;
        }

        public void setOp17Tu68(String op17Tu68) {
            this.op17Tu68 = op17Tu68;
        }

        public String getOp468ProverkaShk() {
            return op468ProverkaShk;
        }

        public void setOp468ProverkaShk(String op468ProverkaShk) {
            this.op468ProverkaShk = op468ProverkaShk;
        }

        public String getShkSyrya() {
            return shkSyrya;
        }

        public void setShkSyrya(String shkSyrya) {
            this.shkSyrya = shkSyrya;
        }

        public String getOp469SpetsifikatsiyaTm() {
            return op469SpetsifikatsiyaTm;
        }

        public void setOp469SpetsifikatsiyaTm(String op469SpetsifikatsiyaTm) {
            this.op469SpetsifikatsiyaTm = op469SpetsifikatsiyaTm;
        }

        public String getOp470DopUpakovka() {
            return op470DopUpakovka;
        }

        public void setOp470DopUpakovka(String op470DopUpakovka) {
            this.op470DopUpakovka = op470DopUpakovka;
        }

        public Integer getMesto() {
            return mesto;
        }

        public void setMesto(Integer mesto) {
            this.mesto = mesto;
        }

        public Integer getVlozhennost() {
            return vlozhennost;
        }

        public void setVlozhennost(Integer vlozhennost) {
            this.vlozhennost = vlozhennost;
        }

        public Integer getPalletNo() {
            return palletNo;
        }

        public void setPalletNo(Integer palletNo) {
            this.palletNo = palletNo;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeMiddle() {
            return timeMiddle;
        }

        public void setTimeMiddle(String timeMiddle) {
            this.timeMiddle = timeMiddle;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
        }

        public String getPersent() {
            return persent;
        }

        public void setPersent(String persent) {
            this.persent = persent;
        }

        public String getShkWps() {
            return shkWps;
        }

        public void setShkWps(String shkWps) {
            this.shkWps = shkWps;
        }
    }

    public ArticlesResponse(boolean success, List<Articuls> articuls, int errorCode) {
        this.success = success;
        this.articuls = articuls;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Articuls> getArticuls() {
        return articuls;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

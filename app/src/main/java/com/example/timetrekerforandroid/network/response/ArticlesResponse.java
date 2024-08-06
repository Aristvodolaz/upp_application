package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlesResponse {
    @SerializedName("success") boolean success;
    @SerializedName("value") List<Articuls> articuls;
    @SerializedName("errorCode") int errorCode;

    public class Articuls {
        @SerializedName("ID") String id;
        @SerializedName("Nazvanie_Zadaniya") String nazvanie;
        @SerializedName("Pref") String pref;
        @SerializedName("Status") int status;
        @SerializedName("Artikul") int articul;
        @SerializedName("Ispolnitel") String ispolnitel;
        @SerializedName("Artikul_Syrya") String articulSyrya;
        @SerializedName("Nomenklatura") int nomenklatura;
        @SerializedName("Nazvanie_Tovara") String nazvanieTovara;
        @SerializedName("SHK") String shk;
        @SerializedName("SHK_SPO") String shkSpo;
        @SerializedName("SHK_SPO_1") String shkSpo1;
        @SerializedName("Kol_vo_Syrya") String kolvoSyrya;
        @SerializedName("Itog_Zakaz") int itogZakaz;
        @SerializedName("SOH") String soh;
        @SerializedName("Tip_Postavki") String tipPostavki;
        @SerializedName("Srok_Godnosti") String srokGodnosti;
        @SerializedName("Op_1_Bl_1_Sht") String op1;
        @SerializedName("Op_2_Bl_2_Sht") String op2;
        @SerializedName("Op_3_Bl_3_Sht") String op3;
        @SerializedName("Op_4_Bl_4_Sht") String op4;
        @SerializedName("Op_5_Bl_5_Sht") String op5;
        @SerializedName("Op_6_Blis_6_10_Sht") String op6;
        @SerializedName("Op_7_Pereschyot") String op7;
        @SerializedName("Op_9_Fasovka_Sborka") String op9;
        @SerializedName("Op_10_Markirovka_SHT") String op10;
        @SerializedName("Op_11_Markirovka_Prom") String op11;
        @SerializedName("Op_12_Markirovka_Prom") String op12;
        @SerializedName("Op_13_Markirovka_Fabr") String op13;
        @SerializedName("Op_14_TU_1_Sht") String op14;
        @SerializedName("Op_15_TU_2_Sht") String op15;
        @SerializedName("Op_16_TU_3_5") String op16;
        @SerializedName("Op_17_TU_6_8") String op17;
        @SerializedName("Op_468_Proverka_SHK") String op468;
        @SerializedName("Op_469_Spetsifikatsiya_TM") String op469;
        @SerializedName("Op_470_Dop_Upakovka") String op470;
        @SerializedName("Mesto") int mesto;
        @SerializedName("Vlozhennost") int vlozhennost;
        @SerializedName("Pallet_No") int pallet;

        public Articuls(String id, String nazvanie, String pref, int status, int articul, String ispolnitel, String articulSyrya, int nomenklatura, String nazvanieTovara, String shk, String shkSpo, String shkSpo1, String kolvoSyrya, int itogZakaz, String soh, String tipPostavki, String srokGodnosti, String op1, String op2, String op3, String op4, String op5, String op6, String op7, String op9, String op10, String op11, String op12, String op13, String op14, String op15, String op16, String op17, String op468, String op469, String op470, int mesto, int vlozhennost, int pallet) {
            this.id = id;
            this.nazvanie = nazvanie;
            this.pref = pref;
            this.status = status;
            this.articul = articul;
            this.ispolnitel = ispolnitel;
            this.articulSyrya = articulSyrya;
            this.nomenklatura = nomenklatura;
            this.nazvanieTovara = nazvanieTovara;
            this.shk = shk;
            this.shkSpo = shkSpo;
            this.shkSpo1 = shkSpo1;
            this.kolvoSyrya = kolvoSyrya;
            this.itogZakaz = itogZakaz;
            this.soh = soh;
            this.tipPostavki = tipPostavki;
            this.srokGodnosti = srokGodnosti;
            this.op1 = op1;
            this.op2 = op2;
            this.op3 = op3;
            this.op4 = op4;
            this.op5 = op5;
            this.op6 = op6;
            this.op7 = op7;
            this.op9 = op9;
            this.op10 = op10;
            this.op11 = op11;
            this.op12 = op12;
            this.op13 = op13;
            this.op14 = op14;
            this.op15 = op15;
            this.op16 = op16;
            this.op17 = op17;
            this.op468 = op468;
            this.op469 = op469;
            this.op470 = op470;
            this.mesto = mesto;
            this.vlozhennost = vlozhennost;
            this.pallet = pallet;
        }

        public String getIspolnitel() {
            return ispolnitel;
        }

        public String getPref() {
            return pref;
        }

        public String getId() {
            return id;
        }

        public String getNazvanie() {
            return nazvanie;
        }

        public int getStatus() {
            return status;
        }

        public int getArticul() {
            return articul;
        }

        public String getArticulSyrya() {
            return articulSyrya;
        }

        public int getNomenklatura() {
            return nomenklatura;
        }

        public String getNazvanieTovara() {
            return nazvanieTovara;
        }

        public String getShk() {
            return shk;
        }

        public String getShkSpo() {
            return shkSpo;
        }

        public String getShkSpo1() {
            return shkSpo1;
        }

        public String getKolvoSyrya() {
            return kolvoSyrya;
        }

        public int getItogZakaz() {
            return itogZakaz;
        }

        public String getSoh() {
            return soh;
        }

        public String getTipPostavki() {
            return tipPostavki;
        }

        public String getSrokGodnosti() {
            return srokGodnosti;
        }

        public String getOp1() {
            return op1;
        }

        public String getOp2() {
            return op2;
        }

        public String getOp3() {
            return op3;
        }

        public String getOp4() {
            return op4;
        }

        public String getOp5() {
            return op5;
        }

        public String getOp6() {
            return op6;
        }

        public String getOp7() {
            return op7;
        }

        public String getOp9() {
            return op9;
        }

        public String getOp10() {
            return op10;
        }

        public String getOp11() {
            return op11;
        }

        public String getOp12() {
            return op12;
        }

        public String getOp13() {
            return op13;
        }

        public String getOp14() {
            return op14;
        }

        public String getOp15() {
            return op15;
        }

        public String getOp16() {
            return op16;
        }

        public String getOp17() {
            return op17;
        }

        public String getOp468() {
            return op468;
        }

        public String getOp469() {
            return op469;
        }

        public String getOp470() {
            return op470;
        }

        public int getMesto() {
            return mesto;
        }

        public int getVlozhennost() {
            return vlozhennost;
        }

        public int getPallet() {
            return pallet;
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

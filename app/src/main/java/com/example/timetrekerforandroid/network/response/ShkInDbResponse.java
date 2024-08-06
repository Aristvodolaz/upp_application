package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShkInDbResponse {
    @SerializedName("success") boolean success;
    @SerializedName("value")
    List<ShkInfo> value;
    @SerializedName("errorCode") int errorCode;

    public ShkInDbResponse(boolean success, List<ShkInfo> value, int errorCode) {
        this.success = success;
        this.value = value;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ShkInfo> getValue() {
        return value;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public class ShkInfo {
        @SerializedName("ID") String article;
        @SerializedName("ARTICLE_MEASURE_ID") String articleMeasure;
        @SerializedName("IS_ACTIVE") int status;
        @SerializedName("NAME") String name;
        @SerializedName("PIECE_GTIN") String shk;
        @SerializedName("FPACK_GTIN") String shkFP;
        @SerializedName("IS_VALID_PERIOD_WATCH") int periodWatch;
        @SerializedName("VALID_PERIOD_DAYS") int periodDays;

        public ShkInfo(String article, String articleMeasure, int status, String name, String shk, String shkFP, int periodWatch, int periodDays) {
            this.article = article;
            this.articleMeasure = articleMeasure;
            this.status = status;
            this.name = name;
            this.shk = shk;
            this.shkFP = shkFP;
            this.periodWatch = periodWatch;
            this.periodDays = periodDays;
        }

        public int getPeriodWatch() {
            return periodWatch;
        }

        public int getPeriodDays() {
            return periodDays;
        }

        public String getShkFP() {
            return shkFP;
        }

        public String getArticle() {
            return article;
        }

        public String getArticleMeasure() {
            return articleMeasure;
        }

        public int getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public String getShk() {
            return shk;
        }
    }
}

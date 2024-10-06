package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChooseOpResponse {
    @SerializedName("success")
        private boolean success;

        @SerializedName("value")
        private List<Value> value;

        @SerializedName("errorCode")
        private int errorCode;

        // Getters and setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<Value> getValue() {
            return value;
        }

    public ChooseOpResponse(boolean success, List<Value> value, int errorCode) {
        this.success = success;
        this.value = value;
        this.errorCode = errorCode;
    }

    public void setValue(List<Value> value) {
            this.value = value;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
    }



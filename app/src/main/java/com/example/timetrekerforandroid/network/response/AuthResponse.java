package com.example.timetrekerforandroid.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {
    @SerializedName("success") boolean success;
    @SerializedName("value")
    List<Value> value;
    @SerializedName("errorCode") int errorCode;

    public AuthResponse(boolean success, List<Value> value, int errorCode) {
        this.success = success;
        this.value = value;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Value> getValue() {
        return value;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public class Value {
        @SerializedName("ID") int id;
        @SerializedName("FULL_NAME") String name;

        public Value(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}

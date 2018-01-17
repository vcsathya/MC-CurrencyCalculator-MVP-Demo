package com.currencyconverter.vcsathya.mc.data.models;

import com.google.gson.annotations.SerializedName;

public class Exchange {

    @SerializedName("baseCode")
    String baseCode;

    @SerializedName("targetCode")
    String targetCode;

    @SerializedName("rate")
    float rate;

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}

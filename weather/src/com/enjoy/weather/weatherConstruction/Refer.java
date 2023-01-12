package com.enjoy.weather.weatherConstruction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Refer {
    @JsonProperty("sources")
    private String[] sources;
    @JsonProperty("license")
    private String[] license;

    public String[] getSources() {
        return sources;
    }

    public void setSources(String[] sources) {
        this.sources = sources;
    }

    public String[] getLicense() {
        return license;
    }

    public void setLicense(String[] license) {
        this.license = license;
    }
}

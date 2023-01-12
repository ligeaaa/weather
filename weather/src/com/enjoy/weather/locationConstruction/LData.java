package com.enjoy.weather.locationConstruction;

import com.enjoy.weather.weatherConstruction.Refer;

public class LData {
    private String code;
    private Location[] location = new Location[20];
    private Refer refer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Location[] getLocation() {
        return location;
    }

    public void setLocation(Location[] location) {
        this.location = location;
    }

    public Refer getRefer() {
        return refer;
    }

    public void setRefer(Refer refer) {
        this.refer = refer;
    }
}

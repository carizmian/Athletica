package com.dimitrioskanellopoulos.athletica;

public class SunrisePaint extends AbstractSensorPaint {

    private String icon = "\uf185";

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getUnits(){
        return units;
    }

}
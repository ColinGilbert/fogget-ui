/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import noob.plantsystem.common.ArduinoProxy;

import javax.servlet.jsp.JspWriter;

/**
 *
 * @author noob
 */
public class TextArea {

    JspWriter writer;
    ArduinoProxy proxy;

    boolean mistingInterval = false;
    boolean mistingDuration = false;
    boolean statusUpdatePushInterval = false;
    boolean nutrientSolutionRatio = false;
    boolean lightsOntime = false;
    boolean lightsOffTime = false;
    boolean targetUpperChamberTemperature = false;
    boolean targetUpperChamberHumidity = false;
    boolean targetLowerChamberTemperature = false;
    boolean targetCO2PPM = false;
    boolean set = false;

    public TextArea(JspWriter writer, ArduinoProxy proxy) {
        this.writer = writer;
        this.proxy = proxy;
    }

    @Override
    public String toString() {
        String results = "";
        if (set) {

            results += "<input type=\"text\" name=\"";
            if (mistingInterval) {
                results += "update misting interval\" id=\"misting-interval-\"";
            }
            if (mistingDuration) {
                results += "update misting duration\" id=\"misting-duration-\"";
            }
            if (statusUpdatePushInterval) {
                results += "update status update push interval\" id=\"status-push-interval-\"";
            }
            if (nutrientSolutionRatio) {
                results += "update nutrient solution to water ratio\" id=\"nutrient-ratio-\"";
            }
            if (lightsOntime) {
                results += "update lights-off time\" id=\"lights-off-time-\"";
            }
            if (lightsOffTime) {
                results += "update lights-on time\" id=\"lights-on-time-\"";
            }
            if (targetUpperChamberTemperature) {
                results += "update target upper chamber temperature\" id=\"target-upper-temperature-\"";
            }
            if (targetUpperChamberHumidity) {
                results += "update target upper chamber humidity\" id=\"target-upper-humidity-\"";
            }
            if (targetLowerChamberTemperature) {
                results += "update target lower chamber temperature\" id=\"target-lower-humidity-\"";
            }
            if (targetCO2PPM) {
                results += "update target CO2 PPM\" id=\"target-co2-ppm-\"";
            }
            results += proxy.getUid();
            results += " />";
            set = false;
        }
        return results;
    }

    public void setMistingInterval() {
        if (!set) {
            reset();
        }
        mistingInterval = true;
        set = true;
    }

    public void setMistingDuration() {
        if (!set) {
            reset();
        }
        mistingDuration = true;
        set = true;
    }

    public void setStatusUpdatePushInterval() {
        if (!set) {
            reset();
        }
        statusUpdatePushInterval = true;
        set = true;
    }

    public void setNutrientSolutionRatio() {
        if (!set) {
            reset();
        }
        nutrientSolutionRatio = true;
        set = true;
    }

    public void setLightsOnTime() {
        if (!set) {
            reset();
        }
        lightsOntime = true;
        set = true;
    }

    public void setLightsOffTime() {
        if (!set) {
            reset();
        }
        lightsOffTime = true;
        set = true;
    }

    public void setTargetUpperChamberHumidity() {
        if (!set) {
            reset();
        }
        targetUpperChamberHumidity = true;
        set = true;
    }

    public void setTargetUpperChamberTemperature() {
        if (!set) {
            reset();
        }
        targetUpperChamberTemperature = true;
        set = true;
    }

    public void setTargetLowerChamberTemperature() {
        if (!set) {
            reset();
        }
        targetLowerChamberTemperature = true;
        set = true;
    }

    public void setTargetCO2PPM() {
        if (!set) {
            reset();
        }
        targetCO2PPM = true;
        set = true;
    }

    protected void reset() {
        mistingInterval = false;
        mistingDuration = false;
        statusUpdatePushInterval = false;
        nutrientSolutionRatio = false;
        lightsOffTime = false;
        lightsOntime = false;
        targetUpperChamberTemperature = false;
        targetUpperChamberHumidity = false;
        targetLowerChamberTemperature = false;
        targetCO2PPM = false;
        set = false;
    }
}

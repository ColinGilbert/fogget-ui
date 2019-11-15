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
public class TextInput {

    JspWriter writer;
    //ArduinoProxy proxy;
    boolean mistingInterval = false;
    boolean mistingDuration = false;
    boolean statusPushInterval = false;
    boolean nutrientSolutionRatio = false;
    boolean lightsOnHour = false;
    boolean lightsOnMinute = false;
    boolean lightsOffHour = false;
    boolean lightsOffMinute = false;
    boolean targetUpperChamberTemperature = false;
    boolean targetUpperChamberHumidity = false;
    boolean targetLowerChamberTemperature = false;
    boolean targetCO2PPM = false;
    boolean description = false;
    boolean set = false;
    int width = 0;
    int height = 0;
    long uid = 0;

    public TextInput(JspWriter writer, long uidArg) {
        this.writer = writer;
        uid = uidArg;
    }

    @Override
    public String toString() {
        boolean isArea = false;
        String results = "";
        if (set) {
            if (height < 2) {
                results += "<input type=\"text\" ";
                if (width > 0) {
                    results += "size=\"";
                    results += width;
                    results += "\" ";
                }
            } else {
                isArea = true;
                results += "<textarea ";
                if (height > 0) {
                    results += "rows=\"";
                    results += height;
                    results += "\" ";
                    if (width > 0) {
                        results += "cols=\"";
                        results += width;
                        results += "\"";
                    }
                }
            }
            results += " name=\"";
            if (mistingInterval) {
                results += ParameterNames.updateMistingInterval;
            }
            if (mistingDuration) {
                results += ParameterNames.updateMistingDuration;
            }
            if (statusPushInterval) {
                results += ParameterNames.updateStatusPushInterval;
            }
            if (nutrientSolutionRatio) {
                results += ParameterNames.updateNutrientSolutionRatio;
            }
            if (lightsOnHour) {
                results += ParameterNames.updateLightsOnHour;
            }
            if (lightsOnMinute) {
                results += ParameterNames.updateLightsOnMinute;
            }
            if (lightsOffHour) {
                results += ParameterNames.updateLightsOffHour;
            }
            if (lightsOffMinute) {
                results += ParameterNames.updateLightsOffMinute;
            }
            if (targetUpperChamberTemperature) {
                results += ParameterNames.updateTargetUpperChamberTemperature;
            }
            if (targetUpperChamberHumidity) {
                results += ParameterNames.updateTargetUpperChamberHumidity;
            }
            if (targetLowerChamberTemperature) {
                results += ParameterNames.updateTargetLowerChamberTemperature;
            }
            if (targetCO2PPM) {
                results += ParameterNames.updateTargetCO2PPM;
            }
            if (description) {
                results += ParameterNames.updateDescription;
            }
            results += "-";
            results += uid;
            results += "\" ";
            if (!isArea)
            {
            results += " />";
            }
            else {
                results += "></textArea>";
            }
            set = false;
        }
        return results;
    }

    public void setWidth(int arg) {
        width = Math.abs(arg);
    }

    public void setHeight(int arg) {
        height = Math.abs(arg);
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

    public void setStatusPushInterval() {
        if (!set) {
            reset();
        }
        statusPushInterval = true;
        set = true;
    }

    public void setNutrientSolutionRatio() {
        if (!set) {
            reset();
        }
        nutrientSolutionRatio = true;
        set = true;
    }

    public void setLightsOnHour() {
        if (!set) {
            reset();
        }
        lightsOnHour = true;
        set = true;
    }

    public void setLightsOnMinute() {
        if (!set) {
            reset();
        }
        lightsOnHour = true;
        set = true;
    }

    public void setLightsOffHour() {
        if (!set) {
            reset();
        }
        lightsOffMinute = true;
        set = true;
    }

    public void setLightsOffMinute() {
        if (!set) {
            reset();
        }
        lightsOffMinute = true;
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

    public void setDescription() {
        if (!set) {
            reset();
        }
        description = true;
        set = true;
    }

    protected void reset() {
        mistingInterval = false;
        mistingDuration = false;
        statusPushInterval = false;
        nutrientSolutionRatio = false;
        lightsOffHour = false;
        lightsOffMinute = false;
        lightsOnHour = false;
        lightsOffMinute = false;
        targetUpperChamberTemperature = false;
        targetUpperChamberHumidity = false;
        targetLowerChamberTemperature = false;
        targetCO2PPM = false;
        description = false;
        set = false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import noob.plantsystem.common.ArduinoConfigChangeRepresentation;
import noob.plantsystem.common.TopicStrings;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 *
 * @author noob
 */
public class StatePushControllerServlet extends HttpServlet {

    boolean validateTimeOfDay(int hours, int minutes) {
        if (hours < 0 || minutes < 0) {
            return false;
        }
        if (hours > 23) {
            return false;
        }
        if (minutes > 59) {
            return false;
        }

        return true;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        BackendCommunicationHandler backend = new BackendCommunicationHandler();
        boolean connected = backend.connect();
        if (connected) {
            Pattern pattern = Pattern.compile("^[0-9]+$"); // Only numerics
            Enumeration<String> parameterNames = request.getParameterNames();
            TreeMap<Long, ArduinoConfigChangeRepresentation> sentToBackend = new TreeMap<>();
            boolean changingDescriptions = false;
            TreeMap<Long, String> descriptionsToChange = new TreeMap<>();
            while (parameterNames.hasMoreElements()) {
                String fullParamName = parameterNames.nextElement();
                String splitParamName[] = fullParamName.split("-");
                if (splitParamName.length > 1) { // If we actually have a multipart parameter name, as we should
                    Matcher match = pattern.matcher(splitParamName[1]);
                    if (match.find()) { // If the latter half of our parameter name is only numeric
                        System.out.println("Got proper params for machine " + splitParamName[1]);
                        boolean changingOffTimeHours = false;
                        boolean changingOffTimeMinutes = false;
                        boolean changingOnTimeHours = false;
                        boolean changingOnTimeMinutes = false;
                        int hourOn = 0;
                        int hourOff = 0;
                        int minuteOn = 0;
                        int minuteOff = 0;

                        long uid = Long.parseLong(splitParamName[1]);
                        String[] paramValues = request.getParameterValues(fullParamName);
                        ArduinoConfigChangeRepresentation configChange;// = new ArduinoConfigChangeRepresentation();
                        if (sentToBackend.containsKey(uid)) {
                            configChange = sentToBackend.get(uid);
                        } else {
                            configChange = new ArduinoConfigChangeRepresentation();
//                            configChange.setPersistentState(new);
                        }
                        for (String paramValue : paramValues) {
                            if (!"".equals(paramValue)) { // If we actually have a parameter to send to our backend
                                final String firstPart = splitParamName[0];
                                try {
                                    uid = Long.parseLong(splitParamName[1]);
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(StatePushControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
                                    continue;
                                }
                                   configChange.getPersistentState().setUid(uid);
                                try {
                                    switch (firstPart) {
                                        case (ParameterNames.updateMistingInterval): {
                                            configChange.getPersistentState().setMistingInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateMistingDuration): {
                                            configChange.getPersistentState().setMistingDuration(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingDuration(true);
                                            break;
                                        }
                                        case (ParameterNames.updateStatusPushInterval): {
                                            configChange.getPersistentState().setStatusPushInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingStatusPushInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateNutrientSolutionRatio): {
                                            configChange.getPersistentState().setNutrientSolutionRatio(Float.parseFloat(paramValue));
                                            configChange.setChangingNutrientSolutionRatio(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOffHour): {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            hourOff = Integer.parseInt(paramValue);
                                            changingOffTimeHours = true;
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOffMinute): {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            minuteOff = Integer.parseInt(paramValue);
                                            changingOffTimeMinutes = true;
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOnHour): {
                                            // timeOnAccumulator += 3600000 * Long.parseLong(paramValue);
                                            hourOn = Integer.parseInt(paramValue);
                                            changingOnTimeHours = true;
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOnMinute): {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            // timeOnAccumulator += 60000 * Long.parseLong(paramValue);
                                            minuteOn = Integer.parseInt(paramValue);
                                            changingOnTimeMinutes = true;
                                            break;
                                        }
                                        case (ParameterNames.updateTargetUpperChamberHumidity): {
                                            configChange.getPersistentState().setTargetUpperChamberHumidity(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberHumidity(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetUpperChamberTemperature): {
                                            configChange.getPersistentState().setTargetUpperChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberTemperature(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetLowerChamberTemperature): {
                                            configChange.getPersistentState().setTargetLowerChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetLowerChamberTemperature(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetCO2PPM): {
                                            configChange.getPersistentState().setTargetCO2PPM(Integer.parseInt(paramValue));
                                            configChange.setChangingTargetCO2PPM(true);
                                            break;
                                        }
                                        case (ParameterNames.updateDescription): {
                                            descriptionsToChange.put(uid, paramValue);
                                            changingDescriptions = true;
                                            break;
                                        }
                                        default: {
                                            System.out.println("Invalid parameter name: " + firstPart);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(StatePushControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (changingOnTimeHours || changingOnTimeMinutes) {
                                boolean valid = changingOnTimeHours && validateTimeOfDay(hourOn, minuteOn);
                                changingOnTimeHours = changingOnTimeHours && valid;
                                changingOnTimeMinutes = changingOnTimeMinutes && valid;
                            }
                            if (changingOffTimeHours || changingOffTimeMinutes) {
                                boolean valid = validateTimeOfDay(hourOff, minuteOff);
                                changingOffTimeHours = changingOffTimeHours && valid;
                                changingOffTimeMinutes = changingOffTimeMinutes && valid;
                            }
                            long timeOnAccumulator = 0;
                            if (changingOnTimeHours) {
                                timeOnAccumulator = 3600000 * hourOn;
                            }
                            if (changingOnTimeMinutes) {
                                timeOnAccumulator += 60000 * minuteOn;
                            }
                            long timeOffAccumulator = 0;
                            if (changingOffTimeHours) {
                                timeOffAccumulator = 3600000 * hourOff;
                            }
                            if (changingOffTimeMinutes) {
                                timeOffAccumulator += 60000 * minuteOff;
                            }
                            if (changingOnTimeHours || changingOnTimeMinutes) {
                                configChange.getPersistentState().setLightsOnTime(timeOnAccumulator);
                                configChange.setChangingLightsOnTime(true);
                            }
                            if (changingOffTimeHours || changingOffTimeMinutes) {
                                configChange.getPersistentState().setLightsOffTime(timeOffAccumulator);
                                configChange.setChangingLightsOffTime(true);
                            }
                            // TODO: Check if some params have more than one value
                        }

                        if (configChange.hasChanges()) {
                            sentToBackend.put(uid, configChange);
                        } else {
                            System.out.println("Trying to add invalid state changer");
                        }

                    }
                }
            }
            // out.close();
            // setConfigValues
            if (sentToBackend.size() > 0) {
                ArrayList<ArduinoConfigChangeRepresentation> results = new ArrayList<>();
                for (ArduinoConfigChangeRepresentation r : sentToBackend.values()) {
                    results.add(r);
                }
                backend.sendControlInformation(results);
                System.out.println("Sent message to backend.");
            }
            if (changingDescriptions) {
                backend.sendSystemDescriptionUpdate(descriptionsToChange);
            }
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");

        dispatcher.forward(request, response);
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

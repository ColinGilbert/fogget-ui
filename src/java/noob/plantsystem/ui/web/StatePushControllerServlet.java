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
            ArrayList<ArduinoConfigChangeRepresentation> sentToBackend = new ArrayList<>();
            boolean changingDescriptions = false;
            TreeMap<Long, String> descriptionsToChange = new TreeMap<>();
            while (parameterNames.hasMoreElements()) {
                String fullParamName = parameterNames.nextElement();
                String splitParamName[] = fullParamName.split("-");
                if (splitParamName.length > 1) { // If we actually have a multipart parameter name, as we should
                    Matcher match = pattern.matcher(splitParamName[1]);
                    if (match.find()) { // If the latter half of our parameter name is only numeric
                        System.out.println("Got proper params for machine " + splitParamName[1]);
                        long timeOffAccumulator = 0;
                        long timeOnAccumulator = 0;
                        boolean changingOffTime = false;
                        boolean changingOnTime = false;
                        long uid;
                        String[] paramValues = request.getParameterValues(fullParamName);
                        ArduinoConfigChangeRepresentation configChange = new ArduinoConfigChangeRepresentation();
                        for (String paramValue : paramValues) {
                            if (!"".equals(paramValue)) { // If we actually have a parameter to send to our backend
                                final String firstPart = splitParamName[0];
                                try {
                                    uid = Long.parseLong(splitParamName[1]);
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(StatePushControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
                                    continue;
                                }
                                try {
                                    configChange.setUid(uid);
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(StatePushControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
                                    break;
                                }
                                switch (firstPart) {
                                    case (ParameterNames.updateMistingInterval): {
                                        try {
                                            configChange.setMistingInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingInterval(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateMistingDuration): {
                                        try {
                                            configChange.setMistingDuration(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingDuration(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateStatusPushInterval): {
                                        try {
                                            configChange.setStatusPushInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingStatusPushInterval(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateNutrientSolutionRatio): {
                                        try {
                                            configChange.setNutrientSolutionRatio(Float.parseFloat(paramValue));
                                            configChange.setChangingNutrientSolutionRatio(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateLightsOffHour): {
                                        try {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            timeOffAccumulator += 3600000 * Long.parseLong(paramValue);
                                            changingOffTime = true;
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateLightsOffMinute): {
                                        try {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            timeOffAccumulator += 60000 * Long.parseLong(paramValue);
                                            changingOffTime = true;
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateLightsOnHour): {
                                        try {

                                            timeOnAccumulator += 3600000 * Long.parseLong(paramValue);
                                            changingOnTime = true;
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateLightsOnMinute): {
                                        try {
                                            // configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            // configChange.setChangingLightsOffTime(true);
                                            timeOnAccumulator += 60000 * Long.parseLong(paramValue);
                                            changingOnTime = true;
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateTargetUpperChamberHumidity): {
                                        try {
                                            configChange.setTargetUpperChamberHumidity(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberHumidity(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateTargetUpperChamberTemperature): {
                                        try {
                                            configChange.setTargetUpperChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberTemperature(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateTargetLowerChamberTemperature): {
                                        try {
                                            configChange.setTargetLowerChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetLowerChamberTemperature(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateTargetCO2PPM): {
                                        try {
                                            configChange.setTargetCO2PPM(Integer.parseInt(paramValue));
                                            configChange.setChangingTargetCO2PPM(true);
                                        } catch (NumberFormatException ex) {
                                        }
                                        break;
                                    }
                                    case (ParameterNames.updateDescription): {
                                        try {
                                            descriptionsToChange.put(uid, paramValue);
                                            changingDescriptions = true;
                                        } catch (NumberFormatException ex) {
                                            changingDescriptions = true;
                                        }
                                        break;
                                    }

                                    default: {
                                        System.out.println("Invalid parameter name: " + firstPart);
                                    }
                                }
                                if (changingOffTime) {
                                    configChange.setLightsOffTime(Long.parseLong(paramValue));
                                    configChange.setChangingLightsOffTime(true);
                                }
                                if (changingOnTime) {
                                    configChange.setLightsOnTime(Long.parseLong(paramValue));
                                    configChange.setChangingLightsOnTime(true);
                                }

                                // TODO: Check if some params have more than one value
                            }
                        }
                        if (configChange.hasChanges()) {
                            sentToBackend.add(configChange);
                        } else {
                            System.out.println("Trying to add invalid state changer");
                        }

                    }
                }
            }
            // out.close();
            // setConfigValues
            if (sentToBackend.size() > 0) {
                backend.sendControlInformation(sentToBackend);
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

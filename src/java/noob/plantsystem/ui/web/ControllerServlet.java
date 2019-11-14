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
public class ControllerServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        /*
    private boolean changingMistingInterval = false;
    private boolean changingMistingDuration = false;
    private boolean changingStatusPushInterval = false;
        
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
    private boolean  = false;
         */
        BackendCommunicationHandler backend = new BackendCommunicationHandler();

        boolean connected = backend.connect();
        if (connected) {

            Pattern pattern = Pattern.compile("^[0-9]+$"); // Only numerics
            Enumeration<String> parameterNames = request.getParameterNames();

            ArrayList<ArduinoConfigChangeRepresentation> sentToBackend = new ArrayList<>();

            while (parameterNames.hasMoreElements()) {

                String fullParamName = parameterNames.nextElement();
                String splitParamName[] = fullParamName.split("-");
                if (splitParamName.length > 1) { // If we actually have a multipart parameter name, as we should

                    Matcher match = pattern.matcher(splitParamName[1]);
                    if (match.find()) { // If the latter half of our parameter name is only numeric
                        System.out.println("Got proper param names for element ." + splitParamName[0]);

                        String[] paramValues = request.getParameterValues(fullParamName);
                        ArduinoConfigChangeRepresentation configChange = new ArduinoConfigChangeRepresentation();
                        for (int i = 0; i < paramValues.length; i++) {
                            String paramValue = paramValues[i];
                            if (!"".equals(paramValue)) { // If we actually have a parameter to send to our backend
                                final String firstPart = splitParamName[0];
                                try {
                                    configChange.setUid(Long.parseLong(splitParamName[1]));
                                    switch (firstPart) {

                                        case (ParameterNames.updateMistingInterval): {
                                            configChange.setMistingInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateMistingDuration): {
                                            configChange.setMistingDuration(Integer.parseInt(paramValue));
                                            configChange.setChangingMistingDuration(true);
                                            break;
                                        }
                                        case (ParameterNames.updateStatusPushInterval): {
                                            configChange.setStatusPushInterval(Integer.parseInt(paramValue));
                                            configChange.setChangingStatusPushInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateNutrientSolutionRatio): {
                                            configChange.setNutrientSolutionRatio(Float.parseFloat(paramValue));
                                            configChange.setChangingNutrientSolutionRatio(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOffTime): {
                                            configChange.setLightsOffTime(Long.parseLong(paramValue));
                                            configChange.setChangingLightsOffTime(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOnTime): {
                                            configChange.setLightsOnTime(Long.parseLong(paramValue));
                                            configChange.setChangingLightsOnTime(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetUpperChamberHumidity): {
                                            configChange.setTargetUpperChamberHumidity(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberHumidity(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetUpperChamberTemperature): {
                                            configChange.setTargetUpperChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetUpperChamberTemperature(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetLowerChamberTemperature): {
                                            configChange.setTargetLowerChamberTemperature(Float.parseFloat(paramValue));
                                            configChange.setChangingTargetLowerChamberTemperature(true);
                                            break;
                                        }
                                        case (ParameterNames.updateTargetCO2PPM): {
                                            configChange.setTargetCO2PPM(Integer.parseInt(paramValue));
                                            configChange.setChangingTargetCO2PPM(true);
                                            break;
                                        }
                                        default: {
                                            System.out.println("Invalid parameter name in ControllerServlet: " + firstPart);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);

                                }
                                // TODO: Check if some params have more than one value
                            }
                        }
                        sentToBackend.add(configChange);
                    }
                }
            }

            // out.close();
            // setConfigValues
            if (sentToBackend.size() > 0) {
                backend.sendControlInformation(sentToBackend);
                System.out.println("Sent message to backend.");
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

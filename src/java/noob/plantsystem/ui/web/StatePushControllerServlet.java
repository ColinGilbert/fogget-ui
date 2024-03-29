/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
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
import noob.plantsystem.common.EmbeddedSystemConfigChangeMemento;


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
        BackendCommunicationFacade backend = new BackendCommunicationFacade();
        boolean connected = backend.connect();
        if (connected) {
            Pattern pattern = Pattern.compile("^[0-9]+$"); // Only numerics
            Enumeration<String> parameterNames = request.getParameterNames();
            TreeMap<Long, EmbeddedSystemConfigChangeMemento> sentToBackend = new TreeMap<>();
            boolean changingDescriptions = false;
            TreeMap<Long, String> descriptionsToChange = new TreeMap<>();
            while (parameterNames.hasMoreElements()) {
                String fullParamName = parameterNames.nextElement();
                String splitParamName[] = fullParamName.split("-");
                if (splitParamName.length > 1) { // If we actually have a multipart parameter name, as we should
                    Matcher match = pattern.matcher(splitParamName[1]);
                    if (match.find()) { // If the latter half of our parameter name is only numeric
                        System.out.println("Got proper params for machine " + splitParamName[1]);
                        long uid = Long.parseLong(splitParamName[1]);
                        String[] paramValues = request.getParameterValues(fullParamName);
                        EmbeddedSystemConfigChangeMemento configChange;// = new EmbeddedSystemConfigChangeMemento();
                        if (sentToBackend.containsKey(uid)) {
                            configChange = sentToBackend.get(uid);
                        } else {
                            configChange = new EmbeddedSystemConfigChangeMemento();
                           // configChange.setPersistentState(new);
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
                                            configChange.getPersistentState().setMistingInterval(Integer.parseInt(paramValue) * 1000);
                                            configChange.setChangingMistingInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateMistingDuration): {
                                            configChange.getPersistentState().setMistingDuration(Integer.parseInt(paramValue) * 1000);
                                            configChange.setChangingMistingDuration(true);
                                            break;
                                        }
                                        case (ParameterNames.updateStatusPushInterval): {
                                            configChange.getPersistentState().setStatusPushInterval(Integer.parseInt(paramValue) * 1000);
                                            configChange.setChangingStatusPushInterval(true);
                                            break;
                                        }
                                        case (ParameterNames.updateNutrientSolutionRatio): {
                                            configChange.getPersistentState().setNutrientSolutionRatio(Float.parseFloat(paramValue));
                                            configChange.setChangingNutrientSolutionRatio(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOffHour): {
                                            configChange.getPersistentState().setLightsOffHour(Integer.parseInt(paramValue));
                                            configChange.setChangingLightsOffHour(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOffMinute): {
                                            configChange.getPersistentState().setLightsOffMinute(Integer.parseInt(paramValue));
                                            configChange.setChangingLightsOffMinute(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOnHour): {
                                            configChange.getPersistentState().setLightsOnHour(Integer.parseInt(paramValue));
                                            configChange.setChangingLightsOnHour(true);
                                            break;
                                        }
                                        case (ParameterNames.updateLightsOnMinute): {
                                            configChange.getPersistentState().setLightsOnMinute(Integer.parseInt(paramValue));
                                            configChange.setChangingLightsOnMinute(true);
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
                                        //    System.out.println("Invalid parameter name: " + firstPart);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    Logger.getLogger(StatePushControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (configChange.hasChanges()) {
                                sentToBackend.put(uid, configChange);
                            } else {
                             //   System.out.println("Trying to add invalid state changer");
                            }
                        }
                    }
                }
            }
            if (sentToBackend.size() > 0) {
                ArrayList<EmbeddedSystemConfigChangeMemento> results = new ArrayList<>();
                for (EmbeddedSystemConfigChangeMemento r : sentToBackend.values()) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import noob.plantsystem.common.EventRecordMemento;
import noob.plantsystem.common.EmbeddedSystemCombinedStateMemento;
import noob.plantsystem.common.EmbeddedSystemConfigChangeMemento;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.TreeMap;
import noob.plantsystem.common.CommonValues;
import noob.plantsystem.common.ConnectionCloser;

/**
 *
 * @author noob
 */
public class BackendCommunicationFacade {

    protected MqttClient client;
    protected ObjectMapper mapper = new ObjectMapper();

    public boolean connect() {
        
        MqttConnectOptions connectionOptions = new MqttConnectOptions();
        
        connectionOptions.setCleanSession(true); // We want the broker to remember past subscriptions.
        try {
            client = new MqttClient(CommonValues.mqttBrokerURL, CommonValues.mqttServletClientID, new MemoryPersistence());
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        try {
            client.connect();
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println("Connected to backend. :)");
        return true;
    }

    public void close() {
        try {
            client.disconnect();
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            client.close();
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Disconnected from backend. Goodbye.");
    }

    public TreeMap<Long, String> getSystemDescriptionsView() {
        TreeMap<Long, String> results = new TreeMap();
        Socket socket = null;
        String response = null;
        try {
            socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return results;
        }
        PrintWriter tcpOut = null;
        Scanner tcpIn = null;
        try {
            tcpIn = new Scanner(socket.getInputStream());
            tcpOut = new PrintWriter(socket.getOutputStream(), true);
            tcpOut.println(CommonValues.getDescriptionsForUI);
            response = tcpIn.nextLine();
            System.out.println("Descriptions view response : " + response);
            results = mapper.readValue(response, new TypeReference<TreeMap<Long, String>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        try {
            tcpIn.close();
        } catch (Exception ex) {
               Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            tcpOut.close();
        } catch (Exception ex) {
                Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);        
        }
        try {
        tcpOut.close();
        } catch (Exception ex) {
                        Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);

        }
*/
        ConnectionCloser.closeConnection(tcpIn, tcpOut, socket);
        return results;
    }

    public TreeMap<Long, EmbeddedSystemCombinedStateMemento> getSystemsView() {
            Socket socket = null;
            PrintWriter tcpOut = null;
             Scanner tcpIn = null;
            TreeMap<Long, EmbeddedSystemCombinedStateMemento> results = new TreeMap<>();
            String response = "";
        try {
            socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
            tcpIn = new Scanner(socket.getInputStream());
                tcpOut = new PrintWriter(socket.getOutputStream(), true);
                tcpOut.println(CommonValues.getSystemsForUI);
                response = tcpIn.nextLine();
                System.out.println("Systems view response : " + response);
                results = mapper.readValue(response, new TypeReference<TreeMap<Long, EmbeddedSystemCombinedStateMemento>>() {
                });
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        ConnectionCloser.closeConnection(tcpIn, tcpOut, socket);
        return results;
    }

    public TreeMap<Long, ArrayDeque<EventRecordMemento>> getEventsView() {
        Socket socket = null;
         PrintWriter tcpOut = null;
         Scanner tcpIn = null;
                     TreeMap<Long, ArrayDeque<EventRecordMemento>> results = new TreeMap<>();
                     String response  = "";
        try {
            socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
           
                tcpIn = new Scanner(socket.getInputStream());
                tcpOut = new PrintWriter(socket.getOutputStream(), true);
                tcpOut.println(CommonValues.getEventsForUI);
                response = tcpIn.nextLine();              
                System.out.println("Events view response: " + response);
                results = mapper.readValue(response, new TypeReference<TreeMap<Long, ArrayDeque<EventRecordMemento>>>() {
                    
                });
            } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
          } catch (Exception ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        ConnectionCloser.closeConnection(tcpIn, tcpOut, socket);
        return results;
    }

    public void sendControlInformation(ArrayList<EmbeddedSystemConfigChangeMemento> arg) {
        MqttMessage message = new MqttMessage();
        message.setQos(1);
        ObjectMapper mapper = new ObjectMapper();
        try {
            message.setPayload(mapper.writeValueAsString(arg).getBytes());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            System.out.println(message.toString());
            client.publish(CommonValues.configEmbeddedRequestTopic, message);
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendSystemDescriptionUpdate(TreeMap<Long, String> descriptions) {
        MqttMessage message = new MqttMessage();
        ObjectMapper mapper = new ObjectMapper();
        try {
            message.setPayload(mapper.writeValueAsString(descriptions).getBytes());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            System.out.println(message.toString());
            client.publish(CommonValues.updateDescriptionRequestTopic, message);
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

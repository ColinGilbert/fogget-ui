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
import noob.plantsystem.common.TopicStrings;
import noob.plantsystem.common.EventRecord;
import noob.plantsystem.common.ArduinoProxy;
import noob.plantsystem.common.ArduinoConfigChangeRepresentation;
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

/**
 *
 * @author noob
 */
public class BackendCommunicationHandler {

    public boolean connect() {
        MqttConnectOptions connectionOptions = new MqttConnectOptions();
        connectionOptions.setCleanSession(true); // We want the broker to remember past subscriptions.
        try {
            client = new MqttClient(CommonValues.mqttBrokerURL, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        try {
            client.connect();
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println("Connected to backend. :)");
        return true;
    }

    public TreeMap<Long, String> getSystemDescriptionsView() {
        try {
            Socket socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
            PrintWriter tcpOut;
            TreeMap<Long, String> results;
            try (Scanner tcpIn = new Scanner(socket.getInputStream())) {
                tcpOut = new PrintWriter(socket.getOutputStream(), true);
                tcpOut.println(CommonValues.getDescriptionsForUI);
                String response = tcpIn.nextLine();
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("Descriptions view response : " + response);
                results = mapper.readValue(response, new TypeReference<TreeMap<Long, String>>() {
                });
            }
            tcpOut.close();
            return results;
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public TreeMap<Long, ArduinoProxy> getSystemsView() {
        try {
            Socket socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
            PrintWriter tcpOut;
            TreeMap<Long, ArduinoProxy> results;
            try (Scanner tcpIn = new Scanner(socket.getInputStream())) {
                tcpOut = new PrintWriter(socket.getOutputStream(), true);
                tcpOut.println(CommonValues.getProxiesForUI);
                String response = tcpIn.nextLine();
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("Systems view response : " + response);
                results = mapper.readValue(response, new TypeReference<TreeMap<Long, ArduinoProxy>>() {
                });
            }
            tcpOut.close();
            
            return results;

        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public TreeMap<Long, ArrayDeque<EventRecord>> getEventsView() {
        try {
            Socket socket = new Socket(CommonValues.localhost, CommonValues.localUIPort);
            PrintWriter tcpOut;
            TreeMap<Long, ArrayDeque<EventRecord>> results;
            try (Scanner tcpIn = new Scanner(socket.getInputStream())) {
                tcpOut = new PrintWriter(socket.getOutputStream(), true);
                tcpOut.println(CommonValues.getEventsForUI);
                String response = tcpIn.nextLine();
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("Events view response: " + response);
                results = mapper.readValue(response, new TypeReference<TreeMap<Long, ArrayDeque<EventRecord>>>() {
                });
            } 
            tcpOut.close();
            return results;
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public void sendControlInformation(ArrayList<ArduinoConfigChangeRepresentation> arg) {
        MqttMessage message = new MqttMessage();
        message.setQos(1);
        ObjectMapper mapper = new ObjectMapper();
        try {
            message.setPayload(mapper.writeValueAsString(arg).getBytes());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            System.out.println(message.toString());
            client.publish(TopicStrings.stateControlRequest(), message);
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        public void sendSystemDescriptionUpdate(TreeMap<Long, String> descriptions) {
       MqttMessage message = new MqttMessage();
        ObjectMapper mapper = new ObjectMapper();
        try {
            message.setPayload(mapper.writeValueAsString(descriptions).getBytes());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            System.out.println(message.toString());
            client.publish(TopicStrings.descriptionsUpdateRequest(), message);
        } catch (MqttException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    protected MqttClient client;

    TreeMap<Long, ArrayDeque<EventRecord>> eventsViewData = new TreeMap<>();
    ArrayList<ArduinoProxy> systemsViewData = new ArrayList<>();
}

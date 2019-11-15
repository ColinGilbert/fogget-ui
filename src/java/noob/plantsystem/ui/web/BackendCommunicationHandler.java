/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.io.IOException;
import java.io.InputStream;

import noob.plantsystem.common.TopicStrings;
import noob.plantsystem.common.PersistentArduinoState;
import noob.plantsystem.common.EventRecord;
import noob.plantsystem.common.ArduinoProxy;
import noob.plantsystem.common.EventsViewRequestRepresentation;
import noob.plantsystem.common.ArduinoConfigChangeRepresentation;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.json.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.TokenStreamFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.TreeMap;

/**
 *
 * @author noob
 */
public class BackendCommunicationHandler {

    protected int port = 6777;

    public boolean connect() {
        MqttConnectOptions connectionOptions = new MqttConnectOptions();
        connectionOptions.setCleanSession(true); // We want the broker to remember past subscriptions.
        final String brokerURL = "tcp://127.0.0.1:1883";
        try {
            client = new MqttClient(brokerURL, MqttClient.generateClientId(), new MemoryPersistence());
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
            Socket socket = new Socket("127.0.0.1", port);
            Scanner tcpIn = new Scanner(socket.getInputStream());
            PrintWriter tcpOut = new PrintWriter(socket.getOutputStream(), true);
            tcpOut.println("GETDESCRIPTIONS");
            String response = tcpIn.nextLine();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Systems view response : " + response);
            return mapper.readValue(response, new TypeReference<TreeMap<Long, String>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public TreeMap<Long, ArduinoProxy> getSystemsView() {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            Scanner tcpIn = new Scanner(socket.getInputStream());
            PrintWriter tcpOut = new PrintWriter(socket.getOutputStream(), true);
            tcpOut.println("GETPROXIES");
            String response = tcpIn.nextLine();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Systems view response : " + response);
            return mapper.readValue(response, new TypeReference<TreeMap<Long, ArduinoProxy>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public TreeMap<Long, ArrayDeque<EventRecord>> getEventsView() {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            Scanner tcpIn = new Scanner(socket.getInputStream());
            PrintWriter tcpOut = new PrintWriter(socket.getOutputStream(), true);
            tcpOut.println("GETEVENTS");
            String response = tcpIn.nextLine();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Events view response: " + response);
            return mapper.readValue(response, new TypeReference<TreeMap<Long, ArrayDeque<EventRecord>>>() {
            });
            // out.println(scanner.nextLine());
            // System.out.println(tcpIn.nextLine());

        } catch (IOException ex) {
            Logger.getLogger(BackendCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TreeMap<>();
    }

    public void sendControlInformation(ArrayList<ArduinoConfigChangeRepresentation> arg) {
        MqttMessage message = new MqttMessage();

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

    protected MqttClient client;

    TreeMap<Long, ArrayDeque<EventRecord>> eventsViewData = new TreeMap<>();
    ArrayList<ArduinoProxy> systemsViewData = new ArrayList<>();
}

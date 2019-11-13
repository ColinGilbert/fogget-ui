<%-- 
    Document   : index
    Created on : Oct 23, 2019, 10:55:13 PM
    Author     : noob
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ArrayDeque" %>
<%@ page import="noob.plantsystem.common.EventRecord" %>
        

<%@ page import="noob.plantsystem.ui.web.BackendCommunicationHandler" %>
<%@ page import="noob.plantsystem.common.ArduinoProxy" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fogget-About-It-Grow-System</title>
    </head>
    <body>
        <h1>Plant growth cluster management system web interface.</h1>
        <p>
            <%
                BackendCommunicationHandler backend = new BackendCommunicationHandler();
                boolean connected = backend.connect();
                if (connected) {
                    ArrayList<ArduinoProxy> systems = backend.getSystemsView();
                    out.print(systems);
                   // TreeMap<Long, ArrayDeque<EventRecord>> events = backend.getEventsView();
                }
            %>
        </p>
    </body>
</html>
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

<%@ taglib prefix="noob" uri="WEB-INF/tlds/fogget-tags.tld"%>
<%@ page import="noob.plantsystem.ui.web.BackendCommunicationHandler" %>
<%@ page import="noob.plantsystem.common.ArduinoProxy" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fogget-About-It-Grow-System</title>
    </head>
    <body>
        <h3>Plant growth cluster management system web interface.</h3>
        <form name="config-changer" action="ControllerServlet">
            <input type="submit">
            <noob:SystemsView/>
            <input type="submit">
        </form>
    </body>
</html>
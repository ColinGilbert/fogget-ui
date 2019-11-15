<%-- 
    Document   : eventview
    Created on : Nov 11, 2019, 9:01:30 PM
    Author     : noob
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ArrayDeque" %>
<%@ page import="noob.plantsystem.common.EventRecord" %>
<%@ page import="noob.plantsystem.ui.web.BackendCommunicationHandler" %>
<%@ page import="noob.plantsystem.common.ArduinoProxy" %>
<%@ taglib prefix="noob" uri="WEB-INF/tlds/fogget-tags.tld"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fogget-About-It-Grow-System</title>
    </head>
    <body>
        <h3>Plant growth cluster management system web interface.</h3>
        <a href="index.jsp" >Go back</a>
        <noob:EventsView uid=""/>
        <a href="index.jsp" >Go back</a>

    </body>
</html>
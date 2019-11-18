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

<%@ taglib prefix="noob" uri="WEB-INF/tlds/fogget-tags.tld"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Fogget-About-It Grow System</title>
<link rel="stylesheet" type="text/css" href="default.css" />
</head>
<body>
<h3>Fogget-About-It fogponics farm management system</h3>
<h4>Version 1.0</h4>
<p>This is where you view the overall state of your machine network and change settings.</p>
<p><b>Note:</b> Growboxes that cannot be reached over the network get automatically taken off this list and will be restored upon rediscovery.</p>
<form name="config-changer" action="ControllerServlet">
<noob:SystemsView/>
</form>
</body>
</html>
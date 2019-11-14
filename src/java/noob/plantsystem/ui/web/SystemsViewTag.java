/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import noob.plantsystem.common.ArduinoProxy;

import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author noob
 */
public class SystemsViewTag extends SimpleTagSupport {

    protected void printRow(JspWriter writer, TableCell cells[]) throws IOException {
        writer.println("<tr>");
        for (TableCell c : cells) {
            writer.println(c.toString());
        }
        writer.println("</tr>");
    }

    protected TableCell booleanCell(boolean arg) {
        TableCell c = new TableCell();
        if (arg) {
            c.setContents("Yes");
        } else {
            c.setContents("No");
        }
        return c;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        //out.println("<h1 style=\"color:#440000\">Bank of Colin.</h1><h3 style=\"color:#003300\">No refunds!</h3>");
        BackendCommunicationHandler backend = new BackendCommunicationHandler();
        //final int tableRows = 8;
        boolean connected = backend.connect();
        if (connected) {
            ArrayList<ArduinoProxy> systems = backend.getSystemsView();
            for (ArduinoProxy sys : systems) {
                out.println("<table>");
                TableCell cells[] = new TableCell[3];
                final int tableRows = 7;
                TextArea text = new TextArea(out, sys);
                // Instantiate the objects
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;//new TableCell();
                }
                for (int i = 0; i < cells.length; i++) {
                    cells[i].setHeader(true);
                }
                // Print the header.
                cells[0].setColSpan(3);
                cells[2].setColSpan(4);
                //
                cells[0].setContents("Property");
                cells[1].setContents("Value");
                cells[2].setContents("Update to");

                printRow(out, cells);
                for (int i = 0; i < cells.length; i++) {
                    cells[i].setHeader(false);
                }
                // Print the misting interval
                cells[0].setContents("Misting interval");
                cells[1].setContents(sys.getMistingInterval() + "ms");
                text.setMistingInterval();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Print the misting duration
                cells[0].setContents("Misting duration");
                cells[1].setContents(sys.getMistingDuration() + "ms");
                text.setMistingDuration();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Print the status push interval
                cells[0].setContents("Status push interval: ");//sys.getStatusPushUpdateInterval())
                cells[1].setContents(sys.getStatusPushInterval() + "ms");
                text.setStatusPushInterval();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Nutrients to water feed ratio
                cells[0].setContents("Nutrient sol'n to water ratio");
                cells[1].setContents(Double.toString(sys.getNutrientSolutionRatio()));
                text.setNutrientSolutionRatio();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Lights-on time
                cells[0].setContents("Lights-On time");
                cells[1].setContents(Long.toString(sys.getLightsOnTime()));
                text.setLightsOnTime();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Lights-off time
                cells[0].setContents("Lights-Off time");
                cells[1].setContents(Long.toString(sys.getLightsOffTime()));
                text.setLightsOffTime();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Current upper chamber humidity
                cells[0].setContents("Current upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getCurrentUpperChamberHumidity()));
                text.setTargetUpperChamberHumidity();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Target upper chamber humidity
                cells[0].setContents("Target upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getTargetUpperChamberHumidity()));
                text.setTargetUpperChamberHumidity();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Current upper chamber temperature
                cells[0].setContents("Current upper chamber temperature");
                cells[1].setContents(Float.toString(sys.getCurrentUpperChamberTemperature()));
                text.setTargetUpperChamberHumidity();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Target upper chamber temperature
                cells[0].setContents("Target upper chamber temperature");
                cells[1].setContents(Float.toString(sys.getTargetUpperChamberTemperature()));
                text.setTargetUpperChamberTemperature();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Current CO2 PPM
                cells[0].setContents("Current CO2 PPM");
                cells[1].setContents(Long.toString(sys.getCurrentCO2PPM()));
                text.setTargetCO2PPM();
                cells[2].setContents(text.toString());
                printRow(out, cells);                
                // Target CO2 PPM
                cells[0].setContents("Target CO2 PPM");
                cells[1].setContents(Long.toString(sys.getTargetCO2PPM()));
                text.setTargetCO2PPM();
                cells[2].setContents(text.toString());
                printRow(out, cells);
                // Now, we do the rows with all the boolean values.
                cells = new TableCell[1];
                cells[0] = new TableCell();
                cells[0].setHeader(true);
                cells[0].setColSpan(tableRows);
                cells[0].setContents("Machinery status");
                printRow(out, cells);
                cells = new TableCell[10];
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = new TableCell();
                }
                cells[0].setContents("Power");
                cells[1].setContents("Lights");
                cells[2].setContents("Fogging");
                cells[3].setContents("Locked");
                cells[4].setContents("Doors Open");
                cells[5].setContents("Dehumidifying");
                cells[6].setContents("Cooling");
                cells[7].setContents("Injecting CO2");
                printRow(out, cells);
                
                cells[0] = booleanCell(sys.isPowered());
                cells[1] = booleanCell(sys.isLit());
                cells[2] = booleanCell(sys.isMisting());
                cells[3] = booleanCell(sys.isLocked());
                cells[4] = booleanCell(sys.isOpen());
                cells[5] = booleanCell(sys.isDehumidifying());
                cells[6] = booleanCell(sys.isCooling());
                cells[7] = booleanCell(sys.isInjectingCO2());
                printRow(out, cells);
                /*
                            public long getTimestamp();
    public float getReservoirLevel();   
   public float getNutrientSolutionLevel();
        public boolean isPowered();
    public boolean isLit(); 
    public boolean isMisting();
       public boolean isLocked();
    public boolean isOpen();    
    public long getTimeLeftUnlocked();    
    public float getUpperChamberHumidity();    
    public float getUpperChamberTemperature();    
    public float getLowerChamberTemperature();    
    public int getCurrentCO2PPM();    
    public boolean isDehumidifying();    
    public boolean isCooling();    
    public boolean isInjectingCO2(); */
            }
            out.println("</table>");
        } else { // Print error message for user.
            out.println("Could not connect to backend. :(");
        }
    }
}
    /*            <th>Property</th>
                    <th>Value</th>
                    <th>Update value (if applicable)</th>

                </tr>
                <tr>
                    <td>UID</td>
                    <td colspan="2"><% out.println(a.getUid()); %></td>
                </tr>
                <tr>
                    <td>Misting interval</td>
                    <td><% out.println(a.getMistingInterval()); %>  </td>
                    <td>"</td>
                </tr>
                <tr>
                    <td>Misting duration</td>
                    <td><% out.println(a.getMistingDuration()); %>  </td>
                    <td><input type="text" name="update misting duration" id="misting-duration-<% out.println(a.getUid()); %>"</td>
                </tr>
                <tr>
                    <td>Status update push interval</td>
                    <td><% out.println(a.getMistingInterval()); %>  </td>
                    <td><input type="text" name="update status update push interval" id="status-push-interval-<% out.println(a.getUid()); %>"</td>
                </tr>
                <tr>
                    <td>Nutrient solution to water ratio</td>
                    <td><% out.println(a.getNutrientSolutionRatio()); %></td>
                    <td><input type="text" name="update nutrient solution to water ratio" id="nutrient-ratio-<% out.println(a.getUid()); %>"</td>
                </tr>
                <tr>
                    <td>Lights-on time</td>
                    <td><% out.println(a.getLightsOnTime()); %>  </td>
                    <td><input type="text" name="update lights-on time" id="lights-on-time-<% out.println(a.getUid()); %>"</td>
                </tr>
                <tr>
                    <td>Lights-off time</td>
                    <td><% out.println(a.getLightsOffTime()); %>  </td>
                    <td><input type="text" name="update lights-off timen" id="lights-off-time-<% out.println(a.getUid()); %>"</td>
                </tr>            
                <tr>
                    <td>Target upper chamber humidity</td>
                    <td><% out.println(a.getTargetUpperChamberHumidity()); %></td>
                    <td><input type="text" name="update target upper chamber humidity" id="target-upper-humidity-<% out.println(a.getUid()); %>"</td>
                </tr>   

                <tr>
                    <td>Target upper chamber temperature</td>
                    <td><% out.println(a.getTargetUpperChamberTemperature()); %></td>
                    <td><input type="text" name="update target upper chamber temperature" id="target-upper-temperature-<% out.println(a.getUid()); %>"</td>
                </tr>

                <tr>
                    <td>Target lower chamber temperature</td>
                    <td><% out.println(a.getTargetLowerChamberTemperature()); %> </td>
                    <td><input type="text" name="update target lower chamber temperature" id="target-lower-humidity-<% out.println(a.getUid()); %>"</td>
                </tr>

                <tr>
                    <td>Target CO2 PPM</td>   
                    <td><% out.println(a.getTargetCO2PPM()); %> </td>
                    <td><input type="text" name="update target CO2 PPM" id="target-co2-ppm-<% out.println(a.getUid()); %>"</td>
                </tr>   
                <td>Current CO2 PPM</td>   
                <td><% out.println(a.getCurrentCO2PPM()); %> </td>
                <td><input type="text" name="update current CO2 PPM" id="current-co2-ppm-<% out.println(a.getUid()); %>"</td>
                <tr>
                    <th>Powered</th>
                    <th>Lit</th>
                    <th>Misting</th>
                    <th>Doors Locked</th>
                    <th>Doors Open</th>
                    <th>Dehumidifying</th>
                    <th>Cooling</th>
                    <th>Injecting CO2</th>
                </tr>
                </td>
                <td><% out.println(a.isPowered()); %></td>
                <td><%  %></td>
                <td><%  %></td>
                <td><%  %></td>
                <td><%  %></td>
                <td><%  %></td>
                <td><%  %></td>
                </tr>
     */

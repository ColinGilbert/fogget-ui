/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import noob.plantsystem.common.ArduinoProxy;

import java.util.TreeMap;
import java.io.IOException;
import java.sql.Time;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author noob
 */
public class SystemsViewTag extends SimpleTagSupport {

    protected final int horizSpan = 8;

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
        BackendCommunicationHandler backend = new BackendCommunicationHandler();
        boolean connected = backend.connect();
        if (connected) {
            TreeMap<Long, ArduinoProxy> systems = backend.getSystemsView();
            TreeMap<Long, String> descriptions = backend.getSystemDescriptionsView();
            for (ArduinoProxy sys : systems.values()) {
                out.println("<br />");
                out.println("<table>");
                int firstColSpan = 2;
                final long uid = sys.getPersistentState().getUid();
                // Print the description and uid.
                TableCell cells[] = new TableCell[2];
                TextInput textBox = new TextInput(out, sys.getPersistentState().getUid());
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;
                }
                cells[0].setColSpan(firstColSpan);
                cells[0].setHeader(true);
                cells[1].setColSpan(horizSpan - firstColSpan);
                cells[0].setContents("UID: " + Long.toString(sys.getPersistentState().getUid()));
                String cellStr;
                if (descriptions.containsKey(sys.getPersistentState().getUid())) {
                    cellStr = descriptions.get(sys.getPersistentState().getUid());
                } else {
                    cellStr = "This is the default description. Set a new one in the box below!";
                }
                cells[1].setContents(cellStr);
                printRow(out, cells);
                cellStr = "<a href=\"events.jsp?uid=";
                cellStr += sys.getPersistentState().getUid();
                cellStr += "\"> View events for this system</a>";
                textBox.setDescription();
                //textBox.setHeight(3);
                textBox.setWidth(70);
                cells[0].setContents(cellStr);
                cells[1].setColSpan(horizSpan - firstColSpan);
                cells[1].setContents(textBox.toString());
                textBox.setHeight(1);
                textBox.setWidth(8);
                printRow(out, cells);
                cells = new TableCell[3];
                // Instantiate the objects
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;//new TableCell();
                }
                for (int i = 0; i < cells.length; i++) {
                    cells[i].setHeader(true);
                }
                firstColSpan = 4;
                // Print the header.
                cells[0].setColSpan(firstColSpan);
                cells[2].setColSpan(horizSpan - firstColSpan - 2);
                cells[0].setContents("Property");
                cells[1].setContents("Value");
                cells[2].setContents("Update to");
                printRow(out, cells);
                for (int i = 0; i < cells.length; i++) {
                    cells[i].setHeader(false);
                }
                // Print the misting interval
                cells[0].setContents("Misting interval");
                cells[1].setContents(sys.getPersistentState().getMistingInterval() + "ms");
                textBox.setMistingInterval();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                // Print the misting duration
                cells[0].setContents("Misting duration");
                cells[1].setContents(sys.getPersistentState().getMistingDuration() + "ms");
                textBox.setMistingDuration();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                // Print the status push interval
                cells[0].setContents("Status push interval: ");//sys.getStatusPushUpdateInterval())
                cells[1].setContents(sys.getPersistentState().getStatusPushInterval() + "ms");
                textBox.setStatusPushInterval();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                // Nutrients to water feed ratio
                cells[0].setContents("Nutrient sol'n to water ratio");
                cells[1].setContents(Double.toString(sys.getPersistentState().getNutrientSolutionRatio()));
                textBox.setNutrientSolutionRatio();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                
                // Lights-on time
                cells[0].setContents("Lights-On time");
                textBox.setWidth(2);
                if (sys.getPersistentState().getLightsOnTime() > 0) {
                cells[1].setContents(new Time(sys.getPersistentState().getLightsOnTime()).toString());
                }
                else {
                    cells[1].setContents("OFF");
                }
                cellStr = "Hours: ";
                textBox.setLightsOnHour();
                cellStr += textBox.toString();
                cellStr += " Minutes: ";
                textBox.setLightsOnMinute();
                cellStr += textBox.toString();
                cells[2].setContents(cellStr);
                printRow(out, cells);
                
                // Lights-off time
                cells[0].setContents("Lights-Off time");
                if (sys.getPersistentState().getLightsOffTime() > 0) {
                cells[1].setContents(new Time(sys.getPersistentState().getLightsOffTime()).toString());
                }
                else {
                    cells[1].setContents("OFF");
                }
                cellStr = "Hours: ";
                textBox.setLightsOffHour();
                cellStr += textBox.toString();
                cellStr += " Minutes: ";
                textBox.setLightsOffMinute();
                cellStr += textBox.toString();
                cells[2].setContents(cellStr);
                printRow(out, cells);
                
                textBox.setWidth(0);
                textBox.setWidth(8);
                // Current upper chamber humidity
                cells[0].setContents("Current upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getTransientState().getCurrentUpperChamberHumidity()));
                textBox.setTargetUpperChamberHumidity();
                cells[2].setContents("");
                printRow(out, cells);
                // Target upper chamber humidity
                cells[0].setContents("Target upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getPersistentState().getTargetUpperChamberHumidity()));
                textBox.setTargetUpperChamberHumidity();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                // Current upper chamber temperature
                cells[0].setContents("Current upper chamber temperature ");
                cells[1].setContents(Float.toString(sys.getTransientState().getCurrentUpperChamberTemperature()));
                textBox.setTargetUpperChamberHumidity();
                cells[2].setContents("");
                printRow(out, cells);
                // Target upper chamber temperature                
                cells[0].setContents("Target upper chamber temperature");
                cells[1].setContents(Float.toString(sys.getPersistentState().getTargetUpperChamberTemperature()));
                textBox.setTargetUpperChamberTemperature();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                // Current CO2 PPM
                cells[0].setContents("Current CO2 PPM");
                cells[1].setContents(Long.toString(sys.getTransientState().getCurrentCO2PPM()));
                textBox.setTargetCO2PPM();
                cells[2].setContents("");
                printRow(out, cells);
                // Target CO2 PPM
                cells[0].setContents("Target CO2 PPM");
                cells[1].setContents(Long.toString(sys.getPersistentState().getTargetCO2PPM()));
                textBox.setTargetCO2PPM();
                cells[2].setContents(textBox.toString());
                printRow(out, cells);
                out.println("</table>");
                out.println("<p>");
                out.println("<table>");
                // Now, we do the all the boolean values.
                cells = new TableCell[8];
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = new TableCell();
                }
                for (TableCell cell : cells) {
                    cell.setHeader(true);
                    cell.setColSpan(1);
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
                for (TableCell cell : cells) {
                    cell.setHeader(false);
                }
                cells[0] = booleanCell(sys.getTransientState().isPowered());
                cells[1] = booleanCell(sys.getTransientState().isLit());
                cells[2] = booleanCell(sys.getTransientState().isMisting());
                cells[3] = booleanCell(sys.getTransientState().isLocked());
                cells[4] = booleanCell(sys.getTransientState().isOpen());
                cells[5] = booleanCell(sys.getTransientState().isDehumidifying());
                cells[6] = booleanCell(sys.getTransientState().isCooling());
                cells[7] = booleanCell(sys.getTransientState().isInjectingCO2());
                printRow(out, cells);
                cells = new TableCell[1];
                // Instantiate the objects
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;//new TableCell();
                }
                out.println("</table>");
                out.println("<br />");
                out.println("<input type=\"submit\">");
                                     }
        } else { // Print error message for user.
            out.println("Could not connect to backend. :(");
        }
    }
}

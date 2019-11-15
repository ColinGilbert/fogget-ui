/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import noob.plantsystem.common.ArduinoProxy;

import java.util.TreeMap;
import java.io.IOException;
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
                int firstColSpan = 2;
                out.println("<table>");
                // Print the description and uid.
                TableCell cells[] = new TableCell[2];
                TextArea textArea = new TextArea(out, sys.getUid());
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;
                }
                cells[0].setColSpan(firstColSpan);
                cells[0].setHeader(true);
                cells[1].setColSpan(horizSpan - firstColSpan);
                cells[0].setContents("UID: " + Long.toString(sys.getUid()));
                if (descriptions.containsKey(sys.getUid())) {
                    cells[1].setContents(descriptions.get(sys.getUid()));
                }
                else {
                    cells[1].setContents("This is the default description. Set a new one in the box below!");
                }
                printRow(out, cells);
                textArea.setDescription();
                textArea.setSize(100);
                cells[0].setColSpan(horizSpan);
                cells[0].setContents(textArea.toString());
                cells[1].setContents("");
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
                // Print the header.
                cells[0].setColSpan(firstColSpan);
                cells[2].setColSpan(horizSpan - firstColSpan - 1);
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
                textArea.setMistingInterval();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Print the misting duration
                cells[0].setContents("Misting duration");
                cells[1].setContents(sys.getMistingDuration() + "ms");
                textArea.setMistingDuration();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Print the status push interval
                cells[0].setContents("Status push interval: ");//sys.getStatusPushUpdateInterval())
                cells[1].setContents(sys.getStatusPushInterval() + "ms");
                textArea.setStatusPushInterval();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Nutrients to water feed ratio
                cells[0].setContents("Nutrient sol'n to water ratio");
                cells[1].setContents(Double.toString(sys.getNutrientSolutionRatio()));
                textArea.setNutrientSolutionRatio();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Lights-on time
                cells[0].setContents("Lights-On time");
                cells[1].setContents(Long.toString(sys.getLightsOnTime()));
                String cellStr = "H:";
                textArea.setSize(2);
                textArea.setLightsOnHour();
                cellStr += textArea.toString();
                cellStr += " M:";
                textArea.setLightsOnMinute();
                cellStr += textArea.toString();
                cells[2].setContents(cellStr);
                printRow(out, cells);
                // Lights-off time
                cells[0].setContents("Lights-Off time");
                cells[1].setContents(Long.toString(sys.getLightsOffTime()));
                textArea.setLightsOffHour();
                cellStr = "H:";
                cellStr += textArea.toString();
                textArea.setLightsOffMinute();
                cellStr += " M:";
                cellStr += textArea.toString();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                textArea.setSize(0); // Default values
                // Current upper chamber humidity
                cells[0].setContents("Current upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getCurrentUpperChamberHumidity()));
                textArea.setTargetUpperChamberHumidity();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Target upper chamber humidity
                cells[0].setContents("Target upper chamber humidity");
                cells[1].setContents(Float.toString(sys.getTargetUpperChamberHumidity()));
                textArea.setTargetUpperChamberHumidity();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Current upper chamber temperature
                cells[0].setContents("Current upper chamber temperature");
                cells[1].setContents(Float.toString(sys.getCurrentUpperChamberTemperature()));
                textArea.setTargetUpperChamberHumidity();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Target upper chamber temperature                
                cells[0].setContents("Target upper chamber temperature");
                cells[1].setContents(Float.toString(sys.getTargetUpperChamberTemperature()));
                textArea.setTargetUpperChamberTemperature();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Current CO2 PPM
                cells[0].setContents("Current CO2 PPM");
                cells[1].setContents(Long.toString(sys.getCurrentCO2PPM()));
                textArea.setTargetCO2PPM();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Target CO2 PPM
                cells[0].setContents("Target CO2 PPM");
                cells[1].setContents(Long.toString(sys.getTargetCO2PPM()));
                textArea.setTargetCO2PPM();
                cells[2].setContents(textArea.toString());
                printRow(out, cells);
                // Now, we do the rows with all the boolean values.
                cells = new TableCell[10];
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = new TableCell();
                }
                for (TableCell cell : cells) {
                    cell.setHeader(true);
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
                cells[0] = booleanCell(sys.isPowered());
                cells[1] = booleanCell(sys.isLit());
                cells[2] = booleanCell(sys.isMisting());
                cells[3] = booleanCell(sys.isLocked());
                cells[4] = booleanCell(sys.isOpen());
                cells[5] = booleanCell(sys.isDehumidifying());
                cells[6] = booleanCell(sys.isCooling());
                cells[7] = booleanCell(sys.isInjectingCO2());
                printRow(out, cells);
                cells = new TableCell[1];
                // Instantiate the objects
                for (int i = 0; i < cells.length; i++) {
                    TableCell c = new TableCell();
                    cells[i] = c;//new TableCell();
                }
                cells[0].setColSpan(horizSpan);
                cellStr = "<a href=\"events.jsp?uid=";
                cellStr += sys.getUid();
                cellStr += "\"> View events for this system.</a>";
                cells[0].setContents(cellStr);
                printRow(out, cells);
            }
            out.println("</table>");
        } else { // Print error message for user.
            out.println("Could not connect to backend. :(");
        }
    }
}

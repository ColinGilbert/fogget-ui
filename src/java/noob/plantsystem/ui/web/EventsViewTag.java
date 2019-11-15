/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import noob.plantsystem.common.ArduinoEventDescriptions;
import noob.plantsystem.common.EventRecord;

/**
 *
 * @author noob
 */
public class EventsViewTag extends SimpleTagSupport {

    private String uid;

    public void setUid(String arg) {
        uid = arg;
    }

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
            //c.setContents("Yes");
        } else {
            // c.setContents("No");
        }
        return c;
    }

    @Override
    public void doTag() throws JspException, IOException {
        try {
        long uidLocal = Long.parseLong(uid);
        JspWriter out = getJspContext().getOut();
        BackendCommunicationHandler backend = new BackendCommunicationHandler();
        out.println("<table>");
        final int horizSpan = 2;
        TreeMap<Long, String> allDescriptions = backend.getSystemDescriptionsView();
        out.println("Events for system with UID: " + uid);
        out.println("<p>");
        out.println("Description: ");
        out.println(allDescriptions.getOrDefault(Long.parseLong(uid), "No description. <a href=\"index.jsp\">Click here</a> to go back and set one!"));
        out.println("</p>");
        TableCell cells[] = new TableCell[horizSpan];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new TableCell();
        }
        for (int i = 0; i < cells.length; i++) {
            cells[i].setHeader(true);
        }
        cells[0].setContents("Time");
        cells[1].setContents("Description");
        printRow(out, cells);
        for (int i = 0; i < cells.length; i++) {
            cells[i].setHeader(false);
        }
        ArduinoEventDescriptions eventDescriptions = new ArduinoEventDescriptions();
        TreeMap<Long, ArrayDeque<EventRecord>> allEvents = backend.getEventsView();
        if (allEvents.containsKey(uidLocal)) {
            for (EventRecord event : allEvents.get(uidLocal)) {
                Date date = new Date(event.getTimestamp());
                cells[0].setContents(date.toString());
                String eventDescriptionResult = eventDescriptions.getDescription(event.getEvent()).getValue();
                    eventDescriptionResult = eventDescriptionResult.replaceAll("_", " ");
                    cells[1].setContents(eventDescriptionResult.toLowerCase());
                }
                printRow(out, cells);
            }
        out.println("</table>");
        out.println("</p>");
        } catch (NumberFormatException ex) {
                Logger.getLogger(EventsViewTag.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}

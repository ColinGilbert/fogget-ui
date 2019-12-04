/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import noob.plantsystem.common.EmbeddedSystemEventDescriptions;
import noob.plantsystem.common.EventRecordMemento;

/**
 *
 * @author noob
 */
public class EventsViewTag extends SimpleTagSupport {

    private String uid;

    public void setUid(String arg) {
        uid = arg;
    }

    protected void printRow(JspWriter writer, TableCellBuilder cells[]) throws IOException {
        writer.println("<tr>");
        for (TableCellBuilder c : cells) {
            writer.println(c.toString());
        }
        writer.println("</tr>");
    }

    protected TableCellBuilder booleanCell(boolean arg) {
        TableCellBuilder c = new TableCellBuilder();
        if (arg) {
            // c.setContents("Yes");
        } else {
            // c.setContents("No");
        }
        return c;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        BackendCommunicationFacade backend = new BackendCommunicationFacade();
        TreeMap<Long, String> allDescriptions = backend.getSystemDescriptionsView();
        TreeMap<Long, ArrayDeque<EventRecordMemento>> allEvents = null;
        TableCellBuilder cells[]  = null;
        Date date = null;
        EmbeddedSystemEventDescriptions eventDescriptions  = null;
        String eventDescriptionResult = "";
        try {
            long uidLocal = Long.parseLong(uid);
            out.println("<table>");
            final int horizSpan = 2;
            out.println("Events for system with UID: " + uid);
            out.println("<p>");
            out.println("Description: ");
            out.println(allDescriptions.getOrDefault(Long.parseLong(uid), "No description. <a href=\"index.jsp\">Click here</a> to go back and set one!"));
            out.println("</p>");
            cells = new TableCellBuilder[horizSpan];
            for (int i = 0; i < cells.length; i++) {
                cells[i] = new TableCellBuilder();
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
            eventDescriptions = new EmbeddedSystemEventDescriptions();
            allEvents = backend.getEventsView();
            if (allEvents.containsKey(uidLocal)) {
                for (EventRecordMemento event : allEvents.get(uidLocal)) {
                    date = new Time(event.getTimestamp());
                    cells[0].setContents(date.toString());
                    eventDescriptionResult = eventDescriptions.getDescription(event.getEvent()).getValue();
                    eventDescriptionResult = eventDescriptionResult.replaceAll("_", " ");
                    cells[1].setContents(eventDescriptionResult.toLowerCase());
                    printRow(out, cells);
                }
            }
            out.println("</table>");
            out.println("</p>");
        } catch (NumberFormatException ex) {
            Logger.getLogger(EventsViewTag.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EventsViewTag.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        backend.close();
    }
}

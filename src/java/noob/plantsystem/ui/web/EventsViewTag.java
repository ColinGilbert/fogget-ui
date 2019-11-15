/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import noob.plantsystem.common.ArduinoEventDescriptions;

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
        JspWriter out = getJspContext().getOut();
        BackendCommunicationHandler backend = new BackendCommunicationHandler();
        out.println("<table>");

        out.println("</table>");
    }
}

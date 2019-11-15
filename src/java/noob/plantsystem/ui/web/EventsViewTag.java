/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author noob
 */
public class EventsViewTag extends SimpleTagSupport {

    private String uid;

    public void setUid (String arg) {
        uid = arg;
    }

    @Override
    public void doTag() throws JspException, IOException {

    }
}

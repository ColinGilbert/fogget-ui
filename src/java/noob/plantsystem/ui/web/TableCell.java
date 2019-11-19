/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noob.plantsystem.ui.web;

/**
 *
 * @author noob
 */
public class TableCell {

    private String contents = "";
    private String style = "";
    private int rowSpan = 1;
    private int colSpan = 1;
    private boolean header = false;

    public String toString() {
        String results = "";
        if (header) {
            results += "<th";
        } else {
            results += "<td";
        }
        if (!"".equals(style)) {
            results +=  " class=\"" + style + "\" ";
        }
        if (rowSpan > 1) {
            results += " rowspan=\"" + rowSpan + "\" ";
        }
        if (colSpan > 1) {
            results += " colspan=\"" + colSpan + "\" ";
        }
        results += ">";
        results += contents;
        
        if (header) {
            results += "</th>";
        } else {
            results += "</td>";
        }
       return results;
     }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @param rowSpan the rowSpan to set
     */
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * @param colSpan the colSpan to set
     */
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(boolean header) {
        this.header = header;
    }
}

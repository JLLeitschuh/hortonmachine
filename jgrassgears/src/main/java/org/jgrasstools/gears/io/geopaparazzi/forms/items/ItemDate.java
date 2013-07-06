package org.jgrasstools.gears.io.geopaparazzi.forms.items;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDate implements Item {

    private String description;
    private boolean isMandatory;
    private String defaultValueStr;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public ItemDate( String description, Date defaultValue, boolean isMandatory ) {
        if (defaultValue == null) {
            defaultValueStr = "";
        } else {
            this.defaultValueStr = dateFormatter.format(defaultValue);
        }
        this.description = description;
        this.isMandatory = isMandatory;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("        {\n");
        sb.append("             \"key\": \"").append(description).append("\",\n");
        sb.append("             \"value\": \"").append(defaultValueStr).append("\",\n");
        sb.append("             \"type\": \"").append("date").append("\",\n");
        sb.append("             \"mandatory\": \"").append(isMandatory ? "yes" : "no").append("\"\n");
        sb.append("        }\n");
        return sb.toString();
    }

    @Override
    public String getKey() {
        return description;
    }

    @Override
    public void setValue( String value ) {
        defaultValueStr = value;
    }

    @Override
    public String getValue() {
        return defaultValueStr;
    }
}

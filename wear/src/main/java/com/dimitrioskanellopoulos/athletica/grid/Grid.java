package com.dimitrioskanellopoulos.athletica.grid;

import android.graphics.Color;

import com.dimitrioskanellopoulos.athletica.grid.columns.Column;
import com.dimitrioskanellopoulos.athletica.grid.rows.Row;

import java.util.Map;
import java.util.TreeMap;

public class Grid {
    private static final String TAG = "Grid";
    private final TreeMap<String, Row> rows = new TreeMap<>();
    private Integer backgroundColor = Color.BLACK;
    private Integer textColor = Color.WHITE;

    public void putRow(String rowName, Row row) {
        rows.put(rowName, row);
    }

    public void removeRow(String rowName) {
        if (!rows.containsKey(rowName)) {
            return;
        }
        rows.get(rowName).removeAllColumns();
        rows.remove(rowName);
    }

    public Row getRow(String rowName) {
        return rows.get(rowName);
    }

    TreeMap<String, Row> getAllRows() {
        return rows;
    }

    /**
     * Toggles the ambient or not mode for all the rows
     */
    public void setInAmbientMode(boolean inAmbientMode) {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.setAmbientMode(inAmbientMode);
            }
        }
    }

    /**
     * Toggles the burnInProtection
     */
    public void setBurnInProtection(boolean burnInProtection) {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.setBurnInProtection(burnInProtection);
            }
        }
    }

    /**
     * Toggles the burnInProtection
     */
    public void setLowBitAmbient(boolean lowBitAmbient) {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.setLowBitAmbient(lowBitAmbient);
            }
        }
    }

    /**
     * Toggles the antialias for ambient mode
     */
    public void shouldAntialiasInAmbientMode(boolean shouldAntialiasInAmbientMode) {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.shouldAntialiasInAmbientMode(shouldAntialiasInAmbientMode);
            }
        }
    }

    /**
     * Toggles the visible or not mode for all the columns
     */
    public void setIsVisible(boolean isVisible) {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.setIsVisible(isVisible);
            }
        }
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(Integer textColor) {
        this.textColor = textColor;
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.setTextDefaultColor(textColor);
            }
        }
    }

    Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void invertColors() {
        setBackgroundColor(getBackgroundColor() == Color.BLACK ? Color.WHITE : Color.BLACK);
        setTextColor(getTextColor() == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    public void runTasks() {
        for (Map.Entry<String, Row> rowEntry : getAllRows().entrySet()) {
            Row row = rowEntry.getValue();
            for (Map.Entry<String, Column> columnEntry : row.getAllColumns().entrySet()) {
                Column column = columnEntry.getValue();
                column.runTasks();
            }
        }
    }
}

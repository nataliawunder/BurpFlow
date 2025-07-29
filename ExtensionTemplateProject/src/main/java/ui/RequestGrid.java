package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RequestGrid extends JPanel {
    private JTable requestTable;
    private DefaultTableModel tableModel;

    private static final String[] COLUMN_NAMES = {
        "Proxy #", "Host", "Method", "URL", "Status Code", "MIME Type", "Notes", "IP"
    };

    public RequestGrid() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        requestTable = new JTable(tableModel);
        requestTable.setFillsViewportHeight(true);
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        requestTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getRequestTable() {
        return requestTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

}
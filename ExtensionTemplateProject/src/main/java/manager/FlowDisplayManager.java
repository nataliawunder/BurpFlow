// DOES NOT WORK YET

package manager;

import ui.FlowListSidebar;
import ui.RequestGrid;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.TimingData;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;

//import java.time.ZoneId;
import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class FlowDisplayManager {
    private final FlowManager flowManager;
    private final RequestGrid requestGrid;
    private final FlowListSidebar flowListSidebar;
    private final DefaultListModel<String> listModel;
    private final MontoyaApi montoyaApi;

    //private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    public FlowDisplayManager(MontoyaApi montoyaApi, FlowManager flowManager, RequestGrid requestGrid, FlowListSidebar flowListSidebar) {
        this.flowManager = flowManager;
        this.requestGrid = requestGrid;
        this.flowListSidebar = flowListSidebar;
        this.montoyaApi = montoyaApi;

        listModel = new DefaultListModel<>();
        JList<String> flowList = flowListSidebar.getFlowList();
        flowList.setModel(listModel);

        refreshFlowList();

        flowList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = flowList.getSelectedValue();
                // debug
                montoyaApi.logging().logToOutput("FlowDisplayManager selected: " + selected);
                if (selected != null) {
                    populateRequestGrid(selected);
                }
            }
        });
    }

    // public void refreshFlowList() {
    //     listModel.clear();
    //     for (String flowName : flowManager.getAllFlows().keySet()) {
    //         listModel.addElement(flowName);
    //     }
    // }

    public void refreshFlowList() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (String flowName : flowManager.getAllFlows().keySet()) {
                listModel.addElement(flowName);
            }
        });
    }


    private void populateRequestGrid(String flowName) {
        Flow flow = flowManager.getAllFlows().get(flowName);
        montoyaApi.logging().logToOutput("populateRequestGrid: " + flowName + " → " + (flow == null ? 0 : flow.getEntries().size()) + " entries");
        
        DefaultTableModel model = requestGrid.getTableModel();
        model.setRowCount(0);

        //Flow flow = flowManager.getAllFlows().get(flowName);
        montoyaApi.logging().logToOutput("flow" + flow);
        if (flow == null) {
            montoyaApi.logging().logToOutput("flow is null");
            return;
        }

        for (FlowEntry entry: flow.getEntries()) {
            try {
                montoyaApi.logging().logToOutput(
                    "[FlowDisplayManager] adding row for " + entry.messageId()
                );

                Object[] row = new Object[]{
                    entry.messageId(),
                    entry.host(),
                    entry.method(),
                    entry.url(),
                    entry.status(),
                    entry.mimeType(),
                    entry.notes(),
                    entry.ip(),
                    entry.time()
                };
                model.addRow(row);
            } 
            catch (Exception ex) {
                // THIS WILL TELL YOU WHAT BROKE
                montoyaApi.logging().logToError(
                    "[FlowDisplayManager] ERROR adding row for "
                + entry.messageId() + ": " + ex.getClass().getSimpleName()
                + " – " + ex.getMessage()
                );
                ex.printStackTrace();
            }
        }
        
        montoyaApi.logging().logToOutput("[DEBUG] model.getRowCount() = " 
        + model.getRowCount());

        // **DEBUG #2**: is this the same model the JTable is using?
        boolean sameModel = requestGrid
            .getRequestTable()
            .getModel() == model;
        montoyaApi.logging().logToOutput("[DEBUG] table.getModel()==model? " 
            + sameModel);

        // **DEBUG #3**: how many columns does the table think it has?
        int colCount = requestGrid.getRequestTable().getColumnCount();
        montoyaApi.logging().logToOutput("[DEBUG] table.getColumnCount() = " 
            + colCount);

        model.fireTableDataChanged();
        JTable updateRequestGrid = requestGrid.getRequestTable();
        updateRequestGrid.revalidate();
        updateRequestGrid.repaint();
    
    }

}

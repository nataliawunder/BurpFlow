package manager;

import ui.FlowListSidebar;
import ui.FlowPanel;
import ui.FlowSidebarController;
import ui.RequestGrid;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

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

    public FlowDisplayManager(MontoyaApi montoyaApi, FlowManager flowManager, RequestGrid requestGrid, FlowListSidebar flowListSidebar, FlowPanel flowPanel) {
        this.flowManager = flowManager;
        this.requestGrid = requestGrid;
        this.flowListSidebar = flowListSidebar;
        this.montoyaApi = montoyaApi;

        new FlowSidebarController(flowManager, this, flowListSidebar);
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

        JTable requestTable = requestGrid.getRequestTable();
        requestTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = requestTable.getSelectedRow();
            if (row < 0) {
                return;
            }

            String flowName = flowListSidebar.getFlowList().getSelectedValue();
            Flow flow = flowManager.getAllFlows().get(flowName);
            FlowEntry entry = flow.getEntries().get(row);

            HttpRequestEditor requestEditor  = flowPanel.getRequestEditor();
            HttpResponseEditor responseEditor = flowPanel.getResponseEditor();

            // show request
            if (entry.getRequest() != null) {
                requestEditor.setRequest(entry.getRequest());
            } else if (entry.getHttpRequestResponse() != null) {
                requestEditor.setRequest(entry.getHttpRequestResponse().request());
            } else {
                requestEditor.setRequest(null);
            }

            // show response
            Optional<InterceptedResponse> response = entry.getResponse();
            if (response.isPresent()) {
                responseEditor.setResponse(response.get());
            } else if (entry.getHttpRequestResponse() != null && entry.getHttpRequestResponse().response() != null) {
                responseEditor.setResponse(entry.getHttpRequestResponse().response());
            } else {
                responseEditor.setResponse(null);
            }
        });
    }

    public void refreshFlowList() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (String flowName : flowManager.getAllFlows().keySet()) {
                listModel.addElement(flowName);
            }
        });
    }

    public void refreshCurrentFlowRequests() {
        String selected = flowListSidebar.getFlowList().getSelectedValue();
        if (selected != null) {
            populateRequestGrid(selected);
        }
    }

    public void clearRequests() {
        DefaultTableModel model = requestGrid.getTableModel();
        model.setRowCount(0);
    }

    private void populateRequestGrid(String flowName) {
        Flow flow = flowManager.getAllFlows().get(flowName);
        montoyaApi.logging().logToOutput("populateRequestGrid: " + flowName + " → " + (flow == null ? 0 : flow.getEntries().size()) + " entries");
        
        DefaultTableModel model = requestGrid.getTableModel();
        model.setRowCount(0);

        if (flow == null) {
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
                    entry.ip()
                };
                model.addRow(row);
            } 
            catch (Exception ex) {
                montoyaApi.logging().logToError(
                    "[FlowDisplayManager] ERROR adding row for "
                    + entry.messageId() + ": " + ex.getClass().getSimpleName()
                    + " - " + ex.getMessage()
                );
                ex.printStackTrace();
            }
        }

        model.fireTableDataChanged();
        JTable updateRequestGrid = requestGrid.getRequestTable();
        updateRequestGrid.revalidate();
        updateRequestGrid.repaint();
    }
}

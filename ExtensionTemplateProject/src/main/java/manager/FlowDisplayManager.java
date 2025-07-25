package manager;

import ui.FlowListSidebar;
import ui.FlowPanel;
import ui.FlowSidebarController;
import ui.RequestGrid;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public FlowManager getFlowManager() {
        return flowManager;
    }

    private void populateRequestGrid(String flowName) {
        List<ProxyHttpRequestResponse> history = getFullProxyHistory();
        Flow flow = flowManager.getAllFlows().get(flowName);
        
        DefaultTableModel model = requestGrid.getTableModel();
        model.setRowCount(0);

        if (flow == null) {
            return;
        }

        // class RowData {
        //     final FlowEntry entry;
        //     final int proxyIdx;   // -1 if none
        //     RowData(FlowEntry e, int idx) { entry = e; proxyIdx = idx; }
        // }

        // List<RowData> numbered   = new ArrayList<>();
        // List<RowData> unnumbered = new ArrayList<>();

        // for (FlowEntry entry : flow.getEntries()) {
        //     HttpRequestResponse req = entry.getHttpRequestResponse();
        //     int idx = -1;
        //     if (req != null) {
        //         int found = findMatchingIndex(history, req);
        //         if (found >= 0) idx = found + 1;  // 1-based
        //     }
        //     RowData rd = new RowData(entry, idx);
        //     if (idx > 0) numbered.add(rd);
        //     else           unnumbered.add(rd);
        // }

        // numbered.sort(Comparator.comparingInt(rd -> rd.proxyIdx));

        // can switch for numbers to be last
        // List<RowData> all = new ArrayList<>(numbered);
        // all.addAll(unnumbered);

        // for (RowData rd : all) {
        //     FlowEntry entry = rd.entry;
        //     String displayNum = rd.proxyIdx > 0
        //                     ? String.valueOf(rd.proxyIdx)
        //                     : "";

        //     Object[] row = new Object[]{
        //         displayNum,
        //         entry.host(),
        //         entry.method(),
        //         entry.url(),
        //         entry.status(),
        //         entry.mimeType(),
        //         entry.notes(),
        //         entry.ip()
        //     };
        //     model.addRow(row);
        // }

        for (FlowEntry entry: flow.getEntries()) {
            try {
                HttpRequestResponse req = entry.getHttpRequestResponse();
                String displayNum =  "";

                if (req != null) {
                    int proxyIndex = findMatchingIndex(history, req);
                    if (proxyIndex > 0) {
                        displayNum = String.valueOf(proxyIndex + 1);
                    }
                } else {
                    displayNum = entry.messageId();
                }
            
                // montoyaApi.logging().logToOutput(
                //     "[FlowDisplayManager] adding row for " + entry.messageId()
                // );
                
                Object[] row = new Object[]{
                    displayNum,
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

    private int findMatchingIndex(List<ProxyHttpRequestResponse> history, HttpRequestResponse target) {
        for (int i = 0; i < history.size(); i++) {
            ProxyHttpRequestResponse candidate = history.get(i);

            if (requestsMatch(candidate, target)) {
                return i;
            }
        }
        return -1;
    }

    private boolean requestsMatch(ProxyHttpRequestResponse proxy, HttpRequestResponse http) {
        return proxy.request().equals(http.request()) && proxy.response() != null && http.response() != null && proxy.response().equals(http.response());
    }

    private List<ProxyHttpRequestResponse> getFullProxyHistory() {
        return montoyaApi.proxy().history();
    }
}
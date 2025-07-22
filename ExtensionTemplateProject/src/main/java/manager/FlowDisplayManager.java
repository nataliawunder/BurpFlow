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
                if (selected != null) {
                    populateRequestGrid(selected);
                }
            }
        });
    }

    public void refreshFlowList() {
        listModel.clear();
        for (String flowName : flowManager.getAllFlows().keySet()) {
            listModel.addElement(flowName);
        }
    }

    private void populateRequestGrid(String flowName) {
        montoyaApi.logging().logToOutput("populateRequestGrid: " + flowName + " → " + flowManager.getAllFlows().get(flowName).getEntries().size() + " requests");
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = requestGrid.getTableModel();
            model.setRowCount(0);

            Flow flow = flowManager.getAllFlows().get(flowName);
            if (flow == null) {
                return;
            }

            for (FlowEntry entry: flow.getEntries()) {
                InterceptedRequest request = entry.getRequest();
                Optional<InterceptedResponse> response = entry.getResponse();

                String id = entry.messageId();
                String host = entry.host();
                String method = entry.method();
                String url  = entry.url();
                String status = entry.status();
                String mimeType = entry.mimeType();
                String notes = entry.notes();
                String ip = entry.ip();
                ZonedDateTime time = entry.time();

                model.addRow(new Object[]{ 
                    id, host, method, url, status, mimeType, notes, ip, time 
                });
            }

            JTable updateRequestGrid = requestGrid.getRequestTable();
            updateRequestGrid.revalidate();
            updateRequestGrid.repaint();
        });

    }

}

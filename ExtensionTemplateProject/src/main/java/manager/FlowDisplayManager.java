// DOES NOT WORK YET

package manager;

import ui.FlowListSidebar;
import ui.RequestGrid;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.TimingData;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;

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

    // private void setupFlowListListener() {
    //     JList<String> flowList = flowListSidebar.getFlowList();
    //     flowList.addListSelectionListener(new ListSelectionListener() {
    //         @Override
    //         public void valueChanged(ListSelectionEvent e) {
    //             if (!e.getValueIsAdjusting()) {
    //                 String selectedFlow = flowList.getSelectedValue();
    //                 if (selectedFlow != null) {
    //                     populateRequestGrid(selectedFlow);
    //                 }
    //             }
    //         }
    //     });
    // }

    private void populateRequestGrid(String flowName) {
        montoyaApi.logging().logToOutput("populateRequestGrid: " + flowName + " → " + flowManager.getAllFlows().get(flowName).getRequests().size() + " requests");
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = requestGrid.getTableModel();
            model.setRowCount(0);

            Flow flow = flowManager.getAllFlows().get(flowName);
            if (flow == null) {
                return;
            }

            for (ProxyHttpRequestResponse requestResponse : flow.getRequests()) {
                //int id = reqRes.messageId();
                String id = "";
                String host = requestResponse.httpService().host();
                String method = requestResponse.request().method();
                String url  = requestResponse.request().url().toString();
                String status = "";
                if (requestResponse.response() != null) {
                    status = String.valueOf(requestResponse.response().statusCode());
                }
                String mimeType = "";
                if (requestResponse.response() != null) {
                    mimeType = requestResponse.response().mimeType().toString();
                }
                String notes = "";
                if (requestResponse.annotations().hasNotes()) {
                    notes = requestResponse.annotations().notes();
                }
                String ip = "";
                if (requestResponse.httpService().ipAddress() != null) {
                    ip = requestResponse.httpService().ipAddress();
                }
                ZonedDateTime time = null;
                if (requestResponse.time() != null) {
                    time = requestResponse.time();
                }

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

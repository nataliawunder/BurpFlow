// DOES NOT WORK YET

package manager;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import ui.FlowListSidebar;
import ui.RequestGrid;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FlowDisplayManager {
    private final FlowManager flowManager;
    private final RequestGrid requestGrid;
    private final FlowListSidebar flowListSidebar;
    private final DefaultListModel<String> listModel;

    public FlowDisplayManager(FlowManager flowManager, RequestGrid requestGrid, FlowListSidebar flowListSidebar) {
        this.flowManager = flowManager;
        this.requestGrid = requestGrid;
        this.flowListSidebar = flowListSidebar;

        listModel = new DefaultListModel<>();
        JList<String> flowList = flowListSidebar.getFlowList();
        flowList.setModel(listModel);

        refreshFlowList();

        setupFlowListListener();
    }

    public void refreshFlowList() {
        listModel.clear();
        for (String flowName : flowManager.getAllFlows().keySet()) {
            listModel.addElement(flowName);
        }
    }

    private void setupFlowListListener() {
        JList<String> flowList = flowListSidebar.getFlowList();
        flowList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFlow = flowList.getSelectedValue();
                    if (selectedFlow != null) {
                        populateRequestGrid(selectedFlow);
                    }
                }
            }
        });
    }

    private void populateRequestGrid(String flowName) {
        DefaultTableModel model = requestGrid.getTableModel();
        model.setRowCount(0);

        Flow flow = flowManager.getAllFlows().get(flowName);
        if (flow == null) {
            return;
        }

        for (HttpRequestResponse requestResponse : flow.getRequests()) {
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
            String time = "";

            model.addRow(new Object[]{ 
                id, host, method, url, status, mimeType, notes, ip, time 
            });
        }
    }

}

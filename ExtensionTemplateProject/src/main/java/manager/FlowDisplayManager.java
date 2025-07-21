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

    public FlowDisplayManager(FlowManager flowManager, RequestGrid requestGrid, FlowListSidebar flowListSidebar) {
        this.flowManager = flowManager;
        this.requestGrid = requestGrid;
        this.flowListSidebar = flowListSidebar;

        setupFlowListListener();
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

        for (HttpRequestResponse reqRes : flow.getRequests()) {
            //int id = reqRes.messageId();
            String host = reqRes.httpService().host();
            String method = reqRes.request().method();
            String url  = reqRes.request().url().toString();
            String status = "";
            if (reqRes.response() != null) {
                status = String.valueOf(reqRes.response().statusCode());
            }
            String mimeType = "";
            if (reqRes.response() != null) {
                mimeType = reqRes.response().mimeType().toString();
            }
            String notes = "";
            String ip = "";
            if (reqRes.httpService().ipAddress() != null) {
                ip = reqRes.httpService().ipAddress();
            }

            model.addRow(new Object[]{ 
                host, method, url, status, mimeType, notes, ip 
            });
        }
    }

}

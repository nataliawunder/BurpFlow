package manager;

import ui.FlowListSidebar;
import ui.FlowPanel;
import ui.FlowSidebarController;
import ui.RequestGrid;
import ui.UIManager;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FlowDisplayManager {
    private final FlowManager flowManager;
    private final RequestGrid requestGrid;
    private final FlowListSidebar flowListSidebar;
    private final DefaultListModel<String> listModel;
    private List<FlowEntry> visibleEntries = new ArrayList<>();
    private final MontoyaApi montoyaApi;
    private final UIManager uiManager;

    public FlowDisplayManager(MontoyaApi montoyaApi, UIManager uiManager, FlowManager flowManager, RequestGrid requestGrid, FlowListSidebar flowListSidebar, FlowPanel flowPanel) {
        this.flowManager = flowManager;
        this.requestGrid = requestGrid;
        this.flowListSidebar = flowListSidebar;
        this.montoyaApi = montoyaApi;
        this.uiManager = uiManager;

        new FlowSidebarController(flowManager, this, flowListSidebar);
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
            FlowEntry entry = visibleEntries.get(row);

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

        requestTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handlePopup(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handlePopup(e);
            }

            private void handlePopup(MouseEvent e) {
                if (e.isPopupTrigger() && requestTable.isEnabled()) {
                    int row = requestTable.rowAtPoint(e.getPoint());
                    if (row < 0) {
                        return;
                    }
                    if (!requestTable.getSelectionModel().isSelectedIndex(row)) {
                        requestTable.getSelectionModel()
                            .setSelectionInterval(row, row);
                    }
                    showRequestPopup(e, row);
                }
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

        class RowData {
            final FlowEntry entry;
            final int proxyIdx;
            RowData(FlowEntry e, int idx) { entry = e; proxyIdx = idx; }
        }

        List<RowData> numbered   = new ArrayList<>();
        List<RowData> unnumbered = new ArrayList<>();

        for (FlowEntry entry : flow.getEntries()) {
            HttpRequestResponse request = entry.getHttpRequestResponse();
            int idx = -1;
            if (request != null) {
                int found = findMatchingIndex(history, request);
                if (found >= 0) {
                    idx = found + 1;
                }
            }
            RowData rowData = new RowData(entry, idx);
            
            if (idx > 0) {
                numbered.add(rowData);
            } else {
                unnumbered.add(rowData);
            }
        }

        numbered.sort(Comparator.comparingInt(rd -> rd.proxyIdx));

        List<RowData> allData = new ArrayList<>();
        allData.addAll(unnumbered);
        allData.addAll(numbered);

        visibleEntries.clear();
        for (RowData rd : allData) {
            FlowEntry entry = rd.entry;
            String displayNum = "";
            
            boolean useProxyNumbers = uiManager.getConfig().isUsingProxyNumbers();
            if (useProxyNumbers) {
                // proxy mode: only show if we have a valid proxyIdx
                if (rd.proxyIdx > 0) {
                    displayNum = String.valueOf(rd.proxyIdx);
                } else {
                    displayNum = "";
                }
            } else {
                // sequential mode: number each row 1,2,3...
                displayNum = String.valueOf(visibleEntries.size() + 1);
            }

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
            visibleEntries.add(entry);
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

    private void showRequestPopup(MouseEvent event, int row) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem repeaterItem = new JMenuItem("Send to Repeater");
        repeaterItem.addActionListener(ae -> {
            FlowEntry entry = visibleEntries.get(row);
            if (entry.getRequest() != null) {
                montoyaApi.repeater().sendToRepeater(
                    entry.getRequest()
                );
            } else if (entry.getHttpRequestResponse() != null) {
                montoyaApi.repeater().sendToRepeater(
                    entry.getHttpRequestResponse().request()
                );
            }
        });
        menu.add(repeaterItem);

        JMenuItem intruderItem = new JMenuItem("Send to Intruder");
        intruderItem.addActionListener(ae -> {
            FlowEntry entry = visibleEntries.get(row);
            if (entry.getRequest() != null) {
                montoyaApi.intruder().sendToIntruder(
                    entry.getRequest()
                );
            } else if (entry.getHttpRequestResponse() != null) {
                montoyaApi.intruder().sendToIntruder(
                    entry.getHttpRequestResponse().request()
                );
            }
        });
        menu.add(intruderItem);

        JMenuItem deleteItem = new JMenuItem("Delete Request(s)");
        deleteItem.addActionListener(ae -> {
            deleteSelectedRequests();
        });
        menu.addSeparator();
        menu.add(deleteItem);

        menu.show(event.getComponent(), event.getX(), event.getY());
    }

    private void deleteSelectedRequests() {
        JTable table = requestGrid.getRequestTable();
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            return;
        }

        String flowName = flowListSidebar.getFlowList().getSelectedValue();
        Flow flow = flowManager.getAllFlows().get(flowName);
        if (flow == null) {
            return;
        }

        // remove entries in descending order to keep indices valid
        Arrays.sort(rows);
        for (int i = rows.length - 1; i >= 0; i--) {
            FlowEntry toRemove = visibleEntries.get(rows[i]);
            flow.getEntries().remove(toRemove);
        }
        
        populateRequestGrid(flowName);
    }
}
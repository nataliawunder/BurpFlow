package ui;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.proxy.ProxyHttpRequestResponse;
import manager.FlowDisplayManager;
import manager.FlowManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContextMenu implements ContextMenuItemsProvider {

    private final MontoyaApi montoyaApi;
    private final FlowManager flowManager;
    private final FlowDisplayManager flowDisplayManager;

    public ContextMenu(MontoyaApi montoyaApi, FlowManager flowManager, FlowDisplayManager flowDisplayManager) {
        this.montoyaApi = montoyaApi;
        this.flowManager = flowManager;
        this.flowDisplayManager = flowDisplayManager;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        List<Component> menuItemList = new ArrayList<>();

        // TO-DO: MAKE ADD NOTE FOR WHEN IT STARTS AND STOPS
        JMenuItem beginFlowItem = new JMenuItem("Begin Flow on Next Http");
        beginFlowItem.addActionListener(l -> {
            String flowName = flowManager.createNextSequentialFlow();
            flowManager.setActiveFlow(flowName);
            montoyaApi.logging().logToOutput("Started new flow: " + flowName);
            flowDisplayManager.refreshFlowList();
        });
        menuItemList.add(beginFlowItem);

        JMenuItem stopFlowItem = new JMenuItem("Stop Current Flow");
        stopFlowItem.addActionListener(l -> {
            flowManager.endCurrentFlow();
            montoyaApi.logging().logToOutput("Stopped current flow.");
        });
        menuItemList.add(stopFlowItem);

        JMenu addRequestMenu = new JMenu("Add Request to Flow");
        JMenuItem createNewFlowItem = new JMenuItem("Create New Flow");
        createNewFlowItem.addActionListener(l -> {
            String newFlow = flowManager.createNextSequentialFlow();
            List<HttpRequestResponse> selectedItems = event.selectedRequestResponses();
            for (HttpRequestResponse item : selectedItems) {
                flowManager.addRequestToFlow(newFlow, (ProxyHttpRequestResponse) item);
                item.annotations().setHighlightColor(HighlightColor.BLUE);
            }
            montoyaApi.logging().logToOutput("Created new flow and added " + selectedItems.size() + " request(s): " + newFlow);
            flowDisplayManager.refreshFlowList();
        });
        addRequestMenu.add(createNewFlowItem);
        addRequestMenu.addSeparator();

        for (String flowName : flowManager.getAllFlows().keySet()){
            JMenuItem flowItem = new JMenuItem(flowName);
            flowItem.addActionListener(l -> {
                List<HttpRequestResponse> selectedItems = event.selectedRequestResponses();
                for (HttpRequestResponse item : selectedItems) {
                    flowManager.addRequestToFlow(flowName, (ProxyHttpRequestResponse) item);
                    item.annotations().setHighlightColor(HighlightColor.BLUE);
                }
                montoyaApi.logging().logToOutput("Added " + selectedItems.size() + " request(s) to flow: " + flowName);
            });
            addRequestMenu.add(flowItem);
        }

        menuItemList.add(addRequestMenu);

        return menuItemList;
    }
}

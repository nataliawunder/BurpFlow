package ui;

import burp.api.montoya.MontoyaApi;

import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import manager.FlowManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContextMenu implements ContextMenuItemsProvider {

    private final MontoyaApi montoyaApi;
    private final FlowManager flowManager;

    public ContextMenu(MontoyaApi montoyaApi, FlowManager flowManager) {
        this.montoyaApi = montoyaApi;
        this.flowManager = flowManager;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        List<Component> menuItemList = new ArrayList<>();

        JMenuItem beginFlowItem = new JMenuItem("Begin Flow on Next Http");
        // TODO: Implement listener to begin a flow
        beginFlowItem.addActionListener(l -> {
            String flowName = flowManager.createNextSequentialFlow();
            flowManager.setActiveFlow(flowName);
            montoyaApi.logging().logToOutput("Started new flow: " + flowName);
        });
        menuItemList.add(beginFlowItem);

        JMenuItem stopFlowItem = new JMenuItem("Stop Current Flow");
        // TODO: Implement listener to stop a flow
        stopFlowItem.addActionListener(l -> {
            flowManager.endCurrentFlow();
            montoyaApi.logging().logToOutput("Stopped current flow.");
        });
        menuItemList.add(stopFlowItem);

        JMenu addRequestMenu = new JMenu("Add Request to Flow");
        // TODO: Implement loop that creates JMenuItem for all flows available
        
        // JMenuItem menuItem3 = new JMenuItem("Add Request to Flow");
        // // have logic to show new context menu 
        // menuItem3.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        // menuItemList.add(menuItem3);

        menuItemList.add(addRequestMenu);

        return menuItemList;
    }
}

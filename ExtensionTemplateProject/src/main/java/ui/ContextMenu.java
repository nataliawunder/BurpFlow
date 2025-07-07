package ui;

import burp.api.montoya.MontoyaApi;

import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContextMenu implements ContextMenuItemsProvider {

    private final MontoyaApi montoyaApi;

    public ContextMenu(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        List<Component> menuItemList = new ArrayList<>();

        JMenuItem menuItem = new JMenuItem("Begin Flow on Next Http");
        menuItem.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        menuItemList.add(menuItem);

        JMenuItem menuItem2 = new JMenuItem("Stop Current Flow");
        menuItem2.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        menuItemList.add(menuItem2);

        JMenuItem menuItem3 = new JMenuItem("Add Request to Flow");
        // have logic to show new context menu 
        menuItem3.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        menuItemList.add(menuItem3);

        return menuItemList;
    }

    // public List<Component> provideFlowItems(ContextMenuEvent event) {

    //     List<Component> menuFlowList = new ArrayList<>();

    //     JMenuItem FlowItem = new JMenuItem("Flow");
    //     FlowItem.addActionListener(l -> {montoyaApi.logging().logToOutput("Flow");});
    //     menuFlowList.add(FlowItem);

    //     return menuFlowList;
    // }
}

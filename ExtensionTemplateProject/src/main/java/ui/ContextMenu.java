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
        // TODO: Implement listener to begin a flow
        menuItem.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        menuItemList.add(menuItem);

        JMenuItem menuItem2 = new JMenuItem("Stop Current Flow");
        // TODO: Implement listener to stop a flow
        menuItem2.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});
        menuItemList.add(menuItem2);

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

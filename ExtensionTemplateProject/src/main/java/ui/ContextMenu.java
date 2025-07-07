package ui;

import burp.api.montoya.MontoyaApi;

import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContextMenu implements ContextMenuItemsProvider {

    private final MontoyaApi montoyaApi;

    public ContextMenu(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
    }

    // context menu
    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {
        JMenuItem menuItem = new JMenuItem("Begin Flow on Next Http");
        menuItem.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});

        return List.of(menuItem);
    }
}

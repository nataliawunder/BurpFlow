import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

// context menu items
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");

        // TODO Add your code here
        // context menu
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ContextMenuItemsProvider() {
            @Override
            public List<Component> provideMenuItems(ContextMenuEvent event) {
                JMenuItem menuItem = new JMenuItem("Begin Flow on Next Http");
                menuItem.addActionListener(l -> {montoyaApi.logging().logToOutput("Hello, World.");});

                return List.of(menuItem);
            }
        });
        
        // custom tab
        JPanel panel = new JPanel();
        panel.add(new JLabel("BurpFlow"));
        montoyaApi.userInterface().registerSuiteTab("BurpFlow", panel);
        
    }
}
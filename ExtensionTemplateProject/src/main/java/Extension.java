import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ui.ContextMenu(montoyaApi));
        
        // custom tab
        JPanel panel = new JPanel();
        panel.add(new JLabel("BurpFlow"));
        montoyaApi.userInterface().registerSuiteTab("BurpFlow", panel);
        
    }
}
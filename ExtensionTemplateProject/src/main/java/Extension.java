import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ui.UIManager;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ui.ContextMenu(montoyaApi));
        
        UIManager uiManager = new UIManager(montoyaApi);
        uiManager.registerUI();
        
    }
}
import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ui.UIManager;
import manager.*;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ui.ContextMenu(montoyaApi));
        
        // may need to configure the UI manager to the flow manager in order to intertwine the two
        UIManager uiManager = new UIManager(montoyaApi);
        uiManager.registerUI();

        FlowManager flowManager = new FlowManager();
        RequestManager requestManager = new RequestManager(flowManager);
        RequestProcessor requestProcessor = new RequestProcessor(requestManager);
        montoyaApi.proxy().registerRequestHandler(requestProcessor);
        
    }
}
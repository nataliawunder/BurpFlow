import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import ui.FlowPanel;
import ui.*;
import ui.UIManager;
import manager.*;

public class Extension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow3");

        // may need to configure the UI manager to the flow manager in order to intertwine the two
        UIManager uiManager = new UIManager(montoyaApi);
        uiManager.registerUI();
        
        FlowManager flowManager = new FlowManager();
        RequestManager requestManager = new RequestManager(montoyaApi, flowManager);
        RequestProcessor requestProcessor = new RequestProcessor(requestManager);
        montoyaApi.proxy().registerRequestHandler(requestProcessor);
        montoyaApi.proxy().registerResponseHandler(requestProcessor);

        FlowPanel flowPanel = uiManager.getFlowPanel();
        FlowListSidebar flowListSidebar = flowPanel.getFlowListSidebar();
        RequestGrid requestGrid = flowPanel.getRequestGrid();

        FlowDisplayManager flowDisplayManager = new FlowDisplayManager(montoyaApi, flowManager, requestGrid, flowListSidebar, flowPanel);
        
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ui.ContextMenu(montoyaApi, flowManager, flowDisplayManager));
        
    }
}
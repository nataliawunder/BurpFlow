import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.persistence.PersistedObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ui.*;
import ui.UIManager;
import manager.*;

public class Extension implements BurpExtension {
    private FlowManager flowManager;

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");

        UIManager uiManager = new UIManager(montoyaApi);
        uiManager.registerUI();
        
        flowManager = new FlowManager();
        
        // restore flows from the project
        PersistedObject root = montoyaApi.persistence().extensionData();

        for (String flowName : root.childObjectKeys()) {
            PersistedObject flowPersisted = root.getChildObject(flowName);
            Flow flow = new Flow(flowName);

            // restore active state
            Boolean active = flowPersisted.getBoolean("active");
            if (active != null && active) {
                flow.setActive(true);
            }

            // restore each stored request in key order, sort by numberic suffix
            List<String> requestKeys = new ArrayList<>(flowPersisted.httpRequestResponseKeys());
            Collections.sort(requestKeys, Comparator.comparingInt(k -> Integer.parseInt(k.substring(3))));
            for (String key : requestKeys) {
                HttpRequestResponse httpRequestResponse = flowPersisted.getHttpRequestResponse(key);
                flow.addEntry(new FlowEntry(httpRequestResponse));
            }

            flowManager.getAllFlows().put(flowName, flow);
        }

        Integer persistedCounter = root.getInteger("flowCounter");
        if (persistedCounter != null) {
            flowManager.setNextFlowCounter(persistedCounter);
        }

        RequestManager requestManager = new RequestManager(montoyaApi, flowManager);
        RequestProcessor requestProcessor = new RequestProcessor(requestManager);
        montoyaApi.proxy().registerRequestHandler(requestProcessor);
        montoyaApi.proxy().registerResponseHandler(requestProcessor);

        FlowPanel flowPanel = uiManager.getFlowPanel();
        FlowListSidebar flowListSidebar = flowPanel.getFlowListSidebar();
        RequestGrid requestGrid = flowPanel.getRequestGrid();

        FlowDisplayManager flowDisplayManager = new FlowDisplayManager(montoyaApi, flowManager, requestGrid, flowListSidebar, flowPanel);
        
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ui.ContextMenu(montoyaApi, flowManager, flowDisplayManager));
        
        // unload with persistence
        montoyaApi.extension().registerUnloadingHandler(new UnloadingHandler(montoyaApi, flowDisplayManager));
    }
}
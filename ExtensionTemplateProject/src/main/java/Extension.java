import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.persistence.PersistedObject;
import burp.api.montoya.persistence.Preferences;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ui.*;
import ui.UIManager;
import manager.*;

public class Extension implements BurpExtension {
    private FlowManager flowManager;

    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("BurpFlow");

        // maintain persistence
        // if (montoyaApi.persistence().preferences().stringKeys().contains("")) {
        //     _ = montoyaApi.persistence().preferences().getString("");
        // }

        // pass it into the New instance

        UIManager uiManager = new UIManager(montoyaApi);
        uiManager.registerUI();
        
        flowManager = new FlowManager();
        // 2) Try to restore flows from the project
        PersistedObject root = montoyaApi.persistence().extensionData();

        for (String flowName : root.childObjectKeys()) {
            PersistedObject flowP = root.getChildObject(flowName);
            Flow flow = new Flow(flowName);

            // restore active state
            Boolean active = flowP.getBoolean("active");
            if (active != null && active) {
                flow.setActive(true);
            }

            // restore each stored request in key order
            List<String> reqKeys = new ArrayList<>(flowP.httpRequestResponseKeys());
            // sort by numeric suffix
            Collections.sort(reqKeys, Comparator.comparingInt(k -> Integer.parseInt(k.substring(3))));
            for (String rk : reqKeys) {
                HttpRequestResponse hrr = flowP.getHttpRequestResponse(rk);
                flow.addEntry(new FlowEntry(hrr));
            }

            flowManager.getAllFlows().put(flowName, flow);
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
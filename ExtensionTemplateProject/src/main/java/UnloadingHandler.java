import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.persistence.PersistedObject;
import manager.FlowDisplayManager;
import manager.FlowEntry;
import manager.Flow;

import java.util.Map;

public class UnloadingHandler implements ExtensionUnloadingHandler{

    private MontoyaApi montoyaApi;
    private FlowDisplayManager flowDisplayManager;
    // private final Gson gson = new Gson();

    public UnloadingHandler(MontoyaApi montoyaApi, FlowDisplayManager flowDisplayManager) {
        this.montoyaApi = montoyaApi;
        this.flowDisplayManager = flowDisplayManager;

    }

    @Override
    public void extensionUnloaded() {
        PersistedObject root = montoyaApi.persistence().extensionData();

        // 1) Clear out any old flows
        for (String key : root.childObjectKeys()) {
            root.deleteChildObject(key);
        }

        // 2) For each flow, create a child PersistedObject
        for (Map.Entry<String, Flow> fe : displayMgr.getFlowManager().getAllFlows().entrySet()) {
            String flowName = fe.getKey();
            Flow flow       = fe.getValue();
            PersistedObject flowP = PersistedObject.persistedObject();
            root.setChildObject(flowName, flowP);

            flowP.setBoolean("active", flow.isActive());

            int i = 0;
            for (FlowEntry entry : flow.getEntries()) {
                String key = "r" + (i++);
                if (entry.getHttpRequestResponse() != null) {
                    // manual‐added
                    flowP.setHttpRequestResponse(key, entry.getHttpRequestResponse());
                }
                else if (entry.getRequest() != null) {
                    // intercepted request
                    flowP.setProxyHttpRequestResponse(key, entry.getRequest());
                }
            }
        }
    }
}
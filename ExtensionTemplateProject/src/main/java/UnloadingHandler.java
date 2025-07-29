import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.persistence.PersistedObject;
import manager.FlowDisplayManager;
import manager.FlowEntry;
import manager.Flow;
import config.Config;

import java.util.Map;

public class UnloadingHandler implements ExtensionUnloadingHandler{

    private MontoyaApi montoyaApi;
    private FlowDisplayManager flowDisplayManager;
    private Config config = new Config();

    public UnloadingHandler(MontoyaApi montoyaApi, FlowDisplayManager flowDisplayManager, Config config) {
        this.montoyaApi = montoyaApi;
        this.flowDisplayManager = flowDisplayManager;
        this.config = config;

    }

    @Override
    public void extensionUnloaded() {
        PersistedObject root = montoyaApi.persistence().extensionData();

        // clear out any old flows
        for (String key : root.childObjectKeys()) {
            root.deleteChildObject(key);
        }

        // for each flow, create a child PersistedObject
        for (Map.Entry<String, Flow> flowEntry : flowDisplayManager.getFlowManager().getAllFlows().entrySet()) {
            String flowName = flowEntry.getKey();
            Flow flow = flowEntry.getValue();

            // attach a new child object
            PersistedObject flowPersisted = PersistedObject.persistedObject();
            root.setChildObject(flowName, flowPersisted);

            // store active flag
            flowPersisted.setBoolean("active", flow.isActive());

            // store each HttpRequestResponse under "req0", "req1", ...
            int i = 0;
            for (FlowEntry entry : flow.getEntries()) {
                HttpRequestResponse httpRequestResponse = entry.getHttpRequestResponse();
                if (httpRequestResponse != null) {
                    flowPersisted.setHttpRequestResponse("req" + (i++), httpRequestResponse);
                }
            }
        }

        root.setBoolean("useProxyNumbers", config.isUsingProxyNumbers());
        
        root.setInteger("flowCounter", flowDisplayManager.getFlowManager().getNextFlowCounter());
    }
}
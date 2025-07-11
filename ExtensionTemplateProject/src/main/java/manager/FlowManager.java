package manager;

import java.util.HashMap;
import java.util.Map;

import burp.api.montoya.http.message.requests.HttpRequest;

public class FlowManager {

    private final Map<String, Flow> flowMap;

    public FlowManager() {
        flowMap = new HashMap<>();
    }
    
    public void createFlow(String flowName) {
        if (!flowMap.containsKey(flowName)) {
            flowMap.put(flowName, new Flow(flowName));
        }
    }

    public void addRequestToFlow(String flowName, HttpRequest request) {
        Flow flow = flowMap.get(flowName);
        if (flow != null && flow.isActive()) {
            flow.addRequest(request);
        }
    }

    // TODO: make a addRequestsToFlow plural
    // Maybe make a non active version as well if in UI

    public void beginFlow(String flowName) {
        Flow flow = flowMap.get(flowName);
        if (flow != null) {
            flow.setActive(true);
        }
    }

    public void endFlow(String flowName) {
        Flow flow = flowMap.get(flowName);
        if (flow != null) {
            flow.setActive(false);
        }
    }

    public void deleteFlow(String flowName) {
        flowMap.remove(flowName);
    }

    public Map<String, Flow> getAllFlows() {
        return flowMap;
    }
}

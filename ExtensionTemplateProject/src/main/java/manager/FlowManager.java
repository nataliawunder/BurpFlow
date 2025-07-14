package manager;

import java.util.HashMap;
import java.util.Map;

import burp.api.montoya.http.message.requests.HttpRequest;

public class FlowManager {

    private final Map<String, Flow> flowMap;
    private String activeFlowName = null;
    private int flowCounter = 1;

    public FlowManager() {
        this.flowMap = new HashMap<>();
        this.activeFlowName = null;
    }

    // functions for context menu
    public String createNextSequentialFlow() {
        String newFlowName = "New Flow " + flowCounter++;
        flowMap.put(newFlowName, new Flow(newFlowName));
        return newFlowName;
    }
    
    public void setActiveFlow(String flowName) {
        if (flowMap.containsKey(flowName)) {
            endCurrentFlow();
            activeFlowName = flowName;
            flowMap.get(flowName).setActive(true);
        }
    }

    public void endCurrentFlow() {
        if (activeFlowName != null) {
            Flow flow = flowMap.get(activeFlowName);
            if (flow != null) {
                flow.setActive(false);
            }
            activeFlowName = null;
        }
    }

    public boolean isFlowActive() {
        return activeFlowName != null && flowMap.containsKey(activeFlowName) && flowMap.get(activeFlowName).isActive();
    }

    public String getActiveFlowName() {
        return activeFlowName;
    }

    public void addRequestToActiveFlow(HttpRequest request) {
        if (activeFlowName != null) {
            Flow flow = flowMap.get(activeFlowName);
            if (flow != null && flow.isActive()) {
                flow.addRequest(request);
            }
        }
    }

    // functions for UI
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

    // common functions
    public void deleteFlow(String flowName) {
        if (flowName.equals(activeFlowName)) {
            endCurrentFlow();
        }
        flowMap.remove(flowName);
    }

    public Map<String, Flow> getAllFlows() {
        return flowMap;
    }
}

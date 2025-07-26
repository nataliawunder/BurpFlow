package manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.InterceptedResponse;

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
                List<FlowEntry> entry = flow.getEntries();
                if (!entry.isEmpty()) {
                    FlowEntry last = entry.get(entry.size() - 1);
                    InterceptedRequest request = last.getRequest();
                    request.annotations().setNotes("End of " + activeFlowName);
                }
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

    // tweak so only one shows up
    public void addRequestToActiveFlow(InterceptedRequest request) {
        if (activeFlowName != null) {
            Flow flow = flowMap.get(activeFlowName);
            if (flow.getEntries().isEmpty()) {
                request.annotations().setNotes("Start of " + activeFlowName);
            }
            if (flow != null && flow.isActive()) {
                FlowEntry entry = new FlowEntry(request);
                flow.addEntry(entry);
            }
        }
    }

    public void addResponseToFlow(InterceptedResponse response) {
        if (!isFlowActive()) {
            return;
        }

        Flow flow = flowMap.get(activeFlowName);
        long id = response.messageId();
       
        for (FlowEntry entry : flow.getEntries()) {
            if (String.valueOf(id).equals(entry.messageId())) {
                entry.setResponse(response);
                break;
            }
        }
    }

    // functions for UI
    public void createFlow(String flowName) {
        if (!flowMap.containsKey(flowName)) {
            flowMap.put(flowName, new Flow(flowName));
        }
    }

    public void addRequestToFlow(String flowName, HttpRequestResponse request) {
        Flow flow = flowMap.get(flowName);
        if (flow != null) {
            if (flow.getEntries().isEmpty()) {
                request.annotations().setNotes("Adding to " + flowName);
            }
            flow.addEntry(request);
        }
    }

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

    public void deleteSingleEntry(FlowEntry entry) {

    }

    public void renameFlow(String oldName, String newName) {
        if (!flowMap.containsKey(oldName) || flowMap.containsKey(newName)) {
            return;
        }

        Flow flow = flowMap.remove(oldName);
        flow.setFlowName(newName);
        flowMap.put(newName, flow);

        if (oldName.equals(activeFlowName)) {
            activeFlowName = newName;
        }
    }

    public Map<String, Flow> getAllFlows() {
        return flowMap;
    }
}
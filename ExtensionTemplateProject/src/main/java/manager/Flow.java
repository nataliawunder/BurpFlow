package manager;

import java.util.ArrayList;
import java.util.List;
import burp.api.montoya.http.message.HttpRequestResponse;

public class Flow {
    private final String flowName;
    private final List<FlowEntry> entries = new ArrayList<>();
    private boolean isActive = false;

    public Flow(String flowName) { 
        this.flowName = flowName;
    }

    // proxy begin flow
    public void addEntry(FlowEntry flowEntry) {
        entries.add(flowEntry);
        System.out.println("Flow " + flowName + " now has " + entries.size() + " entries");
    }

    // context‑menu manual add
    public void addEntry(HttpRequestResponse httpRR) {
        FlowEntry flowEntry = new FlowEntry(httpRR);
        entries.add(flowEntry);
        System.out.println("Flow " + flowName + " now has " + entries.size() + " entries (menu-added)");
    }

    public List<FlowEntry> getEntries() {
        return entries;
    }

    public void removeEntry(FlowEntry flowEntry) {
        entries.remove(flowEntry);
    }

    public String getFlowName() {
        return flowName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
    

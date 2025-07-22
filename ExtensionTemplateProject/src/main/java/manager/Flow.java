package manager;

import java.util.ArrayList;
import java.util.List;

public class Flow {
    private final String flowName;
    private final List<FlowEntry> entries = new ArrayList<>();
    private boolean isActive = false;

    public Flow(String flowName) { 
        this.flowName = flowName;
    }

    public void addEntry(FlowEntry flowEntry) {
        entries.add(flowEntry);
        System.out.println("Flow " + flowName + " now has " + entries.size() + " entries");
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
    
